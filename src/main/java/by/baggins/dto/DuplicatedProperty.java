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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DuplicatedProperty)) return false;

        DuplicatedProperty that = (DuplicatedProperty) o;

        if (getMsgKey() != null ? !getMsgKey().equals(that.getMsgKey()) : that.getMsgKey() != null)
            return false;
        if (getValues() != null ? !getValues().equals(that.getValues()) : that.getValues() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getMsgKey() != null ? getMsgKey().hashCode() : 0;
        result = 31 * result + (getValues() != null ? getValues().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DuplicatedProperty{" +
                "msgKey='" + msgKey + '\'' +
                ", values=" + values +
                '}';
    }
}
