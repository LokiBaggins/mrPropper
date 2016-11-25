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

public class DuplicatesController {

    @FXML private TextField propsDirectoryPath;

    @FXML private TableView<FileInfo> fileInfoTable;
    @FXML private TableColumn<FileInfo, String> fileNameColumn;
    @FXML private TableColumn<FileInfo, String> fileTypeColumn;
    @FXML private TableColumn<FileInfo, Double> fileSizeColumn;
    @FXML private TableColumn<FileInfo, Integer> fileKeySetColumn;

    @FXML private Label fileNameLabel;
    @FXML private Label fileSizeLabel;
    @FXML private Label fileKeySetLabel;

    @FXML private TextArea resultsArea;

    private DuplicatedPropertiesFinder duplicatesFinder = new DuplicatedPropertiesFinderImpl();

    @FXML
    private void initialize() {
//        fileNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FileInfo, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<FileInfo, String> cellData) {
//                return cellData.getValue().fileNameProperty();
//            }
//        });
        resultsArea.setWrapText(false);

        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
        fileTypeColumn.setCellValueFactory(cellData -> cellData.getValue().fileTypeProperty());
        fileKeySetColumn.setCellValueFactory(cellData -> cellData.getValue().propertiesNumberProperty().asObject());

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
//                TODO: remove sout
                System.out.println("File " + propsFile.getName());
            } else if (propsFile.isDirectory()) {
                fileType = "DIR";
//                TODO: remove sout
                System.out.println("Directory " + fileName);
            }

            Properties fileProps = getFilePropertiesUTF8(propsFile);
            if (fileProps == null) {
                fileProps = new Properties();
            }

            DuplicatesSearchResult duplicates = duplicatesFinder.checkFileForDuplicates(propsFile);
//                TODO: remove sout
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
        showFileDetails(fileInfo, null);
    }

    private void showFileDetails(FileInfo fileInfo, Properties props) {
        fileNameLabel.setText("---");
        fileKeySetLabel.setText("---");
        if (fileInfo != null) {
            fileNameLabel.setText(fileInfo.getFileName());
            fileKeySetLabel.setText(String.valueOf(fileInfo.getPropertiesNumber()));
        }
    }

    private Map<String, Properties> getFilePropertiesMapping(ObservableList<FileInfo> fileList){
        Map<String, Properties> result = new HashMap<>();

        for (FileInfo fileInfo : fileList) {
            result.put(fileInfo.getFileName(), fileInfo.getProperties());
        }
        return result;
    }

    private void printCodeDuplicatesSet(List<DuplicatedProperty> duplicatedProperties) {
        System.out.println("\n\n CODE duplicates list:");
        for (int i = 0; i < duplicatedProperties.size(); i++) {
            DuplicatedProperty duplicatedProperty = duplicatedProperties.get(i);
            System.out.print(i + ". " + duplicatedProperty.getMsgKey() + "\n");
            for (DuplicatedPropertyValue duplicatedPropertyValue : duplicatedProperty.getValues()) {
                System.out.println("\t  " + duplicatedPropertyValue);
            }
        }
    }

    private void printFullDuplicatesSet(List<DuplicatedProperty> duplicatedProperties) {
        System.out.println("\n\n FULL duplicates list:");
        for (int i = 0; i < duplicatedProperties.size(); i++) {
            DuplicatedProperty duplicatedProperty = duplicatedProperties.get(i);
            StringBuilder msg = new StringBuilder((i+1) + ". " + duplicatedProperty.getMsgKey() + "   Used in rows: ");

            for (DuplicatedPropertyValue duplicatedPropertyValue : duplicatedProperty.getValues()) {
                msg.append(duplicatedPropertyValue.getRowNum()).append("; ");
            }
            System.out.println(msg.toString());
        }
    }

}
