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

public class ApplicationController {

//    Directory picker block
    @FXML private TextField propsDirectoryPath;

//    Directory info pane
    @FXML private TableView<FileInfo> fileInfoTable;
    @FXML private TableColumn<FileInfo, String> fileNameColumn;
    @FXML private TableColumn<FileInfo, String> fileTypeColumn;
    @FXML private TableColumn<FileInfo, Integer> propertiesNumberColumn;
    @FXML private TableColumn<FileInfo, Integer> duplicatesNumberColumn;

//    File details pane
    @FXML private Label fileNameLabel;
    @FXML private Label propertiesNumberLabel;
    @FXML private Label duplicatesNumberLabel;
    @FXML private Label keyDuplicatesLabel;
    @FXML private Label fullDuplicatesLabel;
    @FXML private TextArea fileDuplicatesArea;

//    Bundle comparison result block
    @FXML private TextArea resultsArea;

    private DuplicatedPropertiesFinder duplicatesFinder = new DuplicatedPropertiesFinderImpl();
    private CompareService comparator = new CompareServiceImpl();

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
    
    public void compareFilesInDirectory() {
        FolderAnalysisResult analysisResult = analyzeDirectoryFiles(propsDirectoryPath.getText());

        ObservableList<FileInfo> fileInfoList = FXCollections.observableArrayList();
        for (FileGroup fileGroup : analysisResult.getFileGroups()) {
            fileInfoList.addAll(fileGroup.getFiles());
        }
        fileInfoTable.setItems(fileInfoList);

//        Map<String, Properties> fileMapping = getFilePropertiesMapping(fileInfoList);
//        ComparisonSummary summary = comparator.compareProperties(fileMapping);

//        resultsArea.clear();
//        resultsArea.setText(printComparisonSummary(summary));
//        System.out.println("ComparisonSummary: " + summary.getToBeTranslated().toString());


        List<ComparisonSummary> groupSummaries = new ArrayList<>();
        for (FileGroup fileGroup : analysisResult.getFileGroups()) {
            groupSummaries.add(comparator.compareProperties(fileGroup));
        }

        resultsArea.clear();
        resultsArea.setText(printGroupsComparisonSummaries(groupSummaries));
    }

    public FolderAnalysisResult analyzeDirectoryFiles(String dirPath) {

        if (dirPath == null || dirPath.equals("")) {
//            TODO: throw user-readable exception and handle it
            System.out.println("Empty directory path");
            return null;
        }

        File dir = new File(dirPath);
//        leaves files only
        FileFilter dirFilesFilter = File::isFile;
        File[] dirFiles = dir.listFiles(dirFilesFilter);

//        sorting files to .properties and ignored ones
        List<File> propsFiles = new ArrayList<>();
        List<File> ignoredFiles = new ArrayList<>();
        for (File file : dirFiles) {
            if (!file.getName().toLowerCase().endsWith(".properties") || !file.getName().matches("_\\w\\w\\.properties$")) {
                ignoredFiles.add(file);
            }

            propsFiles.add(file);
        }

        if (propsFiles.isEmpty()){
//            TODO: throw user-readable exception and handle it
            System.out.println("No \"..._XX.properties\" files found in dir '" + dir + "'");
            return null;
        }

        ObservableList<FileGroup> fileGroups = groupFilesByNamePattern(propsFiles);

        return new FolderAnalysisResult(dirPath, fileGroups);

//        TODO: collect ignored files names to separate list
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

        System.out.println("groupFilesByNamePattern:" + result);

        return result;
    }

    private FileInfo getFileInfo(File propsFile) {
        String fileType = "";
        String fileName = propsFile.getName();

        if (propsFile.isFile()) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            fileType = propsFile.getName().substring(propsFile.getName().indexOf('.') + 1);
//                TODO: replace with logger
            System.out.println("File " + propsFile.getName());
        }

        Properties fileProps = getFilePropertiesUTF8(propsFile);
        if (fileProps == null) {
            fileProps = new Properties();
        }

        DuplicatesSearchResult duplicates = duplicatesFinder.checkFileForDuplicates(propsFile);
//                TODO: replace with logger
        System.out.println("\tduplicates: " + duplicates);

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
