package org.rsk.doc.extractor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.rsk.doc.extractor.Constants;
import org.rsk.doc.extractor.dto.MethodAndAnnotation;
import org.rsk.doc.extractor.dto.yaml.output.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.rsk.doc.extractor.service.CommonUtil.processBinaryExprString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodService {

    @Getter
    private static final MethodService instance = new MethodService();

    private final DefaultParamService defaultParamService = DefaultParamService.getInstance();
    private final PropertiesService propertiesService = PropertiesService.getInstance();
    private final FileLoaderService fileLoaderService = FileLoaderService.getInstance();
    private final ClassProcessorService classProcessorService = ClassProcessorService.getInstance();

    /**
     * In case of method overloads make sure to extract the primary method which contains most of the documentation.
     * Return default in case there are no overloads.
     * @param methodAndAnnotationList method annotations
     * @return primary entry
     */
    private MethodAndAnnotation getPrimary(List<MethodAndAnnotation> methodAndAnnotationList) {
        if(methodAndAnnotationList.size() > 1) {
            Map<String, Map<String, Expression>> annotationDefaultValues = propertiesService.getAnnotationDefaultValues();
            for (MethodAndAnnotation methodAndAnnotation : methodAndAnnotationList) {
                AnnotationExpr annotationExpr = methodAndAnnotation.getAnnotationExpr();
                Map<String, Expression> processedPairValues = defaultParamService.getProcessedPairValues(annotationDefaultValues, annotationExpr);

                Boolean isPrimary = processedPairValues.get("primaryForOverloads").asBooleanLiteralExpr().getValue();
                if (isPrimary) {
                    return methodAndAnnotation;
                }
            }
        }
        return methodAndAnnotationList.get(0);
    }

    /**
     * This method is reponsible for extracting each individual method in the json
     * @param methodAndAnnotationList all methods present
     * @return a processed json containing all the details pertaining to this method
     */
    public MethodDetails extractMethod(List<MethodAndAnnotation> methodAndAnnotationList) {
        Map<String, Map<String, Expression>> annotationDefaultValues = propertiesService.getAnnotationDefaultValues();

        MethodAndAnnotation methodAndAnnotation = getPrimary(methodAndAnnotationList);
        MethodDeclaration methodDeclaration = methodAndAnnotation.getMethodDeclaration();

        String methodName = methodDeclaration.getNameAsString();

        Map<String, Expression> processedPairValues = defaultParamService.getProcessedPairValues(
            annotationDefaultValues, methodAndAnnotation.getAnnotationExpr());

        String description = processBinaryExprString(processedPairValues.get("description"));

        // In case the user has not set a summary we substitute the description in
        String initialSummary = processBinaryExprString(processedPairValues.get("summary"));
        String summary = (StringUtils.isEmpty(initialSummary))
            ? description
            : initialSummary;

        boolean isWriteMethod = processedPairValues.get("isWriteMethod").asBooleanLiteralExpr().getValue();
        MethodType methodType = isWriteMethod
            ? MethodType.WRITE
            : MethodType.READ;

        Expression requestExampleExpression = processedPairValues.get("requestExamples");

        // The javaparser library detects the type based on what is present
        // in the java file and not what is present in
        // the actual type therefore in case a user omits the
        // curly braces we do this
        List<Expression> requestExampleExpressionsList = (requestExampleExpression.isArrayInitializerExpr())
            ? requestExampleExpression.asArrayInitializerExpr().getValues()
            : Arrays.asList(requestExampleExpression);

        RequestDetails requestDetails = getRequestDetails(methodAndAnnotationList, methodName, requestExampleExpressionsList);

        NodeList<Expression> responseArray = processedPairValues.get("responses").asArrayInitializerExpr().getValues();
        List<ResponseDetails> responseDetails = getResponseDetails(responseArray, methodAndAnnotation);

        JsonNode responseJsonSchemaDetails = fileLoaderService.getJsonSchemaIfExists(methodName, "response");

        return new MethodDetails(methodName, description, summary, methodType, requestDetails, responseDetails, responseJsonSchemaDetails);
    }

    private RequestDetails getRequestDetails(List<MethodAndAnnotation> methodAndAnnotationList, String methodName, List<Expression> requestExampleExpressionsList) {
        List<String> requestExampleEntries = requestExampleExpressionsList.stream()
            .map(expression -> {
                String path = processBinaryExprString(expression);
                return (StringUtils.isEmpty(path))
                    ? null
                    : fileLoaderService.getValueFromPath(path);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        JsonNode requestJsonSchemaDetails = fileLoaderService.getJsonSchemaIfExists(methodName, "request");
        List<List<InputParam>> inputParams = getInputParams(methodAndAnnotationList);
        RequestDetails requestDetails = new RequestDetails(inputParams, requestExampleEntries, requestJsonSchemaDetails);
        return requestDetails;
    }

    private List<ResponseDetails> getResponseDetails(NodeList<Expression> responses, MethodAndAnnotation methodAndAnnotation) {
        Map<String, Map<String, Expression>> annotationDefaultValues = propertiesService.getAnnotationDefaultValues();

        return responses.stream()
            .map(expression -> {
                AnnotationExpr annotation = (AnnotationExpr) expression;
                Map<String, Expression> processedPairs = defaultParamService.getProcessedPairValues(annotationDefaultValues, annotation);

                boolean isSuccess = processedPairs.get("success").asBooleanLiteralExpr().getValue();
                String code = processBinaryExprString(processedPairs.get("code"));

                String processedDescription = getDescriptionAfterProcessing(processedPairs);

                String examplePath = processBinaryExprString(processedPairs.get("examplePath"));
                String example = fileLoaderService.getValueFromPath(examplePath);

                // This didnt seem it need to be set in the annotations
                // as the methods seemed to return either 200 or 500 when checked
                Integer httpCode = (isSuccess)
                    ? 200
                    : 500;

                String modelKey = processModelAndGetModelKey(
                    methodAndAnnotation.getMethodDeclaration().getType(), processedPairs.get("attachModel").asBooleanLiteralExpr().getValue());

                return new ResponseDetails(
                    code, isSuccess, httpCode, processedDescription, example, modelKey);
            })
            .collect(Collectors.toList());
    }

    private String processModelAndGetModelKey(Type type, Boolean attachModel) {
        if(attachModel) {
            if(type instanceof ClassOrInterfaceType) {
                ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) type;
                String className = classOrInterfaceType.getName().getIdentifier();
                classProcessorService.processClassAsType(className);
                return className;
            }
        }
        return null;
    }

    /**
     * This method gets the input parameters pertaining to a method. There are complexities by overliads such as
     *   - Needing to track overloads, overloads can also be annotated with "usePrimary", requiring to ignore that
     *     version
     * @param methodAndAnnotationList
     * @return
     */
    private List<List<InputParam>> getInputParams(List<MethodAndAnnotation> methodAndAnnotationList) {
        Map<String, Map<String, Expression>> annotationDefaultValues = propertiesService.getAnnotationDefaultValues();

        List<MethodAndAnnotation> requestParamsList = methodAndAnnotationList.stream()
            .map(entry -> getRequestParamsOrdered(annotationDefaultValues, entry))
            .collect(Collectors.toList());

        // Get the length of the method with the largest parameters in each overload
        Optional<Integer> highestValueOptional = requestParamsList.stream()
            .map(expressions -> expressions.getAnnotations().size())
            .max(Comparator.comparing(Integer::valueOf));

        // There are no input params at all
        if (!highestValueOptional.isPresent()) {
            return new ArrayList<>();
        }

        // Iterate through to the maximum position and process all overloads
        return IntStream.range(0, highestValueOptional.get())
            .mapToObj(index ->
                groupInputParamsByIndexValue(annotationDefaultValues, requestParamsList, index))
            .collect(Collectors.toList());
    }

    private List<InputParam> groupInputParamsByIndexValue(Map<String, Map<String, Expression>> annotationDefaultValues,
                                                          List<MethodAndAnnotation> requestParamsList,
                                                          int index) {
        List<InputParam> inputParams = new ArrayList<>();

        requestParamsList.forEach(methodAndAnnotation -> {
            List<AnnotationExpr> expressions = methodAndAnnotation.getAnnotations();
            if(expressions.size() > index) {
                AnnotationExpr annotation = expressions.get(index);
                Map<String, Expression> processedPairs = defaultParamService.getProcessedPairValues(annotationDefaultValues, annotation);

                boolean usePrimary = processedPairs.get("usePrimary").asBooleanLiteralExpr().getValue();

                // Use primary is ignored as that indicates to refer to only the primary value
                if (!usePrimary) {
                    Parameter parameter = validateAndGetParameter(methodAndAnnotation.getMethodDeclaration(), processedPairs);
                    String processedParameterName = getParameterName(processedPairs);

                    String processedDescription = getDescriptionAfterProcessing(processedPairs);

                    String modelKey = processModelAndGetModelKey(
                        parameter.getType(),
                        processedPairs.get("attachModel")
                            .asBooleanLiteralExpr()
                            .getValue());

                    inputParams.add(new InputParam(
                        processedParameterName, processedDescription, modelKey));
                }
            }
        });
        return inputParams;
    }

    /**
     * Load the description from the file or annotation depending on if
     * loadDescriptionFromFile is enabled
     */
    private String getDescriptionAfterProcessing(Map<String, Expression> processedPairs) {
        String initialDescription = processBinaryExprString(processedPairs.get("description"));
        boolean loadDescriptionFromFile = processedPairs.get("loadDescriptionFromFile").asBooleanLiteralExpr().getValue();

        return (loadDescriptionFromFile)
            ? fileLoaderService.getValueFromPath(initialDescription)
            : initialDescription;
    }

    private Parameter validateAndGetParameter(MethodDeclaration primaryMethodDeclaration, Map<String, Expression> processedPairs) {
        String parameterName = processBinaryExprString(processedPairs.get("name"));

        Optional<Parameter> matchingParameter = primaryMethodDeclaration.getParameters()
            .stream()
            .filter(parameter -> parameter.getNameAsString().equals(parameterName))
            .findFirst();

        // The matching parameter should exist, otherwise its a documentation error
        if (!matchingParameter.isPresent()) {
            throw new RuntimeException(
                "There was no matching parameter present for " + parameterName + " and " + primaryMethodDeclaration.getNameAsString());
        }

        return matchingParameter.get();
    }

    private MethodAndAnnotation getRequestParamsOrdered(Map<String, Map<String, Expression>> annotationDefaultValues,
                                                        MethodAndAnnotation entry) {
        Map<String, Expression> processedPairValues = defaultParamService.getProcessedPairValues(
            annotationDefaultValues, entry.getAnnotationExpr());
        NodeList<Expression> requestParams = processedPairValues.get("requestParams").asArrayInitializerExpr().getValues();

        // Group all the annotations by name so we can set them to a specific order
        // e.g. :- method overload 1 can have "String name" in pos 1 while the same field is in pos 2 for a different
        // overload
        Map<String, AnnotationExpr> annotationExprByName = requestParams.stream()
            .map(expression -> {
                AnnotationExpr annotationExpr = expression.asAnnotationExpr();
                NormalAnnotationExpr normalAnnotationExpr = annotationExpr.asNormalAnnotationExpr();
                Map<String, Expression> annotationParams = defaultParamService.convertPairsToMap(normalAnnotationExpr.getPairs());

                String name = processBinaryExprString(annotationParams.get("name"));
                return new AbstractMap.SimpleEntry<>(name, annotationExpr);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<AnnotationExpr> orderedExpressions = entry.getMethodDeclaration()
            .getParameters()
            .stream()
            .map(parameter -> annotationExprByName.get(parameter.getNameAsString()))
            .collect(Collectors.toList());

        return new MethodAndAnnotation(entry.getMethodDeclaration(), orderedExpressions);
    }

    // Sometimes an alias can be set where the code uses a different name, in this cases return the alias
    private String getParameterName(Map<String, Expression> processedPairs) {
        String parameterName = processBinaryExprString(processedPairs.get("name"));
        String parameterAlias = processBinaryExprString(processedPairs.get("alias"));

        return (StringUtils.isEmpty(parameterAlias))
            ? parameterName
            : parameterAlias;
    }

}
