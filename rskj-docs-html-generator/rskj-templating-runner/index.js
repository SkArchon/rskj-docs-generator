#!/usr/bin/env node

'use strict';

const process = require('process');
const extractWrapper = require('./extract-wrapper');

// Get the path argument
const argPath = process.env.npm_config_inputJson;

extractWrapper.startProcess(argPath);

console.log('HTML-SUCCESS');


