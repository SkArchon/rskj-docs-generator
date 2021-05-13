package org.rsk.tests.integration.examples.valuesMethodNameSetCorrectly;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "valuesMethodNameSetCorrectly.yaml/request/default"
    )
    public String valuesMethodNameSetCorrectly() {
        return "";
    }
}
