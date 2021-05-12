package org.rsk.doc.extractor.dto.yaml.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetails {
    private List<List<InputParam>> inputParams;
    private List<String> requestExamples;
}