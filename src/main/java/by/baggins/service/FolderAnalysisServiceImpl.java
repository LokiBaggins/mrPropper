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

        return new FolderAnalysisResult(dirPath, fileGroups, ignoredFilesNames);
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
}
