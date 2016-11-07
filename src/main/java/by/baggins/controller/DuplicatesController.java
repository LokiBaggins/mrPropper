package by.baggins.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import by.baggins.dto.ComparisonSummary;
import by.baggins.dto.FileInfo;
import by.baggins.service.CompareService;
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

//    @FXML private TableView<Map.Entry<String, String>> resultsTable;
//    @FXML private TableColumn<Map.Entry<String, String>, String> resultsRowNumberColumn;
//    @FXML private TableColumn<Map.Entry<String, String>, String> resultsKeyColumn;
//    @FXML private TableColumn<Map.Entry<String, String>, String> resultsValueColumn;

    @FXML private TextArea resultsArea;

    @FXML
    private void initialize() {
//        fileNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FileInfo, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<FileInfo, String> cellData) {
//                return cellData.getValue().nameProperty();
//            }
//        });

        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fileTypeColumn.setCellValueFactory(cellData -> cellData.getValue().fileTypeProperty());
        fileSizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty().asObject());
        fileKeySetColumn.setCellValueFactory(cellData -> cellData.getValue().keySetSizeProperty().asObject());

        showFileDetails(null);

        fileInfoTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showFileDetails(newValue));
    }
    
    public void handleSelectBtn() {
        ObservableList<FileInfo> fileInfoList = getDirectoryFiles();
        fileInfoTable.setItems(fileInfoList);

//        Map localeMapping = getLocalePropertyMapping(fileInfoList);
        Map<String, Properties> localeMapping = getFilePropertiesMapping(fileInfoList);
        ComparisonSummary summary = new CompareService().compareProperties(localeMapping);

        resultsArea.clear();
        for (String fileName : summary.getToBeTranslated().keySet()) {
            Set<Map.Entry<Object, Object>> props = summary.getToBeTranslated().get(fileName).entrySet();
            resultsArea.appendText("\n" + fileName + "\nMissed translations: " + props.size() + ".\n\t");
            for (Map.Entry<Object, Object> prop : props) {
                resultsArea.appendText(prop.getKey() + "=" + prop.getValue() + "\n\t");
            }
        }

        System.out.println("ComparisonSummary: " + summary.getToBeTranslated().toString());


    }

    public ObservableList<FileInfo> getDirectoryFiles() {
        String dirPath = propsDirectoryPath.getText();
        if (dirPath == null || dirPath.equals("")) {
//            TODO: throw and exception and handle it
            return null;
        }
        File dir = new File(dirPath);
        FilenameFilter propsFilter = (dir1, name) -> name.toLowerCase().endsWith(".properties");
//        FilenameFilter propsFilter = new FilenameFilter() {
//            public boolean accept(File dir, String name) {
//                return name.toLowerCase().endsWith(".properties");
//            }
//        };
//        File[] propsFiles = dir.listFiles();
        File[] propsFiles = dir.listFiles(propsFilter);

        if (propsFiles == null || propsFiles.length == 0){
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
                System.out.println("File " + propsFile.getName());
            } else if (propsFile.isDirectory()) {
                fileType = "DIR";
                System.out.println("Directory " + fileName);
            }

            double fileSize = ((Long) propsFile.length()).doubleValue() / 1024;
            Properties fileProps = getFileProperties(propsFile);
            int propsNumber = (fileProps != null) ? fileProps.keySet().size() : 0;

            FileInfo fileInfo = new FileInfo((double) Math.round(fileSize * 100) / 100.0d, fileName, fileType, propsNumber, fileProps);
            fileInfoList.add(fileInfo);
        }

        return fileInfoList;
    }

    public void showFileDetails(FileInfo fileInfo) {
        showFileDetails(fileInfo, null);
    }

    public void showFileDetails(FileInfo fileInfo, Properties props) {
        fileNameLabel.setText("---");
        fileSizeLabel.setText("---");
        fileKeySetLabel.setText("---");
        if (fileInfo != null) {
            fileNameLabel.setText(fileInfo.getName());
            fileSizeLabel.setText(fileInfo.getSize().toString());
            fileKeySetLabel.setText(fileInfo.getKeySetSize().toString());
        }
    }



    private Properties getFileProperties(File file){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    private Map<String, Properties> getFilePropertiesMapping(ObservableList<FileInfo> fileList){
        Map<String, Properties> result = new HashMap<>();

        for (FileInfo fileInfo : fileList) {
            result.put(fileInfo.getName(), fileInfo.getProperties());
        }
        return result;
    }

}
