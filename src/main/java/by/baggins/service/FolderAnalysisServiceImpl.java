package by.baggins.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import by.baggins.dto.DuplicatesSearchResult;
import by.baggins.dto.FileGroup;
import by.baggins.dto.FileInfo;
import by.baggins.dto.FolderAnalysisResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FolderAnalysisServiceImpl implements FolderAnalysisService {

    @Override
    public FolderAnalysisResult analyzeDirectoryFiles(String dirPath) {
        if (dirPath == null || dirPath.equals("")) {
            throw new RuntimeException("Empty directory path");
        }
//        surround with try/catch
        File dir = new File(dirPath);
//        leaves files only
        FileFilter dirFilesFilter = File::isFile;
        File[] dirFiles = dir.listFiles(dirFilesFilter);

        if (dirFiles == null) {
            throw new RuntimeException("Can't scan files from directory '" + dir.getAbsolutePath() + "'");
        }

//        sorting files to .properties and ignored ones
        List<File> propsFiles = new ArrayList<>();
        List<String> ignoredFilesNames = new ArrayList<>();
        for (File file : dirFiles) {
            if (!file.getName().toLowerCase().endsWith(".properties")) {
                ignoredFilesNames.add(file.getName());
            } else {
                propsFiles.add(file);
            }
        }

        System.out.println("Props files: " + propsFiles);
        System.out.println("Ignored files: " + ignoredFilesNames);

        if (propsFiles.isEmpty()) {
            throw new RuntimeException("No \"....properties\" files found in dir '" + dir + "'");
        }

        ObservableList<FileGroup> fileGroups = groupFilesByNamePattern(propsFiles);

        return new FolderAnalysisResult(dirPath, fileGroups, ignoredFilesNames);
    }

    private ObservableList<FileGroup> groupFilesByNamePattern(List<File> files) {
        ObservableList<FileGroup> result = FXCollections.observableArrayList();
        ObservableList<FileInfo> groupFilesInfo = FXCollections.observableArrayList();
        File groupingFile = files.get(0);
        String shortFileName = getShortFileName(groupingFile);
        String fileNamePattern = "^" + shortFileName + "(_[a-zA-Z]{2})*.properties";
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

    private String getShortFileName(File file) {
        String fileName = file.getName();
        String shortFileName = fileName.substring(0, fileName.lastIndexOf("."));

        if (fileName.contains("_")) {
            shortFileName = fileName.substring(0, fileName.lastIndexOf("_"));
        }

        return shortFileName;
    }

    private FileInfo getFileInfo(File propsFile) {
        String fileType = "";
        String fileName = propsFile.getName();

        if (propsFile.isFile()) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            fileType = propsFile.getName().substring(propsFile.getName().indexOf('.') + 1);
        }

        Properties fileProps = getFilePropertiesUTF8(propsFile);
        if (fileProps == null) {
            fileProps = new Properties();
        }

        DuplicatesSearchResult duplicates = duplicatesFinder.checkFileForDuplicates(propsFile);

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
}
