package org.rsk.doc.extractor;

import java.io.IOException;

public class StartExtractor {

    private static ExtractorInstructions extractorInstructions = ExtractorInstructions.getInstance();

    /**
     * The main function that will start and initiate the parsing process for the
     * given paths
     * @param args arguments for the paths required to run this application
     * @throws IOException
     */
    public static void main(String ...args) throws IOException {
        String path = args[0];
        String yamlPath = args[1];
        String result = extractorInstructions.startParsing(path, yamlPath);
        System.out.println(result);
    }

}
