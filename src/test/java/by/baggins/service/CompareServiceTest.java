package by.baggins.service;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;

import static org.junit.Assert.assertEquals;

public class CompareServiceTest {

    @Test
    public void testCompareProperties() throws Exception {
        Map<String, Properties> incomingBundle = getMockPropertiesMap();
        ComparisonSummary ethalon = getMockSummary();

        ComparisonSummary actual = new CompareService().compareProperties(incomingBundle);
        System.out.println(actual);
        assertEquals(ethalon, actual);

    }
    
    private Map<String, Properties> getMockPropertiesMap(){
        Properties ru = new Properties();
        ru.setProperty("p1", "1");
        ru.setProperty("p2", "2");
        ru.setProperty("p3", "3");

        Properties en = new Properties();
        en.setProperty("p1", "1");
        en.setProperty("p2", "2");
        en.setProperty("p4", "4");

        Properties kk = new Properties();
        kk.setProperty("p1", "1");
        kk.setProperty("p2", "2");
        kk.setProperty("p3", "3");
        kk.setProperty("p5", "5");

        Map<String,Properties> result = new HashMap<>();
        result.put("file1", ru);
        result.put("file2", en);
        result.put("file3", kk);

        return result;
    }

    private ComparisonSummary getMockSummary(){
        Map<String, Properties> resultMap = new HashMap<>();

        Properties ru = new Properties();
        ru.setProperty("p4", "4");
        ru.setProperty("p5", "5");

        Properties en = new Properties();
        en.setProperty("p3", "3");
        en.setProperty("p5", "5");

        Properties kk = new Properties();
        kk.setProperty("p4", "4");

        resultMap.put("file1",  ru);
        resultMap.put("file2",  en);
        resultMap.put("file3",  kk);

        return new ComparisonSummary(resultMap);
    }

}