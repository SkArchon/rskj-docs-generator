export const SUCCESSFUL_TRY_REQUEST =  {
    "id": 1,
    "jsonrpc": "2.0",
    "method": "eth_getBlockByNumber",
    "params": [
        "0x1b4",
        true
    ]
};

export const SUCCESSFUL_METHOD_WITH_VALID_SCHEMA = {
    "data": {
        "jsonrpc": "2.0",
        "id": 1,
        "result": {
            "number": "0x1b4",
            "hash": "0xf1652d8322a880e520f996f7d28b645814a58a202d7d2ab7f058e5566fe4f9f3",
            "parentHash": "0x9203a97b2595a44e8d31dd7bf607155620dd50982ac282fc776e21a58b2f7795",
            "sha3Uncles": "0x2f752c2e72d38f3677bfa47bd54ec10441141a6415f3be121341d94276dfd732",
            "logsBloom": "0x00000008000000800000000000000000000000000000000000000000000008000000000000040000000000000000000050000000000000000000000000000000000000000000000000000000005000000010000000000000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000200000000000200000000000001040000000000000400000000000000000000100000000000000010000000000000000000001000000000000001000001000000000000000000000000000020000000000080200000100000000000000000000000000000000000000000000000000000000000000000000000",
            "transactionsRoot": "0xe94962ab91fe305207670ca17d958ce15bdd553aa6630eba07476c0f35a65b50",
            "stateRoot": "0xa12e5b995ec8056d6c2d2558c6f4e1ab7f0d6d3eaed868e84c00923cf58bb642",
            "receiptsRoot": "0x2cdc6fdc519baad55c4f9d86d1415dbfe21265c6b2ba1f283e1391e015735c42",
            "miner": "0x1fab9a0e24ffc209b01faa5a61ad4366982d0b7f",
            "difficulty": "0x1aa2b738",
            "totalDifficulty": "0x1f61daa8d6",
            "extraData": "0x",
            "size": "0xafe",
            "gasLimit": "0x67c280",
            "gasUsed": "0x0",
            "timestamp": "0x5d1f7c06",
            "transactions": [
                {
                    "hash": "0x05140aca6b0312a2d7dce35a3227b86b8a973fc69e508c5d4beb2b3c88fa28a1",
                    "nonce": "0x1b3",
                    "blockHash": "0xf1652d8322a880e520f996f7d28b645814a58a202d7d2ab7f058e5566fe4f9f3",
                    "blockNumber": "0x1b4",
                    "transactionIndex": "0x0",
                    "from": "0x0000000000000000000000000000000000000000",
                    "to": "0x0000000000000000000000000000000001000008",
                    "gas": "0x0",
                    "gasPrice": "0x0",
                    "value": "0x0",
                    "input": "0x",
                    "v": null,
                    "r": null,
                    "s": null
                }
            ],
            "uncles": [
                "0x6d12270d24b591931974b75c1a753d1db8c22372baf1aaaa79253858caeed52b",
                "0x9ea21e7d69ffb54face62769a04b08434d606a40c12e1348c4da348d91a9f5e4"
            ],
            "minimumGasPrice": "0x0",
            "bitcoinMergedMiningHeader": "0x000000202d17c9a169c4c93eaf376bc35ff6561d0b743880600f3d8370020000000000008d31ad07fc72224ddd23632463f2119d977defc86f1b0ebf16171225bf9c316c117c1f5d531d041a8046d09f",
            "bitcoinMergedMiningCoinbaseTransaction": "0x000000000000008075aec68cfceb2bda1dcc166c1564517793f90ee07fcd446699d440628290564f6088ac0000000000000000266a24aa21a9ed08ade7f48a09603efcf910361380e3ce1ebc9813eb4a7d85473a9c170d79017e00000000000000002a6a52534b424c4f434b3ac4d2419792f08338d4fa5fd21c61ad79484c875f1ac9b483f253842edb96c92400000000",
            "bitcoinMergedMiningMerkleProof": "0x91062f54a2ea641bebaa19e4d58df522a395bc89e6e230f98cc343d39a8deb070f190492c042ea5ebb75f6e9488345eedf5ff35219fd3ac21be0778e7feed9fc117db6cd70f02ddc5635577981626db173937a041ae38c9cebd10e645fd4ddc00d8662b4bbb4e4309ff07fb30ca6c3365bea0f823a5fe83f60750a3b0f62da5ec8ca82637afafe9869ecfdb26e5136c849080b46f6230090773949a1010a91ce82b1c65d92f57836e28e9f2f90133d4dc2376ea337bd76836058e5cc9ae6975e6211f67ff7f76793b2f505a621ef7c1f90f0f4d4badc62aaa70a20f223160961",
            "hashForMergedMining": "0xc4d2419792f08338d4fa5fd21c61ad79484c875f1ac9b483f253842edb96c924",
            "paidFees": "0x0",
            "cumulativeDifficulty": "0x4ff04782"
        }
    },
    "status": 200,
    "statusText": "",
    "request": {}
}