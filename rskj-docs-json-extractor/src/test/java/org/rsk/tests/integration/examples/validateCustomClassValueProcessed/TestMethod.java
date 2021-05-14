package org.rsk.tests.integration.examples.validateCustomClassValueProcessed;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocRequestParameter;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateCustomClassValueProcessed.yaml/request/default",
        requestParams = {
            @JsonRpcDocRequestParameter(
                name = "address",
                description = "**DATA**, 20 Bytes - address to check for balance.",
                attachModel = true
            ),
            @JsonRpcDocRequestParameter(
                name = "value",
                description = "**BOOLEAN**, a value"
            )
        }
    )
    public String validateCustomClassValueProcessed(VariableDTO address, Boolean value) {
        return "";
    }

}
