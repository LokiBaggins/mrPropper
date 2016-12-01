package by.baggins.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;

public class CompareServiceImpl implements CompareService {

//    TODO: replace param with List<FileInfo>
    @Override
    public ComparisonSummary compareProperties(Map<String, Properties> incomingBundle) {
        //  TODO: remove sout
        Long startTime = new Date().getTime();

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

//                    TODO: refactor with
                    /* Escaping NullPointerException: initialize result Properties for each file while the first call */
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


//      TODO: remove sout
//      Горизонтальная логика сравнения свойств отработала на боевом бандле: "Elapsed time: 16"
        System.out.println("Elapsed time: " + (new Date().getTime() - startTime));

        return new ComparisonSummary(resultMap);
    }
}
