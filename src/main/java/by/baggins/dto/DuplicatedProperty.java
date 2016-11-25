package by.baggins.dto;


import java.util.List;

public class DuplicatedProperty {
    private String code;
    private List<DuplicatedPropertyValue> values;

    public DuplicatedProperty() { }

    public DuplicatedProperty(String code, List<DuplicatedPropertyValue> values) {
        this.code = code;
        this.values = values;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DuplicatedPropertyValue> getValues() {
        return values;
    }

    public void setValues(List<DuplicatedPropertyValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "DuplicatedProperty{" +
                "code='" + code + '\'' +
                ", values=" + values +
                '}';
    }
}
