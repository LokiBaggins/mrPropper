package by.baggins.dto;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FileInfo {
    private final LongProperty size;
    private final StringProperty name;
    private final StringProperty fileType;

    public FileInfo() {
        this(null, null, null);
    }

    public FileInfo(Long size, String name, String fileType) {
        this.size = new SimpleLongProperty(size);
        this.name = new SimpleStringProperty(name);
        this.fileType = new SimpleStringProperty(fileType);
    }

    public Long getSize() {
        return size.get();
    }

    public LongProperty sizeProperty() {
        return size;
    }

    public void setSize(Long size) {
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
}
