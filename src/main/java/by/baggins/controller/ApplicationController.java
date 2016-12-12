package by.baggins.controller;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import by.baggins.dto.ComparisonSummary;
import by.baggins.dto.DuplicatedProperty;
import by.baggins.dto.DuplicatedPropertyValue;
import by.baggins.dto.DuplicatesSearchResult;
import by.baggins.dto.FileGroup;
import by.baggins.dto.FileInfo;
import by.baggins.dto.FolderAnalysisResult;
import by.baggins.service.CompareService;
import by.baggins.service.CompareServiceImpl;
import by.baggins.service.DuplicatedPropertiesFinder;
import by.baggins.service.DuplicatedPropertiesFinderImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class ApplicationController {

    //    Directory picker block
    @FXML
    private TextField directoryPathInput;

    //    Directory info pane
    @FXML
    private TableView<FileInfo> fileInfoTable;
    @FXML
    private TableColumn<FileInfo, String> fileNameColumn;
    @FXML
    private TableColumn<FileInfo, String> fileTypeColumn;
    @FXML
    private TableColumn<FileInfo, Integer> propertiesNumberColumn;
    @FXML
    private TableColumn<FileInfo, Integer> duplicatesNumberColumn;

    //    File details pane
    @FXML
    private Label fileNameLabel;
    @FXML
    private Label propertiesNumberLabel;
    @FXML
    private Label duplicatesNumberLabel;
    @FXML
    private Label keyDuplicatesLabel;
    @FXML
    private Label fullDuplicatesLabel;
    @FXML
    private TextArea fileDuplicatesArea;

    //    Bundle comparison result block
    @FXML
    private TextArea resultsArea;

    private DuplicatedPropertiesFinder duplicatesFinder = new DuplicatedPropertiesFinderImpl();
    private CompareService comparator = new CompareServiceImpl();
    private FolderAnalysisResult folderAnalysisResult;

    @FXML
    private void initialize() {
        resultsArea.setWrapText(false);

        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
        fileTypeColumn.setCellValueFactory(cellData -> cellData.getValue().fileTypeProperty());
        propertiesNumberColumn.setCellValueFactory(cellData -> cellData.getValue().propertiesNumberProperty().asObject());
        duplicatesNumberColumn.setCellValueFactory(cellData -> cellData.getValue().duplicatesNumberProperty().asObject());

        showFileDetails(null);

        fileInfoTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFileDetails(newValue));
    }

    @FXML
    public void compareFilesInDirectory() {
        List<ComparisonSummary> groupSummaries = folderAnalysisResult.getFileGroups().stream()
                .map(fileGroup -> comparator.compareProperties(fileGroup))
                .collect(Collectors.toList());

        resultsArea.appendText(printGroupsComparisonSummaries(groupSummaries));
    }

    @FXML
    public void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Select bundle directory");
        directoryChooser.setInitialDirectory(new File("."));

        //Show open file dialog
        File dir = directoryChooser.showDialog(null);

        if (dir != null) {
            directoryPathInput.setText(dir.getPath());
            analyzeDirectoryFiles();
        }
    }


    public void analyzeDirectoryFiles() {
        String dirPath = directoryPathInput.getText();

        if (dirPath == null || dirPath.equals("")) {
//            TODO: throw user-readable exception and handle it
            throw new RuntimeException("Empty directory path");
        }

//        surround with try/catch
        File dir = new File(dirPath);
//        leaves files only
        FileFilter dirFilesFilter = File::isFile;
        File[] dirFiles = dir.listFiles(dirFilesFilter);

//        sorting files to .properties and ignored ones
        List<File> propsFiles = new ArrayList<>();
        List<String> ignoredFilesNames = new ArrayList<>();
        for (File file : dirFiles) {
            if (!file.getName().toLowerCase().endsWith(".properties") || !file.getName().matches("^.+_[\\w]{2,3}\\.properties$")) {
                ignoredFilesNames.add(file.getName());
            } else {
                propsFiles.add(file);
            }
        }

        System.out.println("Props files: " + propsFiles);
        System.out.println("Ignored files: " + ignoredFilesNames);

        if (propsFiles.isEmpty()) {
//            TODO: throw user-readable exception and handle it
            throw new RuntimeException("No \"..._XX.properties\" files found in dir '" + dir + "'");
        }

        ObservableList<FileGroup> fileGroups = groupFilesByNamePattern(propsFiles);
        folderAnalysisResult = new FolderAnalysisResult(dirPath, fileGroups, ignoredFilesNames);

        displayFolderAnalysisResult();
    }

    private void displayFolderAnalysisResult() {
        ObservableList<FileInfo> fileInfoList = FXCollections.observableArrayList();
        for (FileGroup fileGroup : folderAnalysisResult.getFileGroups()) {
            fileInfoList.addAll(fileGroup.getFiles());
        }
        fileInfoTable.setItems(fileInfoList);

        printIgnoredFilesList();

    }

    private void printIgnoredFilesList() {
        if (!folderAnalysisResult.getIgnoredFilesNames().isEmpty()) {
            String newLine = System.getProperty("line.separator");
            resultsArea.clear();
            StringBuilder ignoredFilesList = new StringBuilder("Files in list below are ignored" + newLine);

            for (String fileName : folderAnalysisResult.getIgnoredFilesNames()) {
                ignoredFilesList.append(fileName).append(newLine);
            }
            ignoredFilesList.append("==============================");

            resultsArea.setText(ignoredFilesList.toString());
        }
    }

    private ObservableList<FileGroup> groupFilesByNamePattern(List<File> files) {
        ObservableList<FileGroup> result = FXCollections.observableArrayList();
        ObservableList<FileInfo> groupFilesInfo = FXCollections.observableArrayList();
        File groupingFile = files.get(0);
        String shortFileName = groupingFile.getName().substring(0, groupingFile.getName().lastIndexOf("_") + 1);
        String fileNamePattern = "^" + shortFileName + "[a-zA-Z]{2}.properties";
        Iterator<File> iterator = files.iterator();

        while (iterator.hasNext()) {
            File file = iterator.next();

            if (file.getName().matches(fileNamePattern)) {
                FileInfo fileInfo = getFileInfo(file);
                groupFilesInfo.add(fileInfo);

                iterator.remove();
            }
        }
        result.add(new FileGroup(shortFileName, groupFilesInfo));

        if (files.size() > 0) {
            result.addAll(groupFilesByNamePattern(files));
        }

        return result;
    }

    private FileInfo getFileInfo(File propsFile) {
        String fileType = "";
        String fileName = propsFile.getName();

        if (propsFile.isFile()) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            fileType = propsFile.getName().substring(propsFile.getName().indexOf('.') + 1);
//                TODO: replace with logger
//            System.out.println("File " + propsFile.getName());
        }

        Properties fileProps = getFilePropertiesUTF8(propsFile);
        if (fileProps == null) {
            fileProps = new Properties();
        }

        DuplicatesSearchResult duplicates = duplicatesFinder.checkFileForDuplicates(propsFile);
//                TODO: replace with logger
//        System.out.println("\tduplicates: " + duplicates);

        return new FileInfo(fileName, fileType, fileProps, duplicates);
    }

    private Properties getFilePropertiesUTF8(File file) {

        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        ) {
            properties.load(inputStreamReader);
            return properties;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showFileDetails(FileInfo fileInfo) {
        fileNameLabel.setText("Choose a file for detail info");
        propertiesNumberLabel.setText("---");
        duplicatesNumberLabel.setText("---");
        keyDuplicatesLabel.setText("---");
        fullDuplicatesLabel.setText("---");
        fileDuplicatesArea.setText("Nothing to show");

        if (fileInfo != null) {
            fileNameLabel.setText(fileInfo.getFileName());
            propertiesNumberLabel.setText(String.valueOf(fileInfo.getPropertiesNumber()));
            duplicatesNumberLabel.setText(String.valueOf(fileInfo.getDuplicatesNumber()));

            DuplicatesSearchResult fileDuplicates = fileInfo.getDuplicates();
            if (fileDuplicates != null && fileDuplicates.getFullDuplicates() != null && fileDuplicates.getKeyDuplicates() != null) {
                keyDuplicatesLabel.setText(String.valueOf(fileDuplicates.getKeyDuplicates().size()));
                fullDuplicatesLabel.setText(String.valueOf(fileDuplicates.getFullDuplicates().size()));
                fileDuplicatesArea.setText(printFileDuplicates(fileDuplicates));
            }
        }
    }

    private String printFileDuplicates(DuplicatesSearchResult fileDuplicates) {
        String newLine = System.getProperty("line.separator");

        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("KEY duplicates list: ").append(printDuplicatesList(fileDuplicates.getKeyDuplicates()));
        resultBuilder.append(newLine).append("----------------------------------------").append(newLine);
        resultBuilder.append("FULL duplicates list: ").append(printDuplicatesList(fileDuplicates.getFullDuplicates()));

        return resultBuilder.toString();
    }

    private String printDuplicatesList(List<DuplicatedProperty> duplicatedProperties) {
        if (duplicatedProperties.isEmpty()) {
            return "empty.";
        }

        StringBuilder resultBuilder = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        int keyCounter = 1;

        for (DuplicatedProperty duplicatedProperty : duplicatedProperties) {
            resultBuilder.append(newLine).append(keyCounter++).append(". ").append(duplicatedProperty.getMsgKey()).append(newLine); // e.g. "\n1. some.key"

            for (DuplicatedPropertyValue duplicatedPropertyValue : duplicatedProperty.getValues()) {
                resultBuilder.append("\t").append(duplicatedPropertyValue).append(newLine); //e.g. "\t someValue1"
            }

            resultBuilder.append(newLine);
        }

        return resultBuilder.toString();
    }

    private String printGroupsComparisonSummaries(List<ComparisonSummary> summaryList) {
        StringBuilder resultsText = new StringBuilder();

        for (ComparisonSummary groupSummary : summaryList) {
            resultsText.append(printComparisonSummary(groupSummary));
            resultsText.append("\n=========================================\n");
        }

        return resultsText.toString();
    }

    private String printComparisonSummary(ComparisonSummary summary) {
        StringBuilder resultsText = new StringBuilder();

        for (String fileName : summary.getToBeTranslated().keySet()) {
            Set<Map.Entry<Object, Object>> props = summary.getToBeTranslated().get(fileName).entrySet();
            resultsText.append("\n" + fileName + "\nMissed translations: " + props.size() + ".\n\t");

            for (Map.Entry<Object, Object> prop : props) {
                resultsText.append(prop.getKey() + "=" + prop.getValue() + "\n\t");
            }
        }

        return resultsText.toString();
    }

}
