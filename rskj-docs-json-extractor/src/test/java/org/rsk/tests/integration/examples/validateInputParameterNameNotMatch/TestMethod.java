package org.rsk.tests.integration.examples.validateInputParameterNameNotMatch;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocRequestParameter;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateInputParameterNameNotMatch.yaml/request/default",
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
    public String validateInputParameterNameNotMatch(String address, Boolean abcd) {
        return "";
    }

}
