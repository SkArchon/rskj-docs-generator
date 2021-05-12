package org.rsk.doc.extractor.service;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.rsk.doc.extractor.dto.MethodAndAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtractAnnotedMethodService {

    @Getter
    private static final ExtractAnnotedMethodService instance = new ExtractAnnotedMethodService();

    private final JsonRpcDocMethodVisitor jsonRpcDocMethodVisitor = new JsonRpcDocMethodVisitor();

    public Map<String, List<MethodAndAnnotation>> extractAnnotatedMethods(List<ParseResult<CompilationUnit>> parseResults) {
        List<MethodAndAnnotation> methods = parseResults.stream()
            .map(parseResult -> parseResult.getResult())
            .filter(result -> result.isPresent())
            .flatMap(result -> {
                // TODO : Replace with generic adapter
                List<MethodAndAnnotation> visit = new ArrayList<>();
                jsonRpcDocMethodVisitor.visit(result.get(), visit);
                return visit.stream();
            })
            .collect(Collectors.toList());

        // We group methods for method overloads
        return methods.stream()
            .collect(Collectors.groupingBy(methodDeclaration -> methodDeclaration.getMethodDeclaration().getName().getIdentifier()));
    }



    private class JsonRpcDocMethodVisitor extends VoidVisitorAdapter<List<MethodAndAnnotation>> {
        @Override
        public void visit(MethodDeclaration methodDeclaration, List<MethodAndAnnotation> methods) {
            Optional<AnnotationExpr> jsonRpcDoc = methodDeclaration.getAnnotations()
                .stream()
                .filter(annotationExpr ->
                    annotationExpr.getName().getIdentifier().equals("JsonRpcDoc"))
                .findFirst();

            if(jsonRpcDoc.isPresent()) {
                methods.add(new MethodAndAnnotation(methodDeclaration, jsonRpcDoc.get()));
            }

            super.visit(methodDeclaration, methods);
        }
    }
}
