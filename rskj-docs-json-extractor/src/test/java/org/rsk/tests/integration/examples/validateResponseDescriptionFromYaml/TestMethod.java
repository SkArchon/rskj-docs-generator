package org.rsk.tests.integration.examples.validateResponseDescriptionFromYaml;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateResponseDescriptionFromYaml.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description = "validateResponseDescriptionFromYaml.yaml/description/response/transaction",
                loadDescriptionFromFile = true,
                code = "Success",
                examplePath = "validateResponseDescriptionFromYaml.yaml/response/success"
            ),
        }
    )
    public String validateResponseDescriptionFromYaml() {
        return "";
    }
}
