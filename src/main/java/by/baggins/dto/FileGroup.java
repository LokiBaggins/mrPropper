package by.baggins.dto;

import javafx.collections.ObservableList;

public class FileGroup {
    private String name;
    private ObservableList<FileInfo> files;

    public FileGroup() {
    }

    public FileGroup(String name, ObservableList<FileInfo> files) {
        this.name = name;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(ObservableList<FileInfo> files) {
        this.files = files;
    }
}
