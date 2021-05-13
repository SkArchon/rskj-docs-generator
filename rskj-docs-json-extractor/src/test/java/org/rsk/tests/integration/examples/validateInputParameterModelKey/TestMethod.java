package org.rsk.tests.integration.examples.validateInputParameterModelKey;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocRequestParameter;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateInputParameterModelKey.yaml/request/default",
        requestParams = {
            @JsonRpcDocRequestParameter(
                name = "address",
                description = "**DATA**, 20 Bytes - address to check for balance."
            ),
            @JsonRpcDocRequestParameter(
                name = "value",
                description = "**ValueDTO**, a value",
                attachModel = true
            )
        }
    )
    public String validateInputParameterModelKey(String address, ValueDTO value) {
        return "";
    }
}
