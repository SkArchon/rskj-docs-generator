#!/usr/bin/env node

'use strict';

const fs = require('fs-extra');
const process = require('process');

const extract = require('./extract');

// Get the path argument
const argPath = process.argv.slice(2)[0];

startProcess(argPath);

function startProcess(inputJsonFilePath) {
  if(!inputJsonFilePath) {
    throw 'There was no input json file path passed';
  }

  const htmlPath = '../rskj-docs';
  extract.clean(htmlPath);

  const rawdata = fs.readFileSync(inputJsonFilePath);
  const documentationJson = JSON.parse(rawdata);

  extract.processAppComponent(documentationJson, htmlPath);

  // This is used as a success code from the parent script
  console.log('HTML-SUCCESS');
}

