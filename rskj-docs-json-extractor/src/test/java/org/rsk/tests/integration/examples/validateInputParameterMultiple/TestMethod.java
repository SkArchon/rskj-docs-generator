package org.rsk.tests.integration.examples.validateInputParameterMultiple;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocRequestParameter;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateInputParameterMultiple.yaml/request/default",
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
    public String validateInputParameterMultiple(String address, Boolean value) {
        return "";
    }
}
