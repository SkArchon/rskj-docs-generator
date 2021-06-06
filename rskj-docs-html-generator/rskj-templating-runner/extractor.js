'use strict';

const marked = require("marked");
const hljs = require('highlight.js');
const common = require('./common');

class Extractor {

  processAppComponent(documentationJson) {
    // Separate read methods and write methods
    // as we want to align them and group them separately
    const readMethods = documentationJson.methodDetails
      .filter(entry => entry.methodType !== 'WRITE')
      .map(entry => common.processEntryWithTitle(entry));

    const writeMethods = documentationJson.methodDetails
      .filter(entry => entry.methodType === 'WRITE')
      .map(entry => common.processEntryWithTitle(entry));

    const models = Object.keys(documentationJson.models)
      .map(modelKey => {
        const processedData = {
          modelKey: modelKey,
          className: `${modelKey}Model`,
          fileName: `${modelKey}Model.vue`,
        };
        return processedData;
      })

    // Create data object for generating complete documentation
    const processedData = {
      title: documentationJson.title,
      description: documentationJson.description,
      readMethods: readMethods,
      writeMethods: writeMethods,
      models: models
    }

    common.processHtml('appComponent', processedData, 'App', null);
  }

  /**
   * Process the model entries into their own section
   */
  processModel(modelKey, models) {
    const model = models[modelKey];
    const modelHtml = common.castString(hljs.highlight(model, { language: 'typescript' }).value);

    const processedData = {
      modelName: modelKey,
      modelHtml: modelHtml,
      className: `${modelKey}Model`,
      fileName: `${modelKey}Model.vue`,
    };

    common.processHtml('modelEntry', { model: processedData }, processedData.className);
  }

  _setRequestExamples(requestDetails) {
    // We need to load the request examples, in case of none set a default
    const requestExamplesExist = requestDetails.requestExamples && requestDetails.requestExamples.length > 0;
    requestDetails.requestExamples = (requestExamplesExist)
      ? requestDetails.requestExamples
      : ['{}'];
  }
  
  processMethod(documentationDataset) {
    const { method, requestDetails, responseDetails } = documentationDataset;

    this._setRequestExamples(requestDetails);
      
    const { requestExamples, inputParams } = requestDetails;
    const actualRequestExamples = [...requestExamples];

    requestExamples.forEach((requestExample, index) => {
      const processedJson = common.processJson(requestExample, true, method);
      requestExamples[index] = common.castString(hljs.highlight(processedJson, { language: 'json' }).value);
    });

    // Process all input params (also replace new line with br to drop it to separate lines)
    let inputParamsPresent = false;
    inputParams.forEach((inputParamPosition) => {
      inputParamPosition.forEach((inputParam) => {
        // Create description as markdown
        inputParam.description = marked(inputParam.description.replace('\\n', '<br/>'));
        inputParamsPresent = true;
      });
    });

    // Process all responses (also replace new line with br to drop it to separate lines)
    if (responseDetails) {
      documentationDataset.responseDetails = responseDetails.map((response) => {
        const processedExample = common.processJson(response.responseExample, true, method);
        // Create description as markdown
        return {
          ...response,
          description: marked(response.description.replace('\\n', '<br/>')),
          responseExample: common.castString(hljs.highlight(processedExample, { language: 'json' }).value)
        }
      });
    }

    // construct generic dto for processing by the ejs template engine
    const processedData = {
      documentationDataset: documentationDataset,
      processedAdditionalData: {
        inputParamsPresent: inputParamsPresent,
        actualRequestExamples: actualRequestExamples,
        className: common.processTitle(method),
        hasRequestSchema: !!requestDetails.schema,
        hasResponseSchema: !!documentationDataset.responseSchema
      }
    };

    const className = processedData.processedAdditionalData.className;
    common.processHtml('methodEntry', processedData, className);

    // When there is a schema we want to process it as a json as well
    if(!processedData.hasRequestSchema) {
      common.processSchema(requestDetails.schema, className, 'request');
    }
    if(!processedData.hasResponseSchema) {
      common.processSchema(documentationDataset.responseSchema, className, 'response');
    }
  }

}

module.exports = new Extractor();

