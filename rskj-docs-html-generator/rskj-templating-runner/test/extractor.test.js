jest.mock('marked');
jest.mock('highlight.js');
jest.mock('../common');

const marked = require("marked");
const hljs = require('highlight.js');
const common = require('../common');
const extractor = require('../extractor');
const commonConstants = require("./common.constants");

afterEach(() => {
  jest.clearAllMocks();
});

test('should process model', () => {
  const modelKey = '';
  const models = { [modelKey]: {} };
  const modelHtml = '<html></html>';
  hljs.highlight.mockReturnValue({ value: 'value' });
  common.castString.mockReturnValue(modelHtml);

  extractor.processModel(modelKey, models);

  const processedData = {
    modelName: modelKey,
    modelHtml: modelHtml,
    className: `${modelKey}Model`,
    fileName: `${modelKey}Model.vue`,
  };
  expect(common.processHtml).toHaveBeenCalledWith('modelEntry', { model: processedData }, processedData.className);
});

test('should process app component', () => {
  common.processEntryWithTitle.mockReturnValue('MethodValue');
  
  const title = 'Title';
  const description = 'Description';

  const modelKey = 'modelSome';

  const documentationJson = {
    methodDetails: [{ methodType: 'WRITE' }, { methodType: 'READ' }],
    models: { [modelKey]: {} },
    title: title,
    description: description
  }

  extractor.processAppComponent(documentationJson);

  const processedData = {
    "title": title,
    "description": description,
    "readMethods": [
      "MethodValue"
    ],
    "writeMethods": [
      "MethodValue"
    ],
    "models": [
      {
        "modelKey": modelKey,
        "className": `${modelKey}Model`,
        "fileName": `${modelKey}Model.vue`
      }
    ]
  };
  expect(common.processHtml).toHaveBeenCalledWith('appComponent', processedData, 'App', null);
});

describe('should process method', () => {

  test('with request examples response details', () => {
    const className = 'methodName';
    const markedHtml = '<div></div>';
    common.processTitle.mockReturnValue(className);
    marked.mockReturnValue(markedHtml);
    hljs.highlight.mockReturnValue({ value: 'value' });

    extractor.processMethod(commonConstants.METHOD_INPUT);
    
    expect(common.processHtml).toHaveBeenCalledWith('methodEntry', commonConstants.METHOD_OUTPUT_PROCESSED_DATA, className);
  })

  test('without request examples', () => {
    const className = 'methodName';
    const markedHtml = '<div></div>';
    common.processTitle.mockReturnValue(className);
    marked.mockReturnValue(markedHtml);
    hljs.highlight.mockReturnValue({ value: 'value' });

    extractor.processMethod(commonConstants.METHOD_INPUT_WITHOUT_REQUEST_EXAMPLES);
    
    expect(common.processHtml).toHaveBeenCalledWith('methodEntry', commonConstants.METHOD_OUTPUT_WITHOUT_REQUEST_EXAMPLES, className);
  })

  

  test('without response details', () => {
    const className = 'methodName';
    const markedHtml = '<div></div>';
    common.processTitle.mockReturnValue(className);
    marked.mockReturnValue(markedHtml);
    hljs.highlight.mockReturnValue({ value: 'value' });

    extractor.processMethod(commonConstants.METHOD_INPUT_WITHOUT_RESPONSE_DETAILS);
    
    expect(common.processHtml).toHaveBeenCalledWith('methodEntry', commonConstants.METHOD_OUTPUT_WITHOUT_RESPONSE_DETAILS, className);
  })

  test('with request schema', () => {
    const className = 'methodName';
    const markedHtml = '<div></div>';
    common.processTitle.mockReturnValue(className);
    marked.mockReturnValue(markedHtml);
    hljs.highlight.mockReturnValue({ value: 'value' });

    extractor.processMethod(commonConstants.METHOD_INPUT);
    
    expect(common.processSchema).toHaveBeenCalledWith(commonConstants.METHOD_OUTPUT_REQUEST_SCHEMA, className, 'request');
  })

  test('with response schema', () => {
    const className = 'methodName';
    const markedHtml = '<div></div>';
    common.processTitle.mockReturnValue(className);
    marked.mockReturnValue(markedHtml);
    hljs.highlight.mockReturnValue({ value: 'value' });

    extractor.processMethod(commonConstants.METHOD_INPUT);
    
    expect(common.processSchema).toHaveBeenCalledWith(commonConstants.METHOD_OUTPUT_RESPONSE_SCHEMA, className, 'response');
  })

  test('without request schema', () => {
    const className = 'methodName';
    const markedHtml = '<div></div>';
    common.processTitle.mockReturnValue(className);
    marked.mockReturnValue(markedHtml);
    hljs.highlight.mockReturnValue({ value: 'value' });

    extractor.processMethod(commonConstants.METHOD_INPUT_WITHOUT_SCHEMA);
    
    expect(common.processSchema).not.toHaveBeenCalledWith(expect.anything(), expect.anything(), 'request');
  })

  test('without response schema', () => {
    const className = 'methodName';
    const markedHtml = '<div></div>';
    common.processTitle.mockReturnValue(className);
    marked.mockReturnValue(markedHtml);
    hljs.highlight.mockReturnValue({ value: 'value' });

    extractor.processMethod(commonConstants.METHOD_INPUT_WITHOUT_SCHEMA);
    
    expect(common.processSchema).not.toHaveBeenCalledWith(expect.anything(), expect.anything(), 'response');
  })

  
  
})
