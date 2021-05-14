package org.rsk.tests.integration.examples.validateCustomClassValueProcessed;

import org.rsk.docs.annotation.JsonRpcDocModelType;

public class VariableDTO {

    private Integer value;

    @JsonRpcDocModelType(
        documentationType = "Integer",
        processClassNames = { "AdditionalDTO" }
    )
    private String description;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
