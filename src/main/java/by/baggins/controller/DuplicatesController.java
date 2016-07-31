package by.baggins.controller;


import java.io.File;
import java.io.FilenameFilter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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

        for (int i = 0; i < propsFiles.length; i++) {
            if (propsFiles[i].isFile()) {
                System.out.println("File " + propsFiles[i].getName());
            } else if (propsFiles[i].isDirectory()) {
                System.out.println("Directory " + propsFiles[i].getName());
            }
        }
    }
}
