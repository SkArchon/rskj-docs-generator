package org.rsk.doc.extractor.dto.yaml.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDetails {
    private String code;
    private Boolean success;
    private Integer httpCode;
    private String description;
    private String responseExample;
    private String modelKey;
}