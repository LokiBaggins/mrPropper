package by.baggins.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import by.baggins.App;
import by.baggins.dto.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DuplicatesController {

    @FXML private TextField propsDirectoryPath;

    @FXML private TableView<FileInfo> fileInfoTable;
    @FXML private TableColumn<FileInfo, String> fileNameColumn;
    @FXML private TableColumn<FileInfo, String> fileTypeColumn;
    @FXML private TableColumn<FileInfo, Double> fileSizeColumn;
    @FXML private TableColumn<FileInfo, Integer> fileKeySetColumn;

    @FXML private Label fileInfoLabel;
    @FXML private Label fileNameLabel;
    @FXML private Label fileTypeLabel;
    @FXML private Label fileSizeLabel;
    @FXML private Label fileKeySetLabel;

    private App app;

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
        fileInfoTable.setItems(getDirectoryFiles());

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
                fileType = propsFile.getName().substring(propsFile.getName().indexOf('.') + 1);
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                System.out.println("File " + propsFile.getName());
            } else if (propsFile.isDirectory()) {
                fileType = "DIR";
                System.out.println("Directory " + fileName);
            }

            double fileSize = ((Long) propsFile.length()).doubleValue() / 1024;

            FileInfo fileInfo = new FileInfo((double) Math.round(fileSize * 100) / 100.0d, fileName, fileType, getFileKeySetLength(propsFile));
            fileInfoList.add(fileInfo);
        }

//        fileInfoList.forEach(System.out::println);
//        for (FileInfo fileInfo : fileInfoList) {
//            System.out.println(fileInfo);
//        }

        return fileInfoList;
    }

    private Integer getFileKeySetLength(File file){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//TODO replace logic of keys.length definition. Set trimms duplicates
        return properties.keySet().size();
    }

    public void setApp(App app) {
        this.app = app;
        fileInfoTable.setItems(app.getFileInfoList());
    }

    public void showFileDetails(FileInfo fileInfo) {
        fileInfoLabel.setText("File Info");
        fileNameLabel.setText("---");
        fileTypeLabel.setText("---");
        fileSizeLabel.setText("---");
        fileKeySetLabel.setText("---");
        if (fileInfo != null) {
            fileInfoLabel.setText(fileInfo.getName());
            fileNameLabel.setText(fileInfo.getName());
            fileTypeLabel.setText(fileInfo.getFileType());
            fileSizeLabel.setText(fileInfo.getSize().toString());
            fileKeySetLabel.setText(fileInfo.getKeySetSize().toString());
        }
    }



}
