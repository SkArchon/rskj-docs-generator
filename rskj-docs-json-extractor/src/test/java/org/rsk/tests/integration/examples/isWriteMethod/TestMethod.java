package org.rsk.tests.integration.examples.isWriteMethod;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = true,
        requestExamples = "isWriteMethod.yaml/request/default"
    )
    public String isWriteMethod() {
        return "";
    }
}
