package org.rsk.doc.extractor.service;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesService {

    @Getter
    @Setter
    private String yamlPath;

    // Store all the parse results
    @Getter
    @Setter
    private List<ParseResult<CompilationUnit>> parseResults;

    // Store default values of the annotations
    @Getter
    @Setter
    private Map<String, Map<String, Expression>> annotationDefaultValues;

    @Getter
    private static final PropertiesService instance = new PropertiesService();

}
