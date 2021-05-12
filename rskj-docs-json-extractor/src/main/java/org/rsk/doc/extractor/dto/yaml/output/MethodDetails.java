package org.rsk.doc.extractor.dto.yaml.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodDetails {

    private String method;
    private String description;
    private String summary;
    private MethodType methodType;
    private RequestDetails requestDetails;
    private List<ResponseDetails> responseDetails;

}
