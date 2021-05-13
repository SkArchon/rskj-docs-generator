package org.rsk.tests.integration.examples.validateInputParameterMultipleWithOverloads;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocRequestParameter;

public class TestMethod {

    @JsonRpcDoc(
        primaryForOverloads = false,
        isWriteMethod = false,
        requestExamples = "validateInputParameterMultipleWithOverloads.yaml/request/default",
        requestParams = {
            @JsonRpcDocRequestParameter(
                name = "dataset",
                description = "**DATA**, dataset."
            ),
        }
    )
    public String validateInputParameterMultipleWithOverloads(Double dataset) {
        return "";
    }

    @JsonRpcDoc(
        primaryForOverloads = true,
        isWriteMethod = false,
        requestExamples = "validateInputParameterMultipleWithOverloads.yaml/request/default",
        requestParams = {
            @JsonRpcDocRequestParameter(
                name = "address",
                description = "**DATA**, 20 Bytes - address to check for balance."
            ),
            @JsonRpcDocRequestParameter(
                name = "value",
                description = "**BOOLEAN**, a value"
            )
        }
    )
    public String validateInputParameterMultipleWithOverloads(String address, Boolean value) {
        return "";
    }

}
