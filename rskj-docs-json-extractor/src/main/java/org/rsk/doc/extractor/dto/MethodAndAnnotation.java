package org.rsk.doc.extractor.dto;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class MethodAndAnnotation {

    private MethodDeclaration methodDeclaration;
    private AnnotationExpr annotationExpr;
    private List<AnnotationExpr> annotations;

    public MethodAndAnnotation(MethodDeclaration methodDeclaration, AnnotationExpr annotationExpr) {
        this.methodDeclaration = methodDeclaration;
        this.annotationExpr = annotationExpr;
    }

    public MethodAndAnnotation(MethodDeclaration methodDeclaration, List<AnnotationExpr> annotations) {
        this.methodDeclaration = methodDeclaration;
        this.annotations = annotations;
    }
}
