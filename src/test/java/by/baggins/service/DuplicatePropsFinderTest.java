package by.baggins.service;

import org.junit.Test;

import java.io.File;

import by.baggins.dto.DuplicatesSearchResult;

public class DuplicatePropsFinderTest {
    private DuplicatedPropertiesFinder duplicator = new DuplicatedPropertiesFinder();
    String filePath ="D:\\Projects\\mrPropper\\src\\test\\resources\\test_duplicates_en.properties";
//    String filePath ="\\test_duplicates_en.properties";

    @Test
    public void testDuplicatesSearchResult() {
        DuplicatesSearchResult duplicates = duplicator.checkFileForDuplicates(new File(filePath));

        System.out.println(duplicates);
    }

}