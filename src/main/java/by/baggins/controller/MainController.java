package by.baggins.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import by.baggins.dto.ComparisonSummary;
import by.baggins.dto.DuplicatedProperty;
import by.baggins.dto.DuplicatedPropertyValue;
import by.baggins.dto.DuplicatesSearchResult;
import by.baggins.dto.FileInfo;
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

public class MainController {

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
    
    public void handleSelectBtn() {
        ObservableList<FileInfo> fileInfoList = analyzeDirectoryFiles();
        fileInfoTable.setItems(fileInfoList);

        Map<String, Properties> localeMapping = getFilePropertiesMapping(fileInfoList);
        CompareService comparator = new CompareServiceImpl();
        ComparisonSummary summary = comparator.compareProperties(localeMapping);

        resultsArea.clear();
        StringBuilder resultsText = new StringBuilder();
        for (String fileName : summary.getToBeTranslated().keySet()) {
            Set<Map.Entry<Object, Object>> props = summary.getToBeTranslated().get(fileName).entrySet();
            resultsText.append("\n" + fileName + "\nMissed translations: " + props.size() + ".\n\t");
            for (Map.Entry<Object, Object> prop : props) {
                resultsText.append(prop.getKey() + "=" + prop.getValue() + "\n\t");
            }
        }
        resultsArea.setText(resultsText.toString());

        System.out.println("ComparisonSummary: " + summary.getToBeTranslated().toString());


    }

    private ObservableList<FileInfo> analyzeDirectoryFiles() {
        String dirPath = propsDirectoryPath.getText();

        if (dirPath == null || dirPath.equals("")) {
//            TODO: throw user-readable exception and handle it
            System.out.println("Empty directory path");
            return null;
        }

        File dir = new File(dirPath);
        FilenameFilter propsFilter = (dir1, name) -> name.toLowerCase().endsWith(".properties");
        File[] propsFiles = dir.listFiles(propsFilter);

        if (propsFiles == null || propsFiles.length == 0){
//            TODO: throw user-readable exception and handle it
            System.out.println("No files found in dir '" + dir + "'");
            return null;
        }

        ObservableList<FileInfo> fileInfoList = FXCollections.observableArrayList();
        for (File propsFile : propsFiles) {
            String fileType = "";
            String fileName = propsFile.getName();

            if (propsFile.isFile()) {
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                fileType = propsFile.getName().substring(propsFile.getName().indexOf('.') + 1);
//                TODO: replace with logger
                System.out.println("File " + propsFile.getName());
            } else if (propsFile.isDirectory()) {
                fileType = "DIR";
//                TODO: replace with logger
                System.out.println("Directory " + fileName);
            }

            Properties fileProps = getFilePropertiesUTF8(propsFile);
            if (fileProps == null) {
                fileProps = new Properties();
            }

            DuplicatesSearchResult duplicates = duplicatesFinder.checkFileForDuplicates(propsFile);
//                TODO: replace with logger
            System.out.println("\tduplicates: " + duplicates);

            FileInfo fileInfo = new FileInfo(fileName, fileType, fileProps, duplicates);
            fileInfoList.add(fileInfo);
        }

        return fileInfoList;

    }

    private Properties getFilePropertiesUTF8(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
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
                fileDuplicatesArea.setText(prettyPrintFileDuplicates(fileDuplicates));
            }
        }
    }

    private Map<String, Properties> getFilePropertiesMapping(ObservableList<FileInfo> fileList){
        Map<String, Properties> result = new HashMap<>();

        for (FileInfo fileInfo : fileList) {
            result.put(fileInfo.getFileName(), fileInfo.getProperties());
        }
        return result;
    }

    private String prettyPrintFileDuplicates(DuplicatesSearchResult fileDuplicates) {
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

}
