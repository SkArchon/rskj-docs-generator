package org.rsk.doc.extractor.dto.yaml.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputParam {
    private String parameterName;
    private String description;
    private String modelKey;
}