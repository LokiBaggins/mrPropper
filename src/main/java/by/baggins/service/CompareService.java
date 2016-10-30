package by.baggins.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;
import by.baggins.dto.LocaleName;

public class CompareService {


    public ComparisonSummary compareProperties(Map<LocaleName, Properties> incomingBundle) {
        ComparisonSummary result = new ComparisonSummary();

//TODO: replace with List<Properties> and find out how to bind it with proper file
        for (LocaleName localeName : incomingBundle.keySet()) {
            System.out.println("Comparing locale " + localeName.toString() + ":");
            Properties props = incomingBundle.get(localeName);
            if (props == null) {
//                System.out.println("No such file in bundle");
                continue;
            }

            Iterator<Map.Entry<Object, Object>> iterator = props.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> currentProp = iterator.next();
                String currKey = (String) currentProp.getKey();
                String currValue = (String) currentProp.getValue();
                
                for (LocaleName locale : incomingBundle.keySet()) {

//                    System.out.print("\t"+currKey+" vs " + locale +":");

                    if(localeName.equals(locale)){
                        iterator.remove();
//                        System.out.println(" removed");
                        continue;
                    }

                    Properties toBeTranslated = getStorageByLocaleName(locale, result);
                    if (toBeTranslated == null) {
//                        System.out.println(" skipped");
                        continue;
                    }

                    if (incomingBundle.get(locale).containsKey(currKey)) {
//                        System.out.println(" present. removed from " + locale);
                        incomingBundle.get(locale).remove(currKey);
                    } else {
                        toBeTranslated.setProperty(currKey, currValue);
//                        System.out.println(" added");
                    }
                }
            }
        }

        return result;
    }

    private Properties getStorageByLocaleName(LocaleName localeName, ComparisonSummary containerObject) {
        switch (localeName) {
            case RU: return containerObject.translateIntoRU;
            case KK: return containerObject.translateIntoKK;
            case EN: return containerObject.translateIntoEN;
            case DE: return containerObject.translateIntoDE;
            case FR: return containerObject.translateIntoFR;
            case TR: return containerObject.translateIntoTR;
        }

        return null;
    }
}
