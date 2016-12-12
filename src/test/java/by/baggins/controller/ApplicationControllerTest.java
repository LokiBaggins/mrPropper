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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import by.baggins.FileMocker;
import by.baggins.MocksDuplicatesSerachResults;
import by.baggins.dto.DuplicatesSearchResult;
import by.baggins.dto.FileGroup;
import by.baggins.dto.FileInfo;
import by.baggins.dto.FolderAnalysisResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApplicationControllerTest {
    private static String validFilesNamePattern = "validFile";
    private static String duplicatedFilesNamePattern = "duplicates_file";
    private static File mockFolder = new File("src\\test\\resources\\mockFolder");
    private static FileMocker fileMocker = new FileMocker(mockFolder.getPath() + "\\");
    private static File validFile1;
    private static File validFile2;
    private static File validFile3;
    private static File fileWithDuplicatesMixed;
    private static File fileWithDuplicatesKeyOnly;
    private static File fileWithDuplicatesFullOnly;

    private static File lonePropertiesFile;

//    private static ObservableList<FileInfo> expectedFileInfoList;
    private static FolderAnalysisResult expectedAnalysisResult;
    private static FolderAnalysisResult actualResult;

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

        validFile1 = fileMocker.createValidFile1("validFile_xx.properties");
        validFile2 = fileMocker.createValidFile2("validFile_yy.properties");
        validFile3 = fileMocker.createValidFile3("validFile_zz.properties");
        fileWithDuplicatesMixed = fileMocker.createFileWithDuplicatesMixed("duplicates_file_xx.properties");
        fileWithDuplicatesKeyOnly = fileMocker.createFileWithDuplicatesFullOnly("duplicates_file_yy.properties");
        fileWithDuplicatesFullOnly = fileMocker.createFileWithDuplicatesKeyOnly("duplicates_file_zz.properties");
        lonePropertiesFile = fileMocker.createValidFile3("lonelyValidFile_aa.properties");
        fileMocker.createNotPropertiesFiles("someTextFile.txt", "someFile.pdf", "someExeFile.exe"); // here you are welcome to set any filenames with extensions different of ".properties". Any of such files must be ignored by application

        expectedAnalysisResult = getExpectedAnalysisResult();
//        actualResult = new ApplicationController().analyzeDirectoryFiles(mockFolder.getPath());
    }

    @Test
    public void testAnalyzeDirectoryFiles_filteringFilesByExtension() {
        assertEquals(expectedAnalysisResult.getFolderPath(), actualResult.getFolderPath());

//        List<String> actualFileTypes = actualResult.getFileGroups().stream().map(FileGroup::getFiles).map(FileInfo::getFileType).collect(Collectors.toList());
//        TODO: replace with lambda
        for (FileGroup group : actualResult.getFileGroups()) {
            for (FileInfo file : group.getFiles()) {
                assertEquals("properties", file.getFileType());
            }
        }
    }

//    @Test
    public void testAnalyzeDirectoryFiles_groupingFilesByNamePattern() {
        assertEquals(expectedAnalysisResult.getFileGroups().size(), actualResult.getFileGroups().size());

        for (FileGroup group : actualResult.getFileGroups()) {
            String groupFileNamePattern = "^" + group.getName() + "_[a-zA-Z]{2}$";
            for (FileInfo file : group.getFiles()) {
                assertTrue(file.getFileName().matches(groupFileNamePattern));
            }
        }
    }


    @Test
    public void java8lambdasTest() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        int  counter = 0;
        Long start = new Date().getTime();
        for (Integer li : list) {
            if (li % 2 == 0) {
                System.out.print(li);
                counter++;
            }
        }
        Long stop = new Date().getTime() - start;
        System.out.println("\nclassic counted " + counter + " evens for " + stop + "ms");

        counter = 0;
        start = new Date().getTime();
        long count = list.stream().filter(li -> li % 2 == 0).count();
        stop = new Date().getTime() - start;
        System.out.println("\nlambda counted " + count + " evens for " + stop + "ms");

    }

    @AfterClass
    public static void removeMockFolderAndFiles() {
        try {
            FileUtils.deleteDirectory(mockFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FolderAnalysisResult getExpectedAnalysisResult() {
        FileInfo validFile1Info = getFileInfo(validFile1);
        FileInfo validFile2Info = getFileInfo(validFile2);
        FileInfo validFile3Info = getFileInfo(validFile3);

        DuplicatesSearchResult duplicatesMixed = MocksDuplicatesSerachResults.getDuplicatesSearchResultMixed();
        DuplicatesSearchResult duplicatesKeyOnly = MocksDuplicatesSerachResults.getDuplicatesSearchResultKeyOnly();
        DuplicatesSearchResult duplicatesFullOnly = MocksDuplicatesSerachResults.getDuplicatesSearchResultFullOnly();
        FileInfo fileWithDuplicatesMixedFileInfo = getFileInfo(fileWithDuplicatesMixed, duplicatesMixed);
        FileInfo fileWithDuplicatesKeyOnlyFileInfo = getFileInfo(fileWithDuplicatesKeyOnly, duplicatesKeyOnly);
        FileInfo fileWithDuplicatesFullOnlyFileInfo = getFileInfo(fileWithDuplicatesFullOnly, duplicatesFullOnly);

        ObservableList<FileInfo> group1files = FXCollections.observableArrayList();
        group1files.addAll(validFile1Info, validFile2Info, validFile3Info);
        FileGroup group1 = new FileGroup(validFilesNamePattern, group1files);

        ObservableList<FileInfo> group2files = FXCollections.observableArrayList();
        group2files.addAll(fileWithDuplicatesMixedFileInfo, fileWithDuplicatesKeyOnlyFileInfo, fileWithDuplicatesFullOnlyFileInfo);
        FileGroup group2 = new FileGroup(duplicatedFilesNamePattern, group2files);

        ObservableList<FileGroup> fileGroups = FXCollections.observableArrayList();
        fileGroups.addAll(group1, group2);

        List<String> ignoredFiles = new ArrayList<>(Arrays.asList("someTextFile.txt", "someFile.pdf", "someExeFile.exe"));

        FolderAnalysisResult result = new FolderAnalysisResult(mockFolder.getPath(), fileGroups, ignoredFiles);
        return result;
    }

    private static FileInfo getFileInfo(File validFile) {
        return getFileInfo(validFile, new DuplicatesSearchResult());
    }

    private static FileInfo getFileInfo(File file, DuplicatesSearchResult duplicates) {

        String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
        String fileType = file.getName().substring(file.getName().indexOf('.') + 1);
        Properties fileProps = getFilePropertiesUTF8(file);

        return new FileInfo(fileName, fileType, fileProps, duplicates);
    }

    private static Properties getFilePropertiesUTF8(File file) {
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