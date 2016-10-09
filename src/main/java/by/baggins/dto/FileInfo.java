package by.baggins.dto;

import java.util.Properties;

import javafx.beans.property.*;

public class FileInfo {
    private final DoubleProperty size;
    private final StringProperty name;
    private final StringProperty fileType;
    private final IntegerProperty keySetSize;
    private Properties properties;

    public FileInfo() {
        this(null, null, null, null, null);
    }

    public FileInfo(Double size, String name, String fileType) {
        this(size, name, fileType, null, null);
    }

    public FileInfo(Double size, String name, String fileType, Integer keySetSize, Properties properties) {
        this.size = new SimpleDoubleProperty(size);
        this.name = new SimpleStringProperty(name);
        this.fileType = new SimpleStringProperty(fileType);
        this.keySetSize = new SimpleIntegerProperty(keySetSize);
        this.properties = properties;
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

    public Integer getKeySetSize() {
        return keySetSize.get();
    }

    public IntegerProperty keySetSizeProperty() {
        return keySetSize;
    }

    public void setKeySetSize(int keySetSize) {
        this.keySetSize.set(keySetSize);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
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
