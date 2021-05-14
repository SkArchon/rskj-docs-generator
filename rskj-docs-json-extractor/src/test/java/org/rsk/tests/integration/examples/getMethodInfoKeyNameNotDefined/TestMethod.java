package org.rsk.tests.integration.examples.getMethodInfoKeyNameNotDefined;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "getMethodInfoKeyNameNotDefined.yaml/testvalues",
        responses = {
            @JsonRpcDocResponse(
                description = "Description value that described",
                code = "Success",
                examplePath = "getMethodInfoKeyNameNotDefined.yaml/response/success"
            ),
        }
    )
    public String getMethodInfoWithInvalidPathDefinition() {
        return "";
    }
}
