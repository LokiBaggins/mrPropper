package by.baggins.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import by.baggins.FileMocker;
import by.baggins.dto.DuplicatedProperty;
import by.baggins.dto.DuplicatedPropertyValue;
import by.baggins.dto.DuplicatesSearchResult;

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
        DuplicatesSearchResult duplicatesExpected = getDuplicatesSearchResultMixed();

        assertEquals(duplicatesExpected.getKeyDuplicates(), duplicatesActual.getKeyDuplicates());
    }

    @Test
    public void testDuplicatesSearchResult_mixedDuplicates_fullDuplicates() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithDuplicatesMixed);
        DuplicatesSearchResult duplicatesExpected = getDuplicatesSearchResultMixed();

        assertEquals(duplicatesExpected.getFullDuplicates(), duplicatesActual.getFullDuplicates());
    }

    @Test
    public void testDuplicatesSearchResult_keyDuplicatesOnly() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithDuplicatesKeyOnly);
        DuplicatesSearchResult duplicatesExpected = getDuplicatesSearchResultKeyOnly();

        assertEquals(duplicatesExpected, duplicatesActual);
    }

    @Test
    public void testDuplicatesSearchResult_fullDuplicatesOnly() {
        DuplicatesSearchResult duplicatesActual = duplicator.checkFileForDuplicates(fileWithDuplicatesFullOnly);
        DuplicatesSearchResult duplicatesExpected = getDuplicatesSearchResultFullOnly();

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

    private DuplicatesSearchResult getDuplicatesSearchResultMixed() {

        List<DuplicatedProperty> keyDups = new ArrayList<>();
        List<DuplicatedProperty> fullDups = new ArrayList<>();

//        1st key duplicate
        List<DuplicatedPropertyValue> keyDupValues1 =  new ArrayList<>();
        keyDupValues1.add(new DuplicatedPropertyValue(1, "one"));
        keyDupValues1.add(new DuplicatedPropertyValue(2, "раз"));
        keyDupValues1.add(new DuplicatedPropertyValue(8, "ещё раз"));
        DuplicatedProperty keyDup1 = new DuplicatedProperty("p1", keyDupValues1);
        keyDups.add(keyDup1);

//        1st full duplicate
        List<DuplicatedPropertyValue> fullDupValues1 =  new ArrayList<>();
        fullDupValues1.add(new DuplicatedPropertyValue(3, "two"));
        fullDupValues1.add(new DuplicatedPropertyValue(4, "two"));
        DuplicatedProperty fullDup1 = new DuplicatedProperty("p2", fullDupValues1);
        fullDups.add(fullDup1);

//        2nd full duplicate
        List<DuplicatedPropertyValue> fullDupValues2 =  new ArrayList<>();
        fullDupValues2.add(new DuplicatedPropertyValue(6, "four"));
        fullDupValues2.add(new DuplicatedPropertyValue(7, "four"));
        DuplicatedProperty fullDup2 = new DuplicatedProperty("p4", fullDupValues2);
        fullDups.add(fullDup2);

        return new DuplicatesSearchResult(keyDups, fullDups);
    }

    private DuplicatesSearchResult getDuplicatesSearchResultKeyOnly() {
        DuplicatesSearchResult result = new DuplicatesSearchResult();

        List<DuplicatedProperty> keyDups = new ArrayList<>();

//        1st key duplicate
        List<DuplicatedPropertyValue> keyDupValues1 =  new ArrayList<>();
        keyDupValues1.add(new DuplicatedPropertyValue(1, "one"));
        keyDupValues1.add(new DuplicatedPropertyValue(2, "раз"));
        DuplicatedProperty keyDup1 = new DuplicatedProperty("p1", keyDupValues1);
        keyDups.add(keyDup1);

//        2nd key duplicate
        List<DuplicatedPropertyValue> keyDupValues2 =  new ArrayList<>();
        keyDupValues2.add(new DuplicatedPropertyValue(5, "four"));
        keyDupValues2.add(new DuplicatedPropertyValue(6, "четыре"));
        keyDupValues2.add(new DuplicatedPropertyValue(7, "ещё четыре"));
        DuplicatedProperty keyDup2 = new DuplicatedProperty("p4", keyDupValues2);
        keyDups.add(keyDup2);

        result.setKeyDuplicates(keyDups);
        return result;
    }

    private DuplicatesSearchResult getDuplicatesSearchResultFullOnly() {
        DuplicatesSearchResult result = new DuplicatesSearchResult();

        List<DuplicatedProperty> fullDups = new ArrayList<>();

//        1st full duplicate
        List<DuplicatedPropertyValue> fullDupValues1 =  new ArrayList<>();
        fullDupValues1.add(new DuplicatedPropertyValue(1, "one"));
        fullDupValues1.add(new DuplicatedPropertyValue(2, "one"));
        fullDupValues1.add(new DuplicatedPropertyValue(7, "one"));
        DuplicatedProperty fullDup1 = new DuplicatedProperty("p1", fullDupValues1);
        fullDups.add(fullDup1);

//        2nd full duplicate
        List<DuplicatedPropertyValue> fullDupValues2 =  new ArrayList<>();
        fullDupValues2.add(new DuplicatedPropertyValue(5, "four"));
        fullDupValues2.add(new DuplicatedPropertyValue(6, "four"));
        DuplicatedProperty fullDup2 = new DuplicatedProperty("p4", fullDupValues2);
        fullDups.add(fullDup2);

        result.setFullDuplicates(fullDups);
        return result;
    }
}