package org.rsk.doc.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.rsk.doc.extractor.dto.MethodAndAnnotation;
import org.rsk.doc.extractor.dto.yaml.input.InputDocsInfo;
import org.rsk.doc.extractor.dto.yaml.output.*;
import org.rsk.doc.extractor.service.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.rsk.doc.extractor.Constants.DOCS_INFO_FILE_NAME;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtractorInstructions {

    @Getter
    private static final ExtractorInstructions instance = new ExtractorInstructions();

    private final DefaultParamService defaultParamService = DefaultParamService.getInstance();
    private final ExtractAnnotedMethodService extractAnnotedMethodService = ExtractAnnotedMethodService.getInstance();
    private final PropertiesService propertiesService = PropertiesService.getInstance();
    private final FileLoaderService fileLoaderService = FileLoaderService.getInstance();
    private final ClassProcessorService classProcessorService = ClassProcessorService.getInstance();
    private final MethodService methodService = MethodService.getInstance();

    private final ObjectMapper jsonMapper = Constants.getJsonMapper();

    public String startParsing(String path, String yamlPath) throws IOException {
        List<ParseResult<CompilationUnit>> parseResults = fileLoaderService.getParseResults(path);
        InputDocsInfo docsInfo = fileLoaderService.getDocsInfo(yamlPath);

        Map<String, Map<String, Expression>> annotationDefaultValue = defaultParamService.getAnnotationDefaultValue(parseResults);

        propertiesService.setYamlPath(yamlPath);
        propertiesService.setParseResults(parseResults);
        propertiesService.setAnnotationDefaultValues(annotationDefaultValue);

        Map<String, List<MethodAndAnnotation>> methods = extractAnnotedMethodService.extractAnnotatedMethods(parseResults);

        List<MethodDetails> methodDetails = getMethodDetails(methods);

        ProcessedDoc processedDoc = new ProcessedDoc(
            docsInfo.getTitle(), docsInfo.getDescription(), methodDetails, classProcessorService.getModelStrings());

        return jsonMapper.writeValueAsString(processedDoc);
    }

    /**
     * Loop through all the methods and get method details for each method
     * @param methods method list
     * @return processed methods
     */
    private List<MethodDetails> getMethodDetails(Map<String, List<MethodAndAnnotation>> methods) {
        List<MethodDetails> methodDetails = methods.entrySet()
            .stream()
            .map(entry -> methodService.extractMethod(entry.getValue()))
            .collect(Collectors.toList());
        return methodDetails;
    }


}
