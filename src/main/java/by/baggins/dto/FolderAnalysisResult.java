package by.baggins.dto;

import java.util.List;

import javafx.collections.ObservableList;

public class FolderAnalysisResult {
    private String folderPath;
    private ObservableList<FileGroup> fileGroups;
    private List<String> ignoredFilesNames;

    public FolderAnalysisResult(String folderPath, ObservableList<FileGroup> fileGroups, List<String> ignoredFilesNames) {
        this.folderPath = folderPath;
        this.fileGroups = fileGroups;
        this.ignoredFilesNames = ignoredFilesNames;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public ObservableList<FileGroup> getFileGroups() {
        return fileGroups;
    }

    public void setFileGroups(ObservableList<FileGroup> fileGroups) {
        this.fileGroups = fileGroups;
    }

    public List<String> getIgnoredFilesNames() {
        return ignoredFilesNames;
    }

    public void setIgnoredFilesNames(List<String> ignoredFilesNames) {
        this.ignoredFilesNames = ignoredFilesNames;
    }
}
