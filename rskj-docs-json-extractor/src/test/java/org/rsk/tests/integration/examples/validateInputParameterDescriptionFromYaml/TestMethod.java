package org.rsk.tests.integration.examples.validateInputParameterDescriptionFromYaml;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocRequestParameter;
import org.rsk.tests.integration.examples.validateInputParameterModelKey.ValueDTO;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateInputParameterDescriptionFromYaml.yaml/request/default",
        requestParams = {
            @JsonRpcDocRequestParameter(
                name = "address",
                description = "**DATA**, 20 Bytes - address to check for balance."
            ),
            @JsonRpcDocRequestParameter(
                name = "values",
                description = "validateInputParameterDescriptionFromYaml.yaml/description/request/transaction",
                loadDescriptionFromFile = true
            )
        }
    )
    public String validateInputParameterDescriptionFromYaml(String address, ValueDTO values) {
        return "";
    }
}
