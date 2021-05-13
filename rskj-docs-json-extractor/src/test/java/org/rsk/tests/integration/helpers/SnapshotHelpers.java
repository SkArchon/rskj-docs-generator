package org.rsk.tests.integration.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.mockito.Mockito;
import org.rsk.doc.extractor.Constants;
import org.rsk.doc.extractor.ExtractorInstructions;
import org.rsk.doc.extractor.dto.yaml.input.InputDocsInfo;
import org.rsk.doc.extractor.service.FileLoaderService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.rsk.doc.extractor.Constants.DOCS_INFO_FILE_NAME;

public class SnapshotHelpers {

    private final ExtractorInstructions extractorInstructions;
    private final FileLoaderService fileLoaderService;

    private static ObjectMapper objectMapper = Constants.getJsonMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private final String TESTS_BASE_PATH;
    private final String ANNOTATIONS_PATH;

    public SnapshotHelpers(ExtractorInstructions extractorInstructions,
                           FileLoaderService fileLoaderService,
                           String basePath) {
        this.extractorInstructions = extractorInstructions;
        this.fileLoaderService = fileLoaderService;

        this.TESTS_BASE_PATH = basePath + "/src/test/java/org/rsk/tests/integration/examples";
        this.ANNOTATIONS_PATH = basePath + "/src/test/java/org/rsk/docs/annotation";
    }

    private String getProcessedValueForPackage(String testPackageName, Boolean skipYamlDocs) {
        try {
            String path = setFileLoaderWithAnnotationResults(testPackageName, skipYamlDocs);
            return extractorInstructions.startParsing(path, path);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String setFileLoaderWithAnnotationResults(String testPackageName, Boolean skipYamlDocs) {
        try {
            String path = getTestCasePackagePath(testPackageName);

            List<ParseResult<CompilationUnit>> actualResults = fileLoaderService.getParseResults(path);
            List<ParseResult<CompilationUnit>> annotationResults = fileLoaderService.getParseResults(ANNOTATIONS_PATH);

            actualResults.addAll(annotationResults);
            FileLoaderService spy = Mockito.spy(fileLoaderService);
            Mockito.when(spy.getParseResults(path)).thenReturn(actualResults);

            if(skipYamlDocs) {
                InputDocsInfo docsInfo = new InputDocsInfo();
                docsInfo.setTitle("Title");
                docsInfo.setDescription("Description");
                Mockito.doReturn(docsInfo).when(spy).getDocsInfo(Mockito.anyString());
            }

            Whitebox.setInternalState(extractorInstructions, "fileLoaderService", spy);

            return path;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getTestCasePackagePath(String packageName) {
        return TESTS_BASE_PATH + "/" + packageName;
    }

    public void getAndAssertValue(String testPackageName) {
        String[] jsonPaths = new String[]{};
        getAndAssertValue(testPackageName, jsonPaths);
    }

    public void getAndAssertValue(String testPackageName, String... jsonPaths) {
        String value = getProcessedValueForPackage(testPackageName, true);
        assertSnapshot(value, testPackageName, jsonPaths);
    }

    public void getAndAssertValue(String testPackageName, Boolean skipYamlDocs, String... jsonPaths) {
        String value = getProcessedValueForPackage(testPackageName, skipYamlDocs);
        assertSnapshot(value, testPackageName, jsonPaths);
    }


    public void assertSnapshot(String actual, String snapshotName, String... jsonPaths) {
        try {
            FileInputStream fis = new FileInputStream(TESTS_BASE_PATH + "/" + snapshotName + "/expected.json");
            String expected = IOUtils.toString(fis, "UTF-8");

            // We do this so any indentation differences such as whitespaces are not considered
            JsonNode expectedJson = objectMapper.readValue(expected, JsonNode.class);
            JsonNode actualJson = objectMapper.readValue(actual, JsonNode.class);

            String expectedString = objectMapper.writeValueAsString(expectedJson);
            String actualString = objectMapper.writeValueAsString(actualJson);

            if(ArrayUtils.isEmpty(jsonPaths)) {
                Assert.assertEquals(expectedString, actualString);
            } else {
                for (String jsonPath : jsonPaths) {
                    Object expectedData = JsonPath.read(expectedString, jsonPath);
                    Object actualData = JsonPath.read(actualString, jsonPath);
                    Assert.assertEquals(expectedData, actualData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
