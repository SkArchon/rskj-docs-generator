# RSK Document Generator

## How To Run
Firstly make sure you have checked out the rskj project.

Afterwards run the following from the root directory

```bash
# make sure node modules are installed in the parent directory
npm install

# after the root node modules are installed, use the following command to add node modules to any of the modules that require it
npm run install-subdirectories

# run the following command to generate the documentation (Ensure that there is no "/" at the end of the url)
npm run generate --path=/pasth/to/rskj/project/rskj/rskj/rskj-core/src

cd /your/clone/path/build/dist

python -m SimpleHTTPServer
```

The genrated json file (intermediate step) as well as the final html output would be present in the roots build folder
* intermediate json - `input.json`
* compiled html - `dist` folder

**How to view the app without a server**

By default the index.html will not work without a development server, this is due to the way the css and javascript imports have been set in the compiled vue file. Simply replace `=/some.cssorjs` with `=some.cssorjs`


## Structure
The project is separated into three separate parts

### rsk-docs-json-extractor
This module is written in Java, which uses the "javaparser" Library to parse non compiled `.java` source code. It will generate an Abstract Syntax Tree (AST) which is what will be traversed and processed.


1. YAML Files
The library supports (and in some cases require) using yaml files to define descriptions and request/response examples. The descriptions are also processed and considered as markdown (so using markdown can get it formatted on the final html output). The yaml files can be found in the `resources/rpc-docs` folder in the rskj repository.

2. Annotations
The module will look for the `@JsonRpcDoc` annotations. And will process the resulting methods that were annotated. The annotations are currently directly implemented in the Rskj repository. The dependency sharing is not required since this is done via traversing the source.

    * **Why Expressinve Annotations?**
    The documentation annotations are expressive (in that they take up multiple lines in order to document the method). This was designed on purpose, as opposed to having the bulk of the detail in the yaml files. This is was so that all of the details whenever making a change would be in the developers vision, thus prompting the developer to make more active changes to the documentation. Having the details in bulk in a YML would result in the documentation being "out of sight, out of mind", which would result in potentially higher chances of documentation not being updated when any RPC methods change.

3. Processed Output
Currently the json is outputted directly by the module to stdout, this is as the script that calls this module takes the value from stdout. However if required
a fileoutput (without a third party script) can be added.

### rsk-docs-html-generator/rsk-templating-runner
This is a node js script that takes an input of a `input.json`. Which in most cases would be the generated output from the `rsk-docs-json-extractor` module.
This then uses templating to dynamically generate static html files in the "rskj-docs" folder.

### rsk-docs-html-generator/rskj-docs
This is a Vuejs project which contains all of the html and css for our documentation. It contains `ejs` Templates, which are processed by the `rsk-templating-runner`. And its output is processed to the appropriate `src` directories. After the templates have been compiled at least once the project can be built for production using `npm run build`.

**Static vs Dynamic Html**

This project uses vuejs and requires javascript to function, however all of the individual RPC doc sections in the project have all the details injected into them in a static manner (not rendered dynamically), even though javascript is required to load the individual RPC doc vue components into the file.

As seen in the below image, which is a vue file, all of the responses are documented **statically** in the HTML.
![Static Html](https://github.com/SkArchon/rskj-docs-example/blob/eb03f60075cddc7c24b72d25ee39095f4dc9f945/HtmlUpdated.png)

The complete file generated for `eth_getBalance` can be viewed at https://github.com/SkArchon/rskj-docs-example/blob/eb03f60075cddc7c24b72d25ee39095f4dc9f945/EthgetBalance.vue

However the read and write operation section of the app is completely written dynamically.


# Improvements

1. Add unit tests for all modules, and integration tests for `rsk-docs-html-generator/rsk-templating-runner` and `rsk-docs-html-generator/rskj-docs`
2. Make the parent node script more robust (for example, currently it requires "/" being ignored at the end of the file path)
3. Add annotation to ignore fields in classes
4. Currently `"` that is escaped within Strings in Java are not parsed correctly and the `\` will be visible in the final output. As an alternative `'` is recommended to be used. This is an area of improvement .
5. Add typescript to the node scripts (thus adding type safety).
6. Add the ability to provide an option to the `rsk-docs-json-extractor` module to save the processed JSON output directly to a file.
7. Skip using custom singletons in the `rsk-docs-json-extractor` to emulate an *Autowired like* codebase and setup actual autowiring for the codebase.




