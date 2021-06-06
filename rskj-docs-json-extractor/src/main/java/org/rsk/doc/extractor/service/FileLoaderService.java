package org.rsk.doc.extractor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.rsk.doc.extractor.Constants;
import org.rsk.doc.extractor.dto.yaml.input.InputDocsInfo;
import org.rsk.doc.extractor.dto.yaml.input.InputMethodInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.rsk.doc.extractor.Constants.DOCS_INFO_FILE_NAME;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileLoaderService {

    @Getter
    private static final FileLoaderService instance = new FileLoaderService();

    private final PropertiesService propertiesService = PropertiesService.getInstance();
    private final ObjectMapper yamlMapper = Constants.getYamlMapper();
    private final ObjectMapper jsonMapper = Constants.getJsonMapper();

    /**
     * Returns the results of the paths parsed as a ParseResult (from javaparser)
     * @param path Path of the rskj project
     * @return parsed results
     */
    public List<ParseResult<CompilationUnit>> getParseResults(String path) throws IOException {
        Path projectRoot = Paths.get(path);
        SourceRoot sourceRoot = new SourceRoot(projectRoot);
        List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
        return parseResults;
    }

    /**
     * Loads the generic doc info for the documentation (this includes things like documentation title, etc)
     * @param yamlPath the root folder of where the yamls are kept
     * @return DTO containing all documentation overall metadata
     */
    public InputDocsInfo getDocsInfo(String yamlPath) throws IOException {
        String docsInfoPath = yamlPath + "/" + DOCS_INFO_FILE_NAME;
        return yamlMapper.readValue(new File(docsInfoPath), InputDocsInfo.class);
    }

    /**
     * A path mapping is a construct for this repository where a description should be like
     * description.yml/request/abcd where the last part is used to extract the appropriate value from the
     * description.yml file
     * @param path path
     * @return retrieved value
     */
    public String getValueFromPath(String path) {
        String[] splitPath = path.split("/");

        if(splitPath.length <= 1) {
            throw new RuntimeException("The path should have a path mapping to the yaml value");
        }

        String fileName = splitPath[0];
        InputMethodInfo methodInfo = getMethodInfoFromFileName(fileName);

        String methodName = splitPath[1];
        switch(methodName){
            // Get either the response or request description
            case "description":
                String descriptionKey = splitPath[2];
                String key = splitPath[3];

                InputMethodInfo.MethodDescription description = methodInfo.getDescription();
                return (descriptionKey.equals("response"))
                    ? description.getResponse().get(key)
                    : description.getRequest().get(key);
            case "request":
                String requestKey = splitPath[2];
                return methodInfo.getRequest().get(requestKey);
            case "response":
                String responseKey = splitPath[2];
                return methodInfo.getResponse().get(responseKey);
            default:
                throw new RuntimeException("unrecognized value " + methodName);
        }
    }

    private InputMethodInfo getMethodInfoFromFileName(String fileName) {
        try {
            InputMethodInfo methodInfo = yamlMapper.readValue(
                new File(propertiesService.getYamlPath() + "/" + fileName), InputMethodInfo.class);
            return methodInfo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode getJsonSchemaIfExists(String methodName, String prefixFolder) {
        String prefixPath = propertiesService.getYamlPath() + "/schema/";
        String pathWithPrefixedFolder = (StringUtils.isEmpty(prefixFolder))
            ? prefixPath
            : prefixPath + prefixFolder + "/";

        File schemaFile = new File(pathWithPrefixedFolder + methodName + ".json");
        // When there is no json schema, skip processing
        if(!schemaFile.exists()) {
            return null;
        }

        try {
            return jsonMapper.readValue(schemaFile, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
