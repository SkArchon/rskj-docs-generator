#!/usr/bin/env node

'use strict';

const fs = require('fs-extra');
const util = require('util');
const exec = util.promisify(require('child_process').exec);
const process = require('process');

const parentPath = process.argv.slice(2)[0];

const intermediateRecompile = process.argv.slice(2)[1];
const recompileGradleValue = (typeof intermediateRecompile === 'string')
    ? intermediateRecompile.toLowerCase() === 'yes' || intermediateRecompile.toLowerCase() === 'y'
    : false;

const yamlPath = `${parentPath}/main/resources/rpc-docs`

runProcess(recompileGradleValue);

async function runProcess(recompileGradle) {
    try {        
        const currentDirectory = process.cwd();
        const currentBuildDirectory = `${currentDirectory}/build`;
        const currentBuildDirectoryDist = `${currentBuildDirectory}/dist`;

        const htmlGeneratorPath = `${currentDirectory}/rskj-docs-html-generator/rskj-templating-runner`;
        const htmlProjectPath = `${currentDirectory}/rskj-docs-html-generator/rskj-docs`
        
        fs.emptyDirSync(currentBuildDirectory);

        const extractedJson = `${currentBuildDirectory}/input.json`;

        await buildGradle(recompileGradle);

        const jsonResult = await extractJson();
        fs.writeFileSync(extractedJson, jsonResult);

        process.chdir(htmlGeneratorPath);
        const result = await generateHtml(extractedJson);

        if(!result.includes('HTML-SUCCESS')) {
            console.log(`Unexpected result ${result}`);
            throw 'We got an unexpected result, was expecting \'HTML-SUCCESS\'';
        }

        process.chdir(htmlProjectPath);
        await compileHtml();

        const distLocation = `${htmlProjectPath}/dist`;
        fs.existsSync(currentBuildDirectoryDist);

        fs.copySync(distLocation, currentBuildDirectoryDist);

        console.log('Build success');
    }
    catch(e) {
        throw e;
    }
}

async function compileHtml() {
    try {
        console.log('Starting html compiling process');

        const command = `npm run build`;
        const { stdout, stderr } = await exec(command);

        if(!stdout.includes('Build complete.')) {
            throw stderr;
        }

        console.log('Completed html compiling process');
    }
    catch(e) {
        throw e;
    }
}

async function generateHtml(inputJson) {
    try {
        console.log('Starting html generating process');

        const command = `node index.js ${inputJson}`;
        const { stdout, stderr } = await exec(command);

        if(stderr) {
            throw stderr;
        }

        console.log('Completed html generating process');
        return stdout;
    }
    catch(e) {
        throw e;
    }
}

async function extractJson() {
    if (fs.existsSync('./rskj-docs-json-extractor/build/libs/javaparser-all-1.0-SNAPSHOT.jar')) {
        try {
            console.log('Starting json extraction process');

            const command = `java -cp ./rskj-docs-json-extractor/build/libs/javaparser-all-1.0-SNAPSHOT.jar org.rsk.doc.extractor.StartExtractor ${parentPath} ${yamlPath}`;
            const { stdout, stderr } = await exec(command);

            if(stderr) {
                throw stderr;
            }

            const processedJson = JSON.parse(stdout);
            const prettifiedJson = JSON.stringify(processedJson, null, 4);
            console.log('Completed json extraction process');
            return prettifiedJson;
        }
        catch(e) {
            throw e;
        }
    }
}

async function buildGradle(recompileGradle) {
    const jarPath = './rskj-docs-json-extractor/build/libs/javaparser-all-1.0-SNAPSHOT.jar';
    const jarCompiled = fs.existsSync(jarPath);

    if (!jarCompiled || recompileGradle) {
        try {
            console.log('Starting gradle build process');

            const buildCommand = ` ./rskj-docs-json-extractor/gradlew -p ./rskj-docs-json-extractor/ clean build fatJar`;

            const { stdout, stderr } = await exec(buildCommand);
            if(stderr) {
                console.log('There were errors by exec, still trying build');
            }
            console.log('Completed gradle build process');
            return stderr;
        }
        catch(e) {
            throw e;
        }
    }
}

