package org.rsk.tests.integration.examples.isReadMethod;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        primaryForOverloads = true,
        isWriteMethod = false,
        requestExamples = "isReadMethod.yaml/request/default"
    )
    public String isReadMethod() {
        return "";
    }
}
