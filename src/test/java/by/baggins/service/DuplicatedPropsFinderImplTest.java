package by.baggins.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import by.baggins.FileMocker;
import by.baggins.MocksDuplicatesSerachResults;
import by.baggins.dto.DuplicatesSearchResult;

import static by.baggins.MocksDuplicatesSerachResults.getDuplicatesSearchResultMixed;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DuplicatedPropsFinderImplTest {
    private DuplicatedPropertiesFinderImpl duplicator = new DuplicatedPropertiesFinderImpl();
    private static File fileWithDuplicatesMixed;
    private static File fileWithDuplicatesKeyOnly;
    private static File fileWithDuplicatesFullOnly;
    private static File fileWithoutDuplicates;
    private static FileMocker fileMocker = new FileMocker("src\\test\\resources\\");

    @BeforeClass
    public static void createMockFile() {
        fileWithDuplicatesMixed = fileMocker.createFileWithDuplicatesMixed("test_duplicates_mixed.properties");
        fileWithDuplicatesKeyOnly = fileMocker.createFileWithDuplicatesKeyOnly("test_duplicates_keyOnly.properties");
        fileWithDuplicatesFullOnly = fileMocker.createFileWithDuplicatesFullOnly("test_duplicates_fullOnly.properties");
        fileWithoutDuplicates = fileMocker.createValidFile1("test_no_duplicates.properties");

    }

    @Test
    public void testDuplicatesSearchResult_mixedDuplicates_keyDuplicates() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithDuplicatesMixed);
        DuplicatesSearchResult duplicatesExpected = MocksDuplicatesSerachResults.getDuplicatesSearchResultMixed();

        assertEquals(duplicatesExpected.getKeyDuplicates(), duplicatesActual.getKeyDuplicates());
    }

    @Test
    public void testDuplicatesSearchResult_mixedDuplicates_fullDuplicates() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithDuplicatesMixed);
        DuplicatesSearchResult duplicatesExpected = MocksDuplicatesSerachResults.getDuplicatesSearchResultMixed();

        assertEquals(duplicatesExpected.getFullDuplicates(), duplicatesActual.getFullDuplicates());
    }

    @Test
    public void testDuplicatesSearchResult_keyDuplicatesOnly() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithDuplicatesKeyOnly);
        DuplicatesSearchResult duplicatesExpected = MocksDuplicatesSerachResults.getDuplicatesSearchResultKeyOnly();

        assertEquals(duplicatesExpected, duplicatesActual);
    }

    @Test
    public void testDuplicatesSearchResult_fullDuplicatesOnly() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithDuplicatesFullOnly);
        DuplicatesSearchResult duplicatesExpected = MocksDuplicatesSerachResults.getDuplicatesSearchResultFullOnly();

        assertEquals(duplicatesExpected, duplicatesActual);
    }

    @Test
    public void testDuplicatesSearchResult_noDuplicates() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithoutDuplicates);

        assertThat(duplicatesActual.getKeyDuplicates(), hasSize(0));
        assertThat(duplicatesActual.getFullDuplicates(), hasSize(0));
    }

    @AfterClass
    public static void afterClass(){
        try {
            Files.delete(fileWithDuplicatesMixed.toPath());
            System.out.println("Mock file removed: " + fileWithDuplicatesMixed.getName());

            Files.delete(fileWithDuplicatesKeyOnly.toPath());
            System.out.println("Mock file removed: " + fileWithDuplicatesKeyOnly.getName());

            Files.delete(fileWithDuplicatesFullOnly.toPath());
            System.out.println("Mock file removed: " + fileWithDuplicatesFullOnly.getName());

            Files.delete(fileWithoutDuplicates.toPath());
            System.out.println("Mock file removed: " + fileWithoutDuplicates.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}