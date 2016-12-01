package by.baggins.controller;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import by.baggins.FileMocker;

public class ApplicationControllerTest {
//    private static String mockFolderName = "src\\test\\resources\\mockFolder";
    private static File mockFolder = new File("src\\test\\resources\\mockFolder");
    private static FileMocker fileMocker = new FileMocker(mockFolder.getPath() + "\\");

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

        fileMocker.createValidFile1("validFile_xx.properties");
        fileMocker.createValidFile1("validFile_yy.properties");
        fileMocker.createValidFile1("validFile_zz.properties");
        fileMocker.createFileWithDuplicatesMixed("duplicates_file_xx.properties");
        fileMocker.createFileWithDuplicatesFullOnly("duplicates_file_yy.properties");
        fileMocker.createFileWithDuplicatesKeyOnly("duplicates_file_zz.properties");
        fileMocker.createNotPropertiesFiles("someTextFile.txt", "someFile.pdf", "someExeFile.exe");
    }

    @Test
    public void testAnalyzeDirectoryFiles() throws Exception {


    }

    @AfterClass
    public static void removeMockFolderAndFiles() {
        try {
            FileUtils.deleteDirectory(mockFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}