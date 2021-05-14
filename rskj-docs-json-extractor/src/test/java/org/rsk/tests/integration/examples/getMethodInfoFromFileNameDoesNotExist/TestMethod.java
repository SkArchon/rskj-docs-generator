package org.rsk.tests.integration.examples.getMethodInfoFromFileNameDoesNotExist;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "getMethodInfoFromFileNameDoesNotExist.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description = "Description value that described",
                code = "Success",
                examplePath = "getMethodInfoFromFileNameDoesNotExist.yaml/response/success"
            ),
        }
    )
    public String getMethodInfoFromFileNameDoesNotExist() {
        return "";
    }
}
