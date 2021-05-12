package org.rsk.doc.extractor.service;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Stream;

import static org.rsk.doc.extractor.service.CommonUtil.processBinaryExprString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassProcessorService {

    @Getter
    private static final ClassProcessorService instance = new ClassProcessorService();

    private final ClassWithFilterVisitor classWithFilterVisitor = new ClassWithFilterVisitor();
    private final PropertiesService propertiesService = PropertiesService.getInstance();
    private final DefaultParamService defaultParamService = DefaultParamService.getInstance();

    private Map<String, String> modelStrings = new HashMap<>();
    private Set<String> processedClasses = new HashSet<>();
    private Set<String> undetectedClasses = new HashSet<>();

    public void processClassAsType(String searchClassName) {
        if(!processType(searchClassName)) {
            return;
        }
        Optional<ClassOrInterfaceDeclaration> searchClassOptional =
            searchForClass(propertiesService.getParseResults(), searchClassName);
        if(searchClassOptional.isPresent()) {
            convertClassToModel(searchClassOptional.get());
        } else {
            undetectedClasses.add(searchClassName);
        }
    }

    private Optional<ClassOrInterfaceDeclaration> searchForClass(
            List<ParseResult<CompilationUnit>> parseResults, String searchClassName) {
        Optional<ClassOrInterfaceDeclaration> searchClassOptional = parseResults.stream()
            .map(parseResult -> parseResult.getResult())
            .filter(result -> result.isPresent())
            .flatMap(result -> {
                ClassCollectorStateWrapper classCollectorStateWrapper =
                    new ClassCollectorStateWrapper(searchClassName);
                classWithFilterVisitor.visit(result.get(), classCollectorStateWrapper);
                return classCollectorStateWrapper.getCollector().stream();
            })
            .findFirst();
        return searchClassOptional;
    }

    private void convertClassToModel(ClassOrInterfaceDeclaration searchClass) {
        StringBuilder stringBuilder = new StringBuilder();

        String className = searchClass.getName().getIdentifier();
        String interfaceTitle = "interface " + className + " {\n";
        stringBuilder.append(interfaceTitle);

        searchClass.getMembers()
            .stream()
            .flatMap(bodyDeclaration ->
                (bodyDeclaration.isFieldDeclaration())
                    ? ((FieldDeclaration) bodyDeclaration).getVariables().stream()
                    : Stream.empty()
            )
            .forEach(variableDeclarator -> {
                String variableName = variableDeclarator.getName().getIdentifier();
                String customDocumentationType = processListedClassesAndReturnDocumentationType(variableDeclarator);
                String documentationType = StringUtils.isEmpty(customDocumentationType)
                    ? variableDeclarator.getType().asString()
                    : customDocumentationType;

                String processedLine = "  " + variableName + ": " + documentationType + ";\n";

                processClassAsType(variableDeclarator.getType().asString());
                stringBuilder.append(processedLine);
            });

        stringBuilder.append("}");
        modelStrings.put(className, stringBuilder.toString());
        processedClasses.add(className);
    }

    private String processListedClassesAndReturnDocumentationType(VariableDeclarator variableDeclarator) {
        FieldDeclaration fieldDeclaration = (FieldDeclaration) variableDeclarator.getParentNode().get();

        Optional<AnnotationExpr> jsonRpcDocModelType = fieldDeclaration.getAnnotations()
            .stream()
            .filter(annotationExpr -> annotationExpr.getNameAsString().equals("JsonRpcDocModelType"))
            .findFirst();

        if(jsonRpcDocModelType.isPresent()) {
            Map<String, Expression> processedPairValues = defaultParamService.getProcessedPairValues(
                propertiesService.getAnnotationDefaultValues(),
                jsonRpcDocModelType.get());

            String setDocumentationType = processBinaryExprString(processedPairValues.get("documentationType"));

            processedPairValues.get("processClassNames")
                .asArrayInitializerExpr()
                .getValues()
                .forEach(expression -> processClassAsType(processBinaryExprString(expression)));

            if(!StringUtils.isBlank(setDocumentationType)) {
                return setDocumentationType;
            }
        }
        return null;
    }

    private Boolean processType(String type){
        return !processedClasses.contains(type) && !undetectedClasses.contains(type);
    }

    public Map<String, String> getModelStrings() {
        return modelStrings;
    }

    @Getter
    @Setter
    private static class ClassCollectorStateWrapper {
        private final String className;
        private final List<ClassOrInterfaceDeclaration> collector;

        public ClassCollectorStateWrapper(String className) {
            this.className = className;
            this.collector = new ArrayList<>();
        }
    }

    private class ClassWithFilterVisitor extends VoidVisitorAdapter<ClassCollectorStateWrapper> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, ClassCollectorStateWrapper wrapper) {
            super.visit(n, wrapper);

            if(wrapper.getClassName().equals(n.getNameAsString())) {
                wrapper.getCollector().add(n);
            }
        }
    }
}
