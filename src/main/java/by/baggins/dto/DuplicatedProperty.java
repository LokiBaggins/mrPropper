package by.baggins.dto;


import java.util.List;

public class DuplicatedProperty {
    private String msgKey;
    private List<DuplicatedPropertyValue> values;

    public DuplicatedProperty() { }

    public DuplicatedProperty(String msgKey, List<DuplicatedPropertyValue> values) {
        this.msgKey = msgKey;
        this.values = values;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
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
                "msgKey='" + msgKey + '\'' +
                ", values=" + values +
                '}';
    }
}
