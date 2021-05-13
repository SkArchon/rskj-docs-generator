package org.rsk.tests.integration.examples.descriptionSet;

import org.rsk.docs.annotation.JsonRpcDoc;

public class TestMethod {

    @JsonRpcDoc(
        description = "Method Description",
        primaryForOverloads = true,
        isWriteMethod = false,
        requestExamples = "descriptionSet.yaml/request/default"
    )
    public String descriptionSet() {
        return "";
    }
}
