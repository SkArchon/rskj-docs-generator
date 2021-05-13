package org.rsk.doc.extractor;

import java.io.IOException;

public class StartExtractor {

    private static ExtractorInstructions extractorInstructions = ExtractorInstructions.getInstance();

    public static void main(String ...args) throws IOException {
        String path = args[0];
        String yamlPath = args[1];
        String result = extractorInstructions.startParsing(path, yamlPath);
        System.out.println(result);
    }

}
