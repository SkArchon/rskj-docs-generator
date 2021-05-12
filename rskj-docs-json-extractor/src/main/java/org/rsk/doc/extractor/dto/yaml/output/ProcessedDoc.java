package org.rsk.doc.extractor.dto.yaml.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedDoc {

    private String title;
    private String description;
    private List<MethodDetails> methodDetails;
    private Map<String, String> models;

}
