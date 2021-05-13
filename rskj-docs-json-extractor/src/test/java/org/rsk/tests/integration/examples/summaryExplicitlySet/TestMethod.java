package org.rsk.tests.integration.examples.summaryExplicitlySet;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        description = "Method Description",
        summary = "Different Summary",
        isWriteMethod = false,
        requestExamples = "summaryExplicitlySet.yaml/request/default"
    )
    public String summaryExplicitlySet() {
        return "";
    }

}
