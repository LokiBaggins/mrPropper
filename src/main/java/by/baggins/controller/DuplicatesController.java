package by.baggins.controller;


import by.baggins.App;
import by.baggins.dto.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FilenameFilter;

public class DuplicatesController {

    @FXML private TextField propsDirectoryPath;

    @FXML private TableView<FileInfo> fileInfoTable;
    @FXML private TableColumn<FileInfo, String> fileNameColumn;
    @FXML private TableColumn<FileInfo, String> fileTypeColumn;
    @FXML private TableColumn<FileInfo, Double> fileSizeColumn;

    @FXML private Label fileNameLabel;
    @FXML private Label fileTypeLabel;
    @FXML private Label fileSizeLabel;

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
            if (propsFile.isFile()) {
                fileType = propsFile.getName().substring(propsFile.getName().indexOf('.') + 1);
                System.out.println("File " + propsFile.getName());
            } else if (propsFile.isDirectory()) {
                fileType = "DIR";
                System.out.println("Directory " + propsFile.getName());
            }

            double fileSize = ((Long) propsFile.length()).doubleValue() / 1024;

            FileInfo fileInfo = new FileInfo((double) Math.round(fileSize * 100) / 100.0d, propsFile.getName(), fileType);
            fileInfoList.add(fileInfo);
        }

//        fileInfoList.forEach(System.out::println);
//        for (FileInfo fileInfo : fileInfoList) {
//            System.out.println(fileInfo);
//        }

        return fileInfoList;
    }

    public void setApp(App app) {
        this.app = app;
        fileInfoTable.setItems(app.getFileInfoList());
    }

    public void showFileDetails(FileInfo fileInfo) {
        fileNameLabel.setText("---");
        fileTypeLabel.setText("---");
        fileSizeLabel.setText("---");
        if (fileInfo != null) {
            fileNameLabel.setText(fileInfo.getName());
            fileTypeLabel.setText(fileInfo.getFileType());
            fileSizeLabel.setText(fileInfo.getSize().toString());
        }
    }



}
