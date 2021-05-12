'use strict';

const fs = require('fs-extra')
const ejs = require('ejs');
const marked = require("marked");
const hljs = require('highlight.js');
const common = require('./common');

class Extract {

  clean(path) {
    fs.emptyDirSync(`${path}/src/components`);
    fs.removeSync(`${path}/src/App.vue`);
  }

  processEntryWithTitle(entry) {
    const title = common.processTitle(entry.method);
    return {
      ...entry,
      className: title,
      fileName: `${title}.vue`
    }
  }

  processAppComponent(documentationJson, htmlPath) {
    documentationJson.methodDetails.forEach(entry => {
      return this.processMethod(entry, htmlPath);
    });

    Object.keys(documentationJson.models)
      .forEach(modelKey => {
        this.processModel(modelKey, documentationJson.models, htmlPath);
      });

    const template = fs.readFileSync(`${htmlPath}/templates/appComponent.ejs`).toString();

    const readMethods = documentationJson.methodDetails
      .filter(entry => entry.methodType !== 'WRITE')
      .map(entry => this.processEntryWithTitle(entry));

    const writeMethods = documentationJson.methodDetails
      .filter(entry => entry.methodType === 'WRITE')
      .map(entry => this.processEntryWithTitle(entry));

    const models = Object.keys(documentationJson.models)
      .map(modelKey => {
        const processedData = {
          modelKey: modelKey,
          className: `${modelKey}Model`,
          fileName: `${modelKey}Model.vue`,
        };
        return processedData;
      })

    const processedData = {
      title: documentationJson.title,
      description: documentationJson.description,
      readMethods: readMethods,
      writeMethods: writeMethods,
      models: models
    }

    const html = ejs.render(template, processedData);
    fs.writeFileSync(`${htmlPath}/src/App.vue`, html);
  }

  processModel(modelKey, models, htmlPath) {
    const model = models[modelKey];
    const modelHtml = common.castString(hljs.highlight(model, { language: 'typescript' }).value);

    const processedData = {
      modelName: modelKey,
      modelHtml: modelHtml,
      className: `${modelKey}Model`,
      fileName: `${modelKey}Model.vue`,
    };

    const template = fs.readFileSync(`${htmlPath}/templates/modelEntry.ejs`).toString();
    const html = ejs.render(template, { model: processedData });
    fs.writeFileSync(`${htmlPath}/src/components/${processedData.className}.vue`, html);
  }

  processMethod(documentationDataset, htmlPath) {
    const template = fs.readFileSync(`${htmlPath}/templates/methodEntry.ejs`).toString();
    const method = documentationDataset.method;

    const requestExamples = documentationDataset.requestDetails.requestExamples;
    const requestExamplesExist = requestExamples && requestExamples.length > 0;
    documentationDataset.requestDetails.requestExamples = (requestExamplesExist)
      ? documentationDataset.requestDetails.requestExamples
      : ['{}'];
      
    const actualRequestExamples = [...documentationDataset.requestDetails.requestExamples];

    documentationDataset.requestDetails.requestExamples.forEach((requestExample, index) => {
      const processedJson = common.processJson(requestExample, true, method);
      documentationDataset.requestDetails.requestExamples[index] = common.castString(hljs.highlight(processedJson, { language: 'json' }).value);
    });

    let inputParamsPresent = false;
    documentationDataset.requestDetails.inputParams.forEach((inputParamPosition) => {
      inputParamPosition.forEach((inputParam) => {
        inputParam.description = marked(inputParam.description.replace('\\n', '<br/>'));
        inputParamsPresent = true;
      });
    });

    if (documentationDataset.responseDetails) {
      documentationDataset.responseDetails.forEach((response) => {
        response.description = marked(response.description.replace('\\n', '<br/>'));
        const processedExample = common.processJson(response.responseExample, true, method);
        response.responseExample = common.castString(hljs.highlight(processedExample, { language: 'json' }).value);
      });
    }

    const processedData = {
      documentationDataset: documentationDataset,
      processedAdditionalData: {
        inputParamsPresent: inputParamsPresent,
        actualRequestExamples: actualRequestExamples,
        className: common.processTitle(method)
      }
    };

    const html = ejs.render(template, processedData);
    fs.writeFileSync(`${htmlPath}/src/components/${processedData.processedAdditionalData.className}.vue`, html);
  }
}

module.exports = new Extract();

