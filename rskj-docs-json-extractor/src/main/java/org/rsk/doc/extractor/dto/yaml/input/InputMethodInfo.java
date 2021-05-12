package org.rsk.doc.extractor.dto.yaml.input;

import lombok.Data;

import java.util.Map;

@Data
public class InputMethodInfo {

    private MethodDescription description;
    private Map<String, String> response;
    private Map<String, String> request;

    @Data
    public static class MethodDescription {
        private Map<String, String> response;
        private Map<String, String> request;
    }
}
