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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DuplicatedPropertyValue)) return false;

        DuplicatedPropertyValue that = (DuplicatedPropertyValue) o;

        if (getRowNum() != that.getRowNum()) return false;
        return getText() != null ? getText().equals(that.getText()) : that.getText() == null;

    }

    @Override
    public int hashCode() {
        int result = getRowNum();
        result = 31 * result + (getText() != null ? getText().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return  "'" + text + "', row in file: " + rowNum + ";";
    }
}
