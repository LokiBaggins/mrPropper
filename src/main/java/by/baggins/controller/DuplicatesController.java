package by.baggins.controller;


import by.baggins.dto.FileInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DuplicatesController {

    @FXML private TextField propsDirectoryPath;

    public void getDirectoryFiles(ActionEvent event) {
        String dirPath = propsDirectoryPath.getText();
        if (dirPath == null || dirPath.equals("")) {
            return;
        }
        File dir = new File(dirPath);
        FilenameFilter propsFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".properties");
            }
        };
        File[] propsFiles = dir.listFiles();
//        File[] propsFiles = dir.listFiles(propsFilter);

        if (propsFiles == null || propsFiles.length == 0){
            System.out.println("No files founs in dir '" + dir + "'");
        }
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
        for (int i = 0; i < propsFiles.length; i++) {
            String fileType = "";
            if (propsFiles[i].isFile()) {
                fileType = propsFiles[i].getName().substring(propsFiles[i].getName().indexOf('.') +1);
                System.out.println("File " + propsFiles[i].getName());
            } else if (propsFiles[i].isDirectory()) {
                fileType = "DIR";
                System.out.println("Directory " + propsFiles[i].getName());
            }

            FileInfo fileInfo = new FileInfo(propsFiles[i].length() / 1024, propsFiles[i].getName(), fileType);
            fileInfoList.add(fileInfo);
        }
        System.out.println(fileInfoList);
    }
}
