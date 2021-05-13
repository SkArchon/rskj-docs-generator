package org.rsk.tests.integration.examples.validateMultipleResponses;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateMultipleResponses.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available.\n" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "Success",
                examplePath = "validateMultipleResponses.yaml/response/success"
            ),
            @JsonRpcDocResponse(
                description = "Method parameters invalid.",
                code = "-32602",
                examplePath = "generic.yaml/response/methodInvalid",
                success = false
            ),
        }
    )
    public String validateMultipleResponses() {
        return "";
    }
}
