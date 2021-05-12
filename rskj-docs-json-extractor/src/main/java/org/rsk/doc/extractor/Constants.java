package org.rsk.doc.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;

public class Constants {

    public static final String DOCS_INFO_FILE_NAME = "docs_info.yaml";

    @Getter
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Getter
    private static final ObjectMapper jsonMapper = new ObjectMapper();


}
