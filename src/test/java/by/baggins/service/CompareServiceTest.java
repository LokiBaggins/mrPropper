package by.baggins.service;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;
import by.baggins.dto.LocaleName;

import static org.junit.Assert.assertEquals;

public class CompareServiceTest {
    

    @Test
    public void testCompareProperties() throws Exception {
        Map<LocaleName, Properties> incomingBundle = getMockPropertiesMap();
        ComparisonSummary ethalon = getMockSummary();

        ComparisonSummary actual = new CompareService().compareProperties(incomingBundle);
        System.out.println(actual);
        assertEquals(ethalon, actual);
    }
    
    private Map<LocaleName, Properties> getMockPropertiesMap(){
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

        Map<LocaleName,Properties> result = new HashMap<>();
        result.put(LocaleName.RU, ru);
        result.put(LocaleName.EN, en);
        result.put(LocaleName.KK, kk);

        return result;
    }

    private ComparisonSummary getMockSummary(){
        ComparisonSummary result = new ComparisonSummary();

        Properties ru = result.translateIntoRU;
        ru.setProperty("p4", "4");
        ru.setProperty("p5", "5");

        Properties en = result.translateIntoEN;
        en.setProperty("p3", "3");
        en.setProperty("p5", "5");

        Properties kk = result.translateIntoKK;
        kk.setProperty("p4", "4");

        return result;
    }

}