package by.baggins.dto;

import javafx.beans.property.*;

public class FileInfo {
    private final DoubleProperty size;
    private final StringProperty name;
    private final StringProperty fileType;
    private final IntegerProperty keySet;

    public FileInfo() {
        this(null, null, null, null);
    }

    public FileInfo(Double size, String name, String fileType) {
        this(size, name, fileType, null);
    }

    public FileInfo(Double size, String name, String fileType, Integer keySet) {
        this.size = new SimpleDoubleProperty(size);
        this.name = new SimpleStringProperty(name);
        this.fileType = new SimpleStringProperty(fileType);
        this.keySet = new SimpleIntegerProperty(keySet);
    }

    public Double getSize() {
        return size.get();
    }

    public DoubleProperty sizeProperty() {
        return size;
    }

    public void setSize(Double size) {
        this.size.set(size);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getFileType() {
        return fileType.get();
    }

    public StringProperty fileTypeProperty() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType.set(fileType);
    }

    public int getKeySet() {
        return keySet.get();
    }

    public IntegerProperty keySetProperty() {
        return keySet;
    }

    public void setKeySet(int keySet) {
        this.keySet.set(keySet);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "size=" + size +
                ", name=" + name +
                ", fileType=" + fileType +
                '}';
    }
}
