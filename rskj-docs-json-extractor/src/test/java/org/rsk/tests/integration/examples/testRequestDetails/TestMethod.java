package org.rsk.tests.integration.examples.testRequestDetails;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "testRequestDetails.yaml/request/default"
    )
    public String testRequestDetails() {
        return "";
    }
}
