jest.mock('../extractor');
jest.mock('../common');
jest.mock('fs-extra');

const extractor = require('../extractor');
const common = require('../common');
const fs = require('fs-extra');

const extractWrapper = require('../extract-wrapper');

test('start process with invalid value', () => {
  const errorMessage = 'There was no input json file path passed';
  try {
    extractWrapper.startProcess();
  } catch(e) {
    expect(e).toBe(errorMessage);
  }
});

test('should process documentation', () => {
  const model1Key = 'model1Key';
  const model1 = { [model1Key]: { data: 'model1Data' } };
  
  const model2Key = 'model1Key';
  const model2 = { [model2Key]: { data: 'model2Data' } };

  const models = { ...model1, ...model2 };

  const method1 = { data: 'method1Data' };
  const method2 = { data: 'method2Data' };

  const documentationJson = {
    methodDetails: [method1, method2],
    models: models
  };

  const stringifiedJson = JSON.stringify(documentationJson);
  fs.readFileSync.mockReturnValue(stringifiedJson);

  extractWrapper.startProcess(documentationJson);

  expect(common.setRootPathForProcessing).toHaveBeenCalledWith(extractWrapper._HTML_PATH);
  expect(common.clean).toHaveBeenCalledWith();
  
  expect(extractor.processAppComponent).toHaveBeenCalledWith(documentationJson);
  expect(extractor.processModel).toHaveBeenCalledWith(model1Key, models)
  expect(extractor.processModel).toHaveBeenCalledWith(model2Key, models)
  expect(extractor.processMethod).toHaveBeenCalledWith(method1)
  expect(extractor.processMethod).toHaveBeenCalledWith(method2)
});

