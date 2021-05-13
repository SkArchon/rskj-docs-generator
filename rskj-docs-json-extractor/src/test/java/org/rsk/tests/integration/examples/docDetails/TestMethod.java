package org.rsk.tests.integration.examples.docDetails;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        primaryForOverloads = true,
        isWriteMethod = false,
        requestExamples = "docDetails.yaml/request/default"
    )
    public String docDetails() {
        return "";
    }
}
