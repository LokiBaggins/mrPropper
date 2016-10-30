package by.baggins.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;

public class CompareService {

    public ComparisonSummary compareProperties(Map<String, Properties> incomingBundle) {
//        ComparisonSummary result = new ComparisonSummary();
        Map<String, Properties> resultMap = new HashMap<>();

        for (String fileName : incomingBundle.keySet()) {
            Properties props = incomingBundle.get(fileName);
            if (props == null) {
                continue;
            }

            Iterator<Map.Entry<Object, Object>> iterator = props.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> currentProp = iterator.next();
                String currKey = (String) currentProp.getKey();
                String currValue = (String) currentProp.getValue();

                for (String comparingFileName : incomingBundle.keySet()) {

                    if(fileName.equals(comparingFileName)){
                        iterator.remove();
                        continue;
                    }

//                    Properties toBeTranslated = result.getToBeTranslated().get(comparingFileName);
                    Properties toBeTranslated = resultMap.get(comparingFileName);
                    if (toBeTranslated == null) {
                        toBeTranslated = new Properties();
                        resultMap.put(comparingFileName, toBeTranslated);
                    }

                    if (incomingBundle.get(comparingFileName).containsKey(currKey)) {
                        incomingBundle.get(comparingFileName).remove(currKey);
                    } else {
                        toBeTranslated.setProperty(currKey, currValue);
                    }
                }
            }
        }

        return new ComparisonSummary(resultMap);
    }
}
