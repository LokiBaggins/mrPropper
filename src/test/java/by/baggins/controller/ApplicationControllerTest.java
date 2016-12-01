package by.baggins.controller;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Properties;

import by.baggins.FileMocker;
import by.baggins.MocksDuplicatesSerachResults;
import by.baggins.dto.DuplicatesSearchResult;
import by.baggins.dto.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ApplicationControllerTest {
    private static File mockFolder = new File("src\\test\\resources\\mockFolder");
    private static FileMocker fileMocker = new FileMocker(mockFolder.getPath() + "\\");
    private static File validFile1 = new File("validFile_xx.properties");
    private static File validFile2 = new File("validFile_yy.properties");
    private static File validFile3 = new File("validFile_zz.properties");
    private static File fileWithDuplicatesMixed;
    private static File fileWithDuplicatesKeyOnly;
    private static File fileWithDuplicatesFullOnly;
    private static ObservableList<FileInfo> expectedResult;

    @BeforeClass
    public static void createMockFolder() {
        try {
            if (mockFolder.exists()) {
                FileUtils.deleteDirectory(mockFolder);
            }

            Files.createDirectories(mockFolder.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileMocker.createValidFile1(validFile1.getName());
        fileMocker.createValidFile2(validFile2.getName());
        fileMocker.createValidFile3(validFile3.getName());

        fileWithDuplicatesMixed = fileMocker.createFileWithDuplicatesMixed("duplicates_file_xx.properties");
        fileWithDuplicatesKeyOnly = fileMocker.createFileWithDuplicatesFullOnly("duplicates_file_yy.properties");
        fileWithDuplicatesFullOnly = fileMocker.createFileWithDuplicatesKeyOnly("duplicates_file_zz.properties");
        fileMocker.createNotPropertiesFiles("someTextFile.txt", "someFile.pdf", "someExeFile.exe"); // here you are welcome to set any filenames with extensions different of ".properties". Any of such files must be ignored by application

        expectedResult = getExcpectedAnalysisResult();
    }

    @Test
    public void testAnalyzeDirectoryFiles_filteringByExtension() throws Exception {
        ObservableList<FileInfo> actualFileInfoList = new ApplicationController().analyzeDirectoryFiles(mockFolder.getPath());


    }

    @AfterClass
    public static void removeMockFolderAndFiles() {
        try {
            FileUtils.deleteDirectory(mockFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ObservableList<FileInfo> getExcpectedAnalysisResult() {
        FileInfo validFile1Info = getFileInfo(validFile1);
        FileInfo validFile2Info = getFileInfo(validFile2);
        FileInfo validFile3Info = getFileInfo(validFile3);

        DuplicatesSearchResult duplicatesMixed = MocksDuplicatesSerachResults.getDuplicatesSearchResultMixed();
        DuplicatesSearchResult duplicatesKeyOnly = MocksDuplicatesSerachResults.getDuplicatesSearchResultKeyOnly();
        DuplicatesSearchResult duplicatesFullOnly = MocksDuplicatesSerachResults.getDuplicatesSearchResultFullOnly();
        FileInfo fileWithDuplicatesMixedFileInfo = getFileInfo(fileWithDuplicatesMixed, duplicatesMixed);
        FileInfo fileWithDuplicatesKeyOnlyFileInfo = getFileInfo(fileWithDuplicatesKeyOnly, duplicatesKeyOnly);
        FileInfo fileWithDuplicatesFullOnlyFileInfo = getFileInfo(fileWithDuplicatesFullOnly, duplicatesFullOnly);

        ObservableList<FileInfo> result = FXCollections.observableArrayList();
        result.addAll(validFile1Info, validFile2Info, validFile3Info, fileWithDuplicatesMixedFileInfo, fileWithDuplicatesKeyOnlyFileInfo, fileWithDuplicatesFullOnlyFileInfo);

        return result;
    }

    private static FileInfo getFileInfo(File validFile) {
        return getFileInfo(validFile, new DuplicatesSearchResult());
    }

    private static FileInfo getFileInfo(File file, DuplicatesSearchResult duplicates) {

        String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
        String fileType = file.getName().substring(file.getName().indexOf('.') + 1);
        Properties validFile1Props = getFilePropertiesUTF8(file);

        return new FileInfo(fileName, fileType, validFile1Props, duplicates);
    }

    private static Properties getFilePropertiesUTF8(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

}