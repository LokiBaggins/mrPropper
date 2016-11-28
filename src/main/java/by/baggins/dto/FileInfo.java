package by.baggins.dto;

import java.util.Properties;

import javafx.beans.property.*;

public class FileInfo {
    private final StringProperty fileName;
    private final StringProperty fileType;
    private final IntegerProperty propertiesNumber;
    private final IntegerProperty duplicatesNumber;

    private Properties properties;
    private DuplicatesSearchResult duplicates;

    public FileInfo(String name, String fileType, Properties properties, DuplicatesSearchResult duplicates) {
        this.fileName = new SimpleStringProperty(name);
        this.fileType = new SimpleStringProperty(fileType);
        this.propertiesNumber = new SimpleIntegerProperty(properties.keySet().size());
        this.properties = properties;
        this.duplicates = duplicates;
        this.duplicatesNumber = new SimpleIntegerProperty(duplicates.getKeyDuplicates().size() + duplicates.getFullDuplicates().size());
    }


    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
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

    public int getPropertiesNumber() {
        return propertiesNumber.get();
    }

    public IntegerProperty propertiesNumberProperty() {
        return propertiesNumber;
    }

    public void setPropertiesNumber(int propertiesNumber) {
        this.propertiesNumber.set(propertiesNumber);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public DuplicatesSearchResult getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(DuplicatesSearchResult duplicates) {
        this.duplicates = duplicates;
    }

    public int getDuplicatesNumber() {
        return duplicatesNumber.get();
    }

    public IntegerProperty duplicatesNumberProperty() {
        return duplicatesNumber;
    }

    public void setDuplicatesNumber(int duplicatesNumber) {
        this.duplicatesNumber.set(duplicatesNumber);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName=" + fileName +
                ", fileType=" + fileType +
                ", propertiesNumber=" + propertiesNumber +
                ", properties=" + properties +
                ", duplicates=" + duplicates +
                '}';
    }
}
