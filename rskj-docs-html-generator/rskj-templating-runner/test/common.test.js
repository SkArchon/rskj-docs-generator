jest.mock('fs-extra');
jest.mock('ejs');

const common = require('../common');
const fs = require('fs-extra');
const ejs = require('ejs');

const ROOT_PATH = "somepath"; 

beforeEach(() => {  
  common.setRootPathForProcessing(ROOT_PATH);
});

test('set root path ', () => {
  const rootPath = "valuepath"; 
  common.setRootPathForProcessing(rootPath);
  expect(common._rootPath).toBe(rootPath);
});

test('cast string ', () => {
  const stringToBeCasted = "{ ee }"; 
  const result = common.castString(stringToBeCasted);
  
  const expectedResult = "&#123; ee &#125;";
  expect(result).toBe(expectedResult);
});

describe("captialize string", () => {

  test("with empty string", () => {
    const emptyString = ""; 
    const emptyStringResult = common.capitalize(emptyString);
    expect(emptyStringResult).toBe(emptyString);
  });

  test("with string", () => {
    const string = "value"; 
    const stringResult = common.capitalize(string);
    const expectedStringResult = "Value";
    expect(stringResult).toBe(expectedStringResult);
  });
}),


test("title case string", () => {
  const string = "this value 1234 values"; 
  const stringResult = common.titleCase(string);
  const expectedStringResult = "This Value 1234 Values";
  expect(stringResult).toBe(expectedStringResult);
});

test("process title", () => {
  const string = "eth_getBalance"; 
  const stringResult = common.processTitle(string);
  const expectedStringResult = "EthgetBalance";
  expect(stringResult).toBe(expectedStringResult);
});

test('should process entry with title', () => {
  const entry = {
    method: "eth_getBalance",
    value1: 8,
    value2 :18
  };
  const result = common.processEntryWithTitle(entry);
  
  const expectedResult = {
    method: "eth_getBalance",
    value1: 8,
    value2 :18,
    className: "EthgetBalance",
    fileName: "EthgetBalance.vue"
  };
  expect(result).toStrictEqual(expectedResult);
})

test('should clean directories', () => {
  common.clean();

  expect(fs.emptyDirSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/components`);
  expect(fs.emptyDirSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/schema/request`);
  expect(fs.removeSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/App.vue`);
  expect(fs.emptyDirSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/schema/response`);
});

test('should clean directories', () => {
  const schema = { "value": "somevalue" };
  const fileName = "schemafile";
  common.processSchema(schema, fileName, "request");

  expect(fs.writeFileSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/schema/request/schemafile.json`, JSON.stringify(schema));
});


test('process json', () => {
  const json = { value: { newerValue: 'somevalue { }' } };
  const methodName = 'someMethod';

  const result = common.processJson(JSON.stringify(json), methodName);

  const expectedResult = {
    "id": 1,
    "jsonrpc": "2.0",
    "method": methodName,
    "value": {
      "newerValue": "somevalue { }"
    }
  };
  expect(result).toBe(JSON.stringify(expectedResult, null, 4));
})


describe('should process html with file name', () => {
  const processData = { "value": "somevalue" };
  const fileName = "htmlfile";
  const templateName = "template";
  const processedResult = '<html></html>';

  fs.readFileSync.mockReturnValue('abcd');
  ejs.render.mockReturnValue(processedResult);

  test('with default path prefix', () => {
    common.processHtml(templateName, processData, fileName);
    expect(fs.writeFileSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/components/${fileName}.vue`, processedResult);
  });

  test('with no path prefix', () => {
    common.processHtml(templateName, processData, fileName, null);
    expect(fs.writeFileSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/${fileName}.vue`, processedResult);
  });

  test('with custom path prefix', () => {
    const customPath = 'abcd';
    common.processHtml(templateName, processData, fileName, customPath);
    expect(fs.writeFileSync).toHaveBeenCalledWith(`${ROOT_PATH}/src/${customPath}/${fileName}.vue`, processedResult);
  });
  
});

