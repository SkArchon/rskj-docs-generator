package org.rsk.tests.integration.examples.validateMultipleMethodEntries;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethods2 {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "methodThree.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available.\n" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "Success",
                examplePath = "methodThree.yaml/response/success"
            ),
        }
    )
    public String methodThree() {
        return "";
    }

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "methodFour.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available.\n" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "Success",
                examplePath = "methodFour.yaml/response/success"
            ),
        }
    )
    public String methodFour() {
        return "";
    }

}
