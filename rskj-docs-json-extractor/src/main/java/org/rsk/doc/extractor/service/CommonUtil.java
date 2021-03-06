package org.rsk.doc.extractor.service;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.StringLiteralExpr;

public class CommonUtil {

    public static String processBinaryExprString(Expression parentExpr) {
        if(parentExpr.isStringLiteralExpr()) {
            return parentExpr.asStringLiteralExpr().getValue();
        }

        /**
         * This section takes in the parent expression and processes all the
         * concatenation plus operators and merges the string. This is a recursive function.
         */
        return parentExpr
            .getChildNodes()
            .stream()
            .map(node -> {
                if(node instanceof StringLiteralExpr) {
                    return ((StringLiteralExpr) node).getValue();
                }

                BinaryExpr binaryExpr = (BinaryExpr) node;
                if(!binaryExpr.getOperator().equals(BinaryExpr.Operator.PLUS)) {
                    throw new RuntimeException("unsupported operator used in concat string");
                }

                Expression left = binaryExpr.getLeft();
                Expression right = binaryExpr.getRight();

                // Recursively get the concatenated string from the left side
                String leftString = (left.isBinaryExpr())
                    ? processBinaryExprString(left.asBinaryExpr())
                    : left.asStringLiteralExpr().getValue();

                // Recursively get the concatenated string from the right side
                String rightString = (right.isBinaryExpr())
                    ? processBinaryExprString(right.asBinaryExpr())
                    : right.asStringLiteralExpr().getValue();

                return leftString + rightString;
            })
            .reduce("", (o, o2) -> o + o2);
    }


}
