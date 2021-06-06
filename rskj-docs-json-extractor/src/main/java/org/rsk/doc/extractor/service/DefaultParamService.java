package org.rsk.doc.extractor.service;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultParamService {

    @Getter
    private static final DefaultParamService instance = new DefaultParamService();

    private static final String[] annotations = { "JsonRpcDoc", "JsonRpcDocRequestParameter", "JsonRpcDocResponse", "JsonRpcDocModelType" };

    /**
     * Since javaparser only reads the java code, it cannot actually infer any annotation default values
     * from whenever we read an annotation that has optional params, and can only extract actual values the
     * developer has specified.
     *
     * Therefore we extract all default values and substitue them in when a value hasnt been specified by the dev
     *
     * @param parseResults parse results
     * @return default params for the list of annotations we process
     */
    public Map<String, Map<String, Expression>> getAnnotationDefaultValue(List<ParseResult<CompilationUnit>> parseResults) {
        return Arrays.stream(annotations)
            .map(annotationName -> {
                Map<String, Expression> params =
                    getDefaultParametersForJsonRpcDocAnnotation(parseResults, annotationName);
                return new AbstractMap.SimpleEntry<>(annotationName, params);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Expression> getDefaultParametersForJsonRpcDocAnnotation(List<ParseResult<CompilationUnit>> parseResults,
                                                                               String annotationName) {
        Optional<AnnotationDeclaration> jsonRpcAnnotationDeclaration = getAnnotationDeclaration(parseResults, annotationName);

        if(!jsonRpcAnnotationDeclaration.isPresent()) {
            throw new RuntimeException("Unable to detect required documentation annotation");
        }

        // Create the default pairs that we will use
        return jsonRpcAnnotationDeclaration.get()
            .getMembers()
            .stream()
            .map(bodyDeclaration -> (AnnotationMemberDeclaration) bodyDeclaration)
            .filter(annotationMemberDeclaration -> annotationMemberDeclaration.getDefaultValue().isPresent())
            .collect(Collectors.toMap(
                annotationMemberDeclaration -> annotationMemberDeclaration.getNameAsString(),
                annotationMemberDeclaration -> annotationMemberDeclaration.getDefaultValue().get()));
    }

    private Optional<AnnotationDeclaration> getAnnotationDeclaration(List<ParseResult<CompilationUnit>> parseResults, String annotationName) {
        GenericVisitorAdapter<AnnotationDeclaration, Void> collector = new GenericVisitorAdapter<AnnotationDeclaration, Void>() {
            @Override
            public AnnotationDeclaration visit(final AnnotationDeclaration n, final Void arg) {
                if(n.getNameAsString().equals(annotationName)) {
                    return n;
                }
                return super.visit(n, arg);
            }
        };

        return parseResults.stream()
            .map(compilationUnitParseResult -> compilationUnitParseResult.getResult())
            .filter(compilationUnit -> compilationUnit.isPresent())
            .map(compilationUnit -> collector.visit(compilationUnit.get(), null))
            .filter(Objects::nonNull)
            .findFirst();
    }

    /**
     * Since default params can only be inferred at runtime and javaparser only parses the source. We need to merge
     * any annotation values specified by the dev along with the default values of the annotation
     * @param annotationDefaultValue Default values of the annotation
     * @param annotationExpr annotation you need to merge
     * @return merged result of default annotation params and actual annotation params
     */
    public Map<String, Expression> getProcessedPairValues(Map<String, Map<String, Expression>> annotationDefaultValue,
                                                          AnnotationExpr annotationExpr) {
        NodeList<MemberValuePair> pairs = (annotationExpr instanceof NormalAnnotationExpr)
            ? annotationExpr.toNormalAnnotationExpr().get().getPairs()
            : new NodeList<>();

        // We take a copy so we dont need to modify the original instance
        Map<String, Expression> convertedPairs = new HashMap<>();
        convertedPairs.putAll(convertPairsToMap(pairs));

        Map<String, Expression> defaultParams = annotationDefaultValue.get(annotationExpr.getNameAsString());
        defaultParams.forEach(
            (key, value) -> convertedPairs.merge(key, value, (v1, v2) -> v1)
        );

        return convertedPairs;
    }

    public Map<String, Expression> convertPairsToMap(NodeList<MemberValuePair> pairs) {
        return pairs.stream()
            .collect(Collectors.toMap(e -> e.getName().getIdentifier(), MemberValuePair::getValue));
    }

}
