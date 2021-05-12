package org.rsk.doc.extractor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.rsk.doc.extractor.Constants;
import org.rsk.doc.extractor.dto.yaml.input.InputMethodInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileLoaderService {

    @Getter
    private static final FileLoaderService instance = new FileLoaderService();

    private final PropertiesService propertiesService = PropertiesService.getInstance();
    private final ObjectMapper yamlMapper = Constants.getYamlMapper();

    public List<ParseResult<CompilationUnit>> getParseResults(String path) throws IOException {
        Path projectRoot = Paths.get(path);
        SourceRoot sourceRoot = new SourceRoot(projectRoot);
        List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
        return parseResults;
    }

    public String getValueFromPath(String path) {
        String[] splitPath = path.split("/");

        if(splitPath.length <= 1) {
            throw new RuntimeException("The path should have a path mapping to the yaml value");
        }

        String fileName = splitPath[0];
        InputMethodInfo methodInfo = getMethodInfoFromFileName(fileName);

        String methodName = splitPath[1];
        switch(methodName){
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
            InputMethodInfo methodInfo = yamlMapper.readValue(new File(propertiesService.getYamlPath() + "/" + fileName), InputMethodInfo.class);
            return methodInfo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
