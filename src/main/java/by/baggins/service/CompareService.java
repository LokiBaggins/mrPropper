package by.baggins.service;

import java.util.Map;
import java.util.Properties;

import by.baggins.dto.LocaleName;
import by.baggins.dto.ComparisonSummary;

public class CompareService {


    public ComparisonSummary getPropertiesDifference(Map<LocaleName, Properties> incomingBundle) {
        ComparisonSummary result = new ComparisonSummary();

        for (LocaleName localeName : LocaleName.values()) {
            Properties props = incomingBundle.get(localeName);

            for (Object keyObj : props.keySet()) {
                String key = (String) keyObj;
//                checkKeyPresence(key, localeName, result);
                for (LocaleName locale : LocaleName.values()) {
                    if(localeName.equals(locale)){
                        props.remove(key);
                        continue;
                    }

                    Properties toBeTranslated = getStorageByLocaleName(locale, result);

                    if (!incomingBundle.get(locale).containsKey(key)){
                        toBeTranslated.setProperty(key, props.getProperty(key));
                        incomingBundle.get(locale).remove(key);
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


//    TODO test method


}
