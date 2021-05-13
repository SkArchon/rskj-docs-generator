package org.rsk.tests.integration.examples.validateResponseExample;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateResponseExample.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description = "Description value that described",
                code = "Success",
                examplePath = "validateResponseExample.yaml/response/success"
            ),
        }
    )
    public String validateResponseExample() {
        return "";
    }
}
