package org.rsk.tests.integration.examples.testRequestDetailsMultiple;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = {
            "testRequestDetailsMultiple.yaml/request/default",
            "testRequestDetailsMultiple.yaml/request/different",
        }
    )
    public String testRequestDetailsMultiple() {
        return "";
    }
}
