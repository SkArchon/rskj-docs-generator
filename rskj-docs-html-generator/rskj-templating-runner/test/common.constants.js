const METHOD_INPUT = {
    "method": "eth_getBlockByNumber",
    "description": "Returns information about a block by the block number.",
    "summary": "Returns information about a block by the block number.",
    "methodType": "READ",
    "requestDetails": {
        "inputParams": [
            [
                {
                    "parameterName": "bnOrId",
                    "description": "integer of a block number, or the string 'earliest', 'latest' or 'pending', as in the default block parameter.",
                    "modelKey": null
                }
            ],
        ],
        "requestExamples": [
            "{ \"params\": [\"0x1b4\", true] }\n"
        ],
        "schema": {
            "$schema": "http://json-schema.org/draft-04/schema#",
            "type": "object",
            "required": [
                "id",
                "jsonrpc",
                "method",
                "params"
            ]
        }
    },
    "responseDetails": [
        {
            "code": "Success",
            "success": true,
            "httpCode": 200,
            "description": "The transaction passed in could not be found",
            "responseExample": "{\"result\":null}\n",
            "modelKey": null
        }
    ],
    "responseSchema": {
        "$schema": "http://json-schema.org/draft-04/schema#",
        "type": "object",
        "required": [
            "result"
        ]
    }
}

const METHOD_INPUT_WITHOUT_REQUEST_EXAMPLES = {
    "method": "eth_getBlockByNumber",
    "description": "Returns information about a block by the block number.",
    "summary": "Returns information about a block by the block number.",
    "methodType": "READ",
    "requestDetails": {
        "inputParams": [
            [
                {
                    "parameterName": "bnOrId",
                    "description": "integer of a block number, or the string 'earliest', 'latest' or 'pending', as in the default block parameter.",
                    "modelKey": null
                }
            ],
        ],
        "requestExamples": [],
        "schema": {
            "$schema": "http://json-schema.org/draft-04/schema#",
            "type": "object",
            "required": [
                "id",
                "jsonrpc",
                "method",
                "params"
            ]
        }
    },
    "responseDetails": [
        {
            "code": "Success",
            "success": true,
            "httpCode": 200,
            "description": "The transaction passed in could not be found",
            "responseExample": "{\"result\":null}\n",
            "modelKey": null
        }
    ],
    "responseSchema": {
        "$schema": "http://json-schema.org/draft-04/schema#",
        "type": "object",
        "required": [
            "result"
        ]
    }
}
const METHOD_OUTPUT_PROCESSED_DATA = {"documentationDataset":{"method":"eth_getBlockByNumber","description":"Returns information about a block by the block number.","summary":"Returns information about a block by the block number.","methodType":"READ","requestDetails":{"inputParams":[[{"parameterName":"bnOrId","description":"<div></div>","modelKey":null}]],"requestExamples":["<html></html>"],"schema":{"$schema":"http://json-schema.org/draft-04/schema#","type":"object","required":["id","jsonrpc","method","params"]}},"responseDetails":[{"code":"Success","success":true,"httpCode":200,"description":"<div></div>","responseExample":"<html></html>","modelKey":null}],"responseSchema":{"$schema":"http://json-schema.org/draft-04/schema#","type":"object","required":["result"]}},"processedAdditionalData":{"inputParamsPresent":true,"actualRequestExamples":["{ \"params\": [\"0x1b4\", true] }\n"],"className":"methodName","hasRequestSchema":true,"hasResponseSchema":true}};

const METHOD_OUTPUT_REQUEST_SCHEMA = {"$schema":"http://json-schema.org/draft-04/schema#","type":"object","required":["id","jsonrpc","method","params"]};
const METHOD_OUTPUT_RESPONSE_SCHEMA = {"$schema":"http://json-schema.org/draft-04/schema#","type":"object","required":["result"]};

const METHOD_INPUT_WITHOUT_SCHEMA = {
    "method": "eth_getBlockByNumber",
    "description": "Returns information about a block by the block number.",
    "summary": "Returns information about a block by the block number.",
    "methodType": "READ",
    "requestDetails": {
        "inputParams": [
            [
                {
                    "parameterName": "bnOrId",
                    "description": "integer of a block number, or the string 'earliest', 'latest' or 'pending', as in the default block parameter.",
                    "modelKey": null
                }
            ],
        ],
        "requestExamples": [
            "{ \"params\": [\"0x1b4\", true] }\n"
        ],
        "schema": null
    },
    "responseDetails": [
        {
            "code": "Success",
            "success": true,
            "httpCode": 200,
            "description": "The transaction passed in could not be found",
            "responseExample": "{\"result\":null}\n",
            "modelKey": null
        }
    ],
    "responseSchema": null
}

const METHOD_INPUT_WITHOUT_RESPONSE_DETAILS = {
    "method": "eth_getBlockByNumber",
    "description": "Returns information about a block by the block number.",
    "summary": "Returns information about a block by the block number.",
    "methodType": "READ",
    "requestDetails": {
        "inputParams": [
            [
                {
                    "parameterName": "bnOrId",
                    "description": "integer of a block number, or the string 'earliest', 'latest' or 'pending', as in the default block parameter.",
                    "modelKey": null
                }
            ],
        ],
        "requestExamples": [
            "{ \"params\": [\"0x1b4\", true] }\n"
        ],
        "schema": null
    },
    "responseSchema": null
}

const METHOD_OUTPUT_WITHOUT_RESPONSE_DETAILS = {"documentationDataset":{"method":"eth_getBlockByNumber","description":"Returns information about a block by the block number.","summary":"Returns information about a block by the block number.","methodType":"READ","requestDetails":{"inputParams":[[{"parameterName":"bnOrId","description":"<div></div>","modelKey":null}]],"requestExamples":["<html></html>"],"schema":null},"responseSchema":null},"processedAdditionalData":{"inputParamsPresent":true,"actualRequestExamples":["{ \"params\": [\"0x1b4\", true] }\n"],"className":"methodName","hasRequestSchema":false,"hasResponseSchema":false}};
const METHOD_OUTPUT_WITHOUT_REQUEST_EXAMPLES = {"documentationDataset":{"method":"eth_getBlockByNumber","description":"Returns information about a block by the block number.","summary":"Returns information about a block by the block number.","methodType":"READ","requestDetails":{"inputParams":[[{"parameterName":"bnOrId","description":"<div></div>","modelKey":null}]],"requestExamples":["<html></html>"],"schema":{"$schema":"http://json-schema.org/draft-04/schema#","type":"object","required":["id","jsonrpc","method","params"]}},"responseDetails":[{"code":"Success","success":true,"httpCode":200,"description":"<div></div>","responseExample":"<html></html>","modelKey":null}],"responseSchema":{"$schema":"http://json-schema.org/draft-04/schema#","type":"object","required":["result"]}},"processedAdditionalData":{"inputParamsPresent":true,"actualRequestExamples":["{}"],"className":"methodName","hasRequestSchema":true,"hasResponseSchema":true}};


module.exports = {
    METHOD_INPUT,
    METHOD_INPUT_WITHOUT_SCHEMA,
    METHOD_OUTPUT_PROCESSED_DATA,
    METHOD_INPUT_WITHOUT_REQUEST_EXAMPLES,
    METHOD_INPUT_WITHOUT_RESPONSE_DETAILS,
    METHOD_OUTPUT_WITHOUT_RESPONSE_DETAILS,
    METHOD_OUTPUT_WITHOUT_REQUEST_EXAMPLES,
    METHOD_OUTPUT_REQUEST_SCHEMA,
    METHOD_OUTPUT_RESPONSE_SCHEMA    
}
