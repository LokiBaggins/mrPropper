package by.baggins.dto;


import java.util.List;

public class PropertySet {
    private String code;
    private List<PropertyValue> values;

    public PropertySet() {}

    public PropertySet(String code, List<PropertyValue> values) {
        this.code = code;
        this.values = values;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PropertyValue> getValues() {
        return values;
    }

    public void setValues(List<PropertyValue> values) {
        this.values = values;
    }

}
