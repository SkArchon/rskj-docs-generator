package org.rsk.tests.integration;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rsk.doc.extractor.ExtractorInstructions;

import org.rsk.doc.extractor.service.FileLoaderService;
import org.rsk.tests.integration.helpers.SnapshotHelpers;
import org.rsk.tests.integration.helpers.Whitebox;

@RunWith(MockitoJUnitRunner.class)
public class DocsTest {

    private final ExtractorInstructions extractorInstructions = ExtractorInstructions.getInstance();
    private final FileLoaderService fileLoaderService = FileLoaderService.getInstance();

    private final SnapshotHelpers snapshotHelpers;

    // TODO : This might need to be changed
    public DocsTest () {
        String BASE_PATH = System.getProperty("user.dir");
        snapshotHelpers = new SnapshotHelpers(extractorInstructions, fileLoaderService, BASE_PATH);
    }

    @After
    public void after() {
        Whitebox.setInternalState(extractorInstructions, "fileLoaderService", fileLoaderService);
    }

    @Test
    public void testIsWriteMethod() {
        String testPackageName = "isWriteMethod";
        String path = "$.methodDetails[0].methodType";
        snapshotHelpers.getAndAssertValue(testPackageName, path);
    }

    @Test
    public void testIsReadMethod() {
        String testPackageName = "isReadMethod";
        String path = "$.methodDetails[0].methodType";
        snapshotHelpers.getAndAssertValue(testPackageName, path);
    }

    @Test
    public void testIsDescriptionSet() {
        String testPackageName = "descriptionSet";
        String[] paths = {
            "$.methodDetails[0].description", "$.methodDetails[0].summary"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void testIsSummaryExplicitlySet() {
        String testPackageName = "summaryExplicitlySet";
        String[] paths = {
            "$.methodDetails[0].description", "$.methodDetails[0].summary"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void testValuesMethodNameSetCorrectly() {
        String testPackageName = "valuesMethodNameSetCorrectly";
        String path = "$.methodDetails[0].method";
        snapshotHelpers.getAndAssertValue(testPackageName, path);
    }

    @Test
    public void testDocTitleAndDescription() {
        String testPackageName = "docDetails";
        String[] paths = {
            "$.title", "$.description"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, false, paths);
    }

    @Test
    public void testRequestDetails() {
        String testPackageName = "testRequestDetails";
        String[] paths = {
            "$.methodDetails[0].requestDetails.requestExamples[0]"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void testRequestDetailsMultiple() {
        String testPackageName = "testRequestDetailsMultiple";
        String[] paths = {
            "$.methodDetails[0].requestDetails.requestExamples[0]",
            "$.methodDetails[0].requestDetails.requestExamples[1]"
        };
        snapshotHelpers.getAndAssertValue(testPackageName);
    }

    @Test
    public void validateInputParameter() {
        String testPackageName = "validateInputParameter";
        String[] paths = {
            "$.methodDetails[0].requestDetails.inputParams[0][0]"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateInputParameterMultiple() {
        String testPackageName = "validateInputParameterMultiple";
        String[] paths = {
            "$.methodDetails[0].requestDetails.inputParams[0][0]",
            "$.methodDetails[0].requestDetails.inputParams[1][0]"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }


    @Test
    public void validateInputParameterMultipleWithOverloads() {
        String testPackageName = "validateInputParameterMultipleWithOverloads";
        String[] paths = {
            "$.methodDetails[0].requestDetails.inputParams[0][0]",
            "$.methodDetails[0].requestDetails.inputParams[0][1]",
            "$.methodDetails[0].requestDetails.inputParams[1][0]"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateInputParameterUsePrimary() {
        String testPackageName = "validateInputParameterMultipleUsePrimary";
        String[] paths = {
            "$.methodDetails[0].requestDetails.inputParams[0][0]",
            "$.methodDetails[0].requestDetails.inputParams[1][0]"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateInputParameterModelKey() {
        String testPackageName = "validateInputParameterModelKey";
        String[] paths = {
            "$.methodDetails[0].requestDetails.inputParams[0][0].modelKey",
            "$.models.ValueDTO"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateInputParameterDescriptionFromYaml() {
        String testPackageName = "validateInputParameterDescriptionFromYaml";
        String[] paths = {
            "$.methodDetails[0].requestDetails.inputParams[0][0].description"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }


    @Test
    public void validateResponseDescription() {
        String testPackageName = "validateResponseDescription";
        String[] paths = {
            "$.methodDetails[0].responseDetails[0].description"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateResponseDescriptionFromYaml() {
        String testPackageName = "validateResponseDescriptionFromYaml";
        String[] paths = {
            "$.methodDetails[0].responseDetails[0].description"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    // TODO : This might make more sense to assert in the test method
    @Test
    public void validateSuccessResponse() {
        String testPackageName = "validateSuccessResponse";
        String[] paths = {
            "$.methodDetails[0].responseDetails[0].httpCode",
            "$.methodDetails[0].responseDetails[0].success"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    // TODO : This might make more sense to assert in the test method
    @Test
    public void validateFailureResponse() {
        String testPackageName = "validateFailureResponse";
        String[] paths = {
            "$.methodDetails[0].responseDetails[0].httpCode",
            "$.methodDetails[0].responseDetails[0].success"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateResponseModelKey() {
        String testPackageName = "validateResponseModelKey";
        String[] paths = {
            "$.methodDetails[0].responseDetails[0].modelKey",
            "$.models.ValueDTO"
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateResponseExample() {
        String testPackageName = "validateResponseExample";
        String[] paths = {
            "$.methodDetails[0].responseDetails[0].responseExample",
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateMultipleResponses() {
        String testPackageName = "validateMultipleResponses";
        String[] paths = {
            "$.methodDetails[0].responseDetails[0]",
            "$.methodDetails[0].responseDetails[1]",
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

    @Test
    public void validateMultipleMethodEntries() {
        String testPackageName = "validateMultipleMethodEntries";
        String[] paths = {
            "$.methodDetails[0].method",
            "$.methodDetails[1].method",
            "$.methodDetails[2].method",
            "$.methodDetails[3].method",
        };
        snapshotHelpers.getAndAssertValue(testPackageName, paths);
    }

}
