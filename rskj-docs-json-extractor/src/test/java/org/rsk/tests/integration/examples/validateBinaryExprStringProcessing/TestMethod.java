package org.rsk.tests.integration.examples.validateBinaryExprStringProcessing;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateBinaryExprStringProcessing.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available." +
                        " value" + "values" + "abcd" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "Success",
                examplePath = "validateBinaryExprStringProcessing.yaml/response/success"
            ),
        }
    )
    public String validateBinaryExprStringProcessing() {
        return "";
    }
}
