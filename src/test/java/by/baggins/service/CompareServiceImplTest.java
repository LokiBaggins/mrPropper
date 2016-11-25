package by.baggins.service;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;

import static org.junit.Assert.assertEquals;

public class CompareServiceImplTest {

    @Test
    public void testCompareProperties() throws Exception {
        Map<String, Properties> incomingBundle = getMockPropertiesMap();
        ComparisonSummary ethalon = getMockSummary();
        ComparisonSummary actual = new CompareServiceImpl().compareProperties(incomingBundle);

        System.out.println(actual);
        assertEquals(ethalon, actual);

    }
    
    private Map<String, Properties> getMockPropertiesMap(){
        Properties ru = new Properties();
        ru.setProperty("p1", "один");
        ru.setProperty("p2", "два");
        ru.setProperty("p3", "три");

        Properties en = new Properties();
        en.setProperty("p1", "one");
        en.setProperty("p2", "two");
        en.setProperty("p4", "four");

        Properties kk = new Properties();
        kk.setProperty("p1", "а");
        kk.setProperty("p2", "екі");
        kk.setProperty("p3", "үш");
        kk.setProperty("p5", "бес");

        Map<String,Properties> result = new HashMap<>();
        result.put("file1", ru);
        result.put("file2", en);
        result.put("file3", kk);

        return result;
    }

    private ComparisonSummary getMockSummary(){
        Map<String, Properties> resultMap = new HashMap<>();

        Properties ru = new Properties();
        ru.setProperty("p4", "four");
        ru.setProperty("p5", "бес");

        Properties en = new Properties();
        en.setProperty("p3", "үш");
        en.setProperty("p5", "бес");

        Properties kk = new Properties();
        kk.setProperty("p4", "four");

        resultMap.put("file1",  ru);
        resultMap.put("file2",  en);
        resultMap.put("file3",  kk);

        return new ComparisonSummary(resultMap);
    }

}