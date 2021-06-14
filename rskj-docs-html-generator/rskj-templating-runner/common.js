'use strict';

const fs = require('fs-extra')
const ejs = require('ejs');

const COMMON_JSON_RPC_VALUES = { id: 1, jsonrpc: '2.0' };
const INDENT_LENGTH_JSON_STRINGIFY = 4;

class Common {

  _rootPath = null;

  constructor() { }

  _readFile(path) {
    const processedPath = `${this._rootPath}/${path}`;
    return fs.readFileSync(processedPath).toString();
  }

  _writeFile(path, data) {
    const processedPath = `${this._rootPath}/${path}`;
    fs.writeFileSync(processedPath, data);
  }
  
  setRootPathForProcessing(path) {
    this._rootPath = path;
  }

  // Create a json rpc request for printing as code by merging generic values
  // along with any custom values
  processJson(json, method) {
    const parsed = JSON.parse(json);
    const processed = {
      ...COMMON_JSON_RPC_VALUES,
      method: method,
      ...parsed
    };
    const processedJsonString = JSON.stringify(processed, null, INDENT_LENGTH_JSON_STRINGIFY);

    return processedJsonString;
  }

  // Replace any curly braces with a hashed value
  // as it does not get replaced automatically
  castString(stringValue) {
    return stringValue
      .replace(/{/g, '&#123;')
      .replace(/}/g, '&#125;');
  }
      
  capitalize(str) {
    if (str.length == 0) return str;
    return str[0].toUpperCase() + str.substr(1);
  }

  titleCase(str) {
    return str.split(' ').map(this.capitalize).join(' ');
  }

  processTitle(str) {
    return this.titleCase(str.replace(/[^0-9a-zA-Z]/g, ''));
  }

  processEntryWithTitle(entry) {
    const title = this.processTitle(entry.method);
    return {
      ...entry,
      className: title,
      fileName: `${title}.vue`
    }
  }

  // Clear any existing build outputs
  clean() {
    fs.emptyDirSync(`${this._rootPath}/src/components`);
    fs.emptyDirSync(`${this._rootPath}/src/schema/request`);
    fs.emptyDirSync(`${this._rootPath}/src/schema/response`);
    fs.removeSync(`${this._rootPath}/src/App.vue`);
  }

  processHtml(templateName, processData, fileName, componentPath = 'components') {
    const processedComponentPath = (componentPath)
      ? `src/${componentPath}`
      : `src`

    const template = this._readFile(`templates/${templateName}.ejs`);
    const html = ejs.render(template, processData);
    this._writeFile(`${processedComponentPath}/${fileName}.vue`, html);
  }

  processSchema(schema, fileName, prefix) {
    const jsonSchema = JSON.stringify(schema);
    const filePath = `src/schema/${prefix}/${fileName}.json`;
    this._writeFile(filePath, jsonSchema);
  }

}

module.exports = new Common();

