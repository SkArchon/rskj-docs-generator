jest.mock('marked');
jest.mock('highlight.js');
jest.mock('../common');

const marked = require("marked");
const hljs = require('highlight.js');
const common = require('../common');
const extractor = require('../extractor');

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