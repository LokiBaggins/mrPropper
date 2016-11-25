package by.baggins.dto;

public class DuplicatedPropertyValue {
    private int rowNum;
    private String text;

    public DuplicatedPropertyValue() {
    }

    public DuplicatedPropertyValue(int rowNum, String text) {
        this.rowNum = rowNum;
        this.text = text;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return rowNum + ":  " + text;
    }
}
