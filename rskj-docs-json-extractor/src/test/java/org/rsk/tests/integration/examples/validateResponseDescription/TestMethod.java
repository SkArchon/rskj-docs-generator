package org.rsk.tests.integration.examples.validateResponseDescription;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethod {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "validateResponseDescription.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available.\n" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "Success",
                examplePath = "validateResponseDescription.yaml/response/success"
            ),
        }
    )
    public String testRequestDetails() {
        return "";
    }
}
