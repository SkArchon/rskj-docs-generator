'use strict';


const COMMON_JSON_RPC_VALUES = {
  id: 1,
  jsonrpc: '2.0'
};

const INDENT_LENGTH_JSON_STRINGIFY = 4;
class Common {

  constructor() { }

  processJson(json, skipCasting, method) {
    const parsed = JSON.parse(json);
    const processed = {
      ...COMMON_JSON_RPC_VALUES,
      method: method,
      ...parsed
    };
    const processedJsonString = JSON.stringify(processed, null, INDENT_LENGTH_JSON_STRINGIFY);

    const casted = (!skipCasting)
      ? castString(processedJsonString)
      : processedJsonString;

    return casted;
  }

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
        
}

module.exports = new Common();

