package org.rsk.doc.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.rsk.doc.extractor.dto.MethodAndAnnotation;
import org.rsk.doc.extractor.dto.yaml.input.InputDocsInfo;
import org.rsk.doc.extractor.dto.yaml.output.*;
import org.rsk.doc.extractor.service.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private final ObjectMapper yamlMapper = Constants.getYamlMapper();
    private final ObjectMapper jsonMapper = Constants.getJsonMapper();

    public void startParsing(String path, String yamlPath) throws IOException {
        List<ParseResult<CompilationUnit>> parseResults = fileLoaderService.getParseResults(path);
        Map<String, Map<String, Expression>> annotationDefaultValue = defaultParamService.getAnnotationDefaultValue(parseResults);

        propertiesService.setYamlPath(yamlPath);
        propertiesService.setParseResults(parseResults);
        propertiesService.setAnnotationDefaultValues(annotationDefaultValue);

        Map<String, List<MethodAndAnnotation>> methods = extractAnnotedMethodService.extractAnnotatedMethods(parseResults);

        String docsInfoPath = yamlPath + "/" + DOCS_INFO_FILE_NAME;
        InputDocsInfo docsInfo = yamlMapper.readValue(new File(docsInfoPath), InputDocsInfo.class);

        List<MethodDetails> methodDetails = getMethodDetails(methods);

        ProcessedDoc processedDoc = new ProcessedDoc(
            docsInfo.getTitle(), docsInfo.getDescription(), methodDetails, classProcessorService.getModelStrings());

        String result = jsonMapper.writeValueAsString(processedDoc);
        System.out.println(result);
    }

    private List<MethodDetails> getMethodDetails(Map<String, List<MethodAndAnnotation>> methods) {
        List<MethodDetails> methodDetails = methods.entrySet()
            .stream()
//            .filter(entry -> entry.getKey().equals("net_peerCount"))
            .map(entry -> methodService.extractMethod(entry.getValue()))
            .collect(Collectors.toList());
        return methodDetails;
    }


}
