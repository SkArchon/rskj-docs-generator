package org.rsk.tests.integration.examples.validateMultipleMethodEntries;

import org.rsk.docs.annotation.JsonRpcDoc;
import org.rsk.docs.annotation.JsonRpcDocResponse;

public class TestMethods1 {

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "methodOne.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available.\n" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "Success",
                examplePath = "methodOne.yaml/response/success"
            ),
        }
    )
    public String methodOne() {
        return "";
    }

    @JsonRpcDoc(
        isWriteMethod = false,
        requestExamples = "methodTwo.yaml/request/default",
        responses = {
            @JsonRpcDocResponse(
                description =
                    "**DATA**, 32 Bytes - the transaction hash, or the zero hash if the transaction is not yet available.\n" +
                        "Use eth_getTransactionReceipt to get the contract address, after the transaction was mined, when you created a contract.",
                code = "Success",
                examplePath = "methodTwo.yaml/response/success"
            ),
        }
    )
    public String methodTwo() {
        return "";
    }

}
