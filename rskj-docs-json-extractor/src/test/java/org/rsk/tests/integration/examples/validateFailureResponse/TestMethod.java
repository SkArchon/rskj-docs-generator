package org.rsk.tests.integration.examples.validateFailureResponse;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateFailureResponse.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                success = false,
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available.\n" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "-2468",
                examplePath = "validateFailureResponse.yaml/response/success"
            ),
        }
    )
    public String validateFailureResponse() {
        return "";
    }
}
