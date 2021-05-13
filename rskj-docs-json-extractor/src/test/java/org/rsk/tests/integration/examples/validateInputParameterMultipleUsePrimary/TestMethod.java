package org.rsk.tests.integration.examples.validateInputParameterMultipleUsePrimary;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocRequestParameter;

public class TestMethod {

    @JsonRpcDoc(
        primaryForOverloads = false,
        isWriteMethod = false,
        requestExamples = "validateInputParameterMultipleUsePrimary.yaml/request/default",
        requestParams = {
            @JsonRpcDocRequestParameter(
                name = "address",
                description = "**DATA**, dataset.",
                usePrimary = true
            ),
        }
    )
    public String validateInputParameterMultipleUsePrimary(String address) {
        return "";
    }

    @JsonRpcDoc(
        primaryForOverloads = true,
        isWriteMethod = false,
        requestExamples = "validateInputParameterMultipleUsePrimary.yaml/request/default",
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
    public String validateInputParameterMultipleUsePrimary(String address, Boolean value) {
        return "";
    }

}
