'use strict';

const extract = require('./extractor');
const fs = require('fs-extra');
const common = require('./common');

class ExtractWrapper {

  _HTML_PATH = '../rskj-docs';

  startProcess(inputJsonFilePath) {
    if(!inputJsonFilePath) {
      throw 'There was no input json file path passed';
    }
    
    common.setRootPathForProcessing(this._HTML_PATH);
    common.clean();
  
    const documentationJson = JSON.parse(fs.readFileSync(inputJsonFilePath));
    this._processDocumentation(documentationJson);  
  }

  _processDocumentation(documentationJson) {
    this._processAllMethods(documentationJson.methodDetails);
    this._processAllModels(documentationJson.models);
    extract.processAppComponent(documentationJson);
  }

  _processAllModels(models) {
    // Process all the models in the input json
    Object.keys(models)
      .forEach(modelKey => extract.processModel(modelKey, models));
  }

  _processAllMethods(methodDetails) {
    methodDetails.forEach(entry => extract.processMethod(entry));
  }

}

module.exports = new ExtractWrapper();

