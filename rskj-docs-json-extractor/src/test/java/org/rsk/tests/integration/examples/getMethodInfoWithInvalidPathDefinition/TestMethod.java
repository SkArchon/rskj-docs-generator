package org.rsk.tests.integration.examples.getMethodInfoWithInvalidPathDefinition;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        // This should have atleast one "/" separator
        // however in this test case its ignored
        requestExamples = "getMethodInfoWithInvalidPathDefinition.yaml",
        responses = {
            @JsonRpcDocResponse(
                description = "Description value that described",
                code = "Success",
                examplePath = "getMethodInfoWithInvalidPathDefinition.yaml/response/success"
            ),
        }
    )
    public String getMethodInfoWithInvalidPathDefinition() {
        return "";
    }
}
