package by.baggins.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import by.baggins.dto.DuplicatedProperty;
import by.baggins.dto.DuplicatedPropertyValue;
import by.baggins.dto.DuplicatesSearchResult;


/**
 * Finds duplicates of properties keys in file and generates pair-list with values
 */
public class DuplicatedPropertiesFinder {

    public DuplicatesSearchResult checkFileForDuplicates(File file) {
        Map<String, List<DuplicatedPropertyValue>> unsortedDuplicates = parseFileForDuplicates(file);

        return sortDuplicatesByType(unsortedDuplicates, new DuplicatesSearchResult());
    }

    /**
     * Parses incoming .properties file line-by-line to find key duplicates.
     * @throws FileNotFoundException
     * @throws RuntimeException
     * */
    private static Map<String, List<DuplicatedPropertyValue>> parseFileForDuplicates(File file){
        Map<String, List<DuplicatedPropertyValue>> result = new HashMap<>();
        int rowNum = 1;
        String prop = "";

        try {
            Scanner sc = new Scanner(new FileInputStream(file), "UTF-8");
            String pCode = "";
            while(sc.hasNext()) {
                prop = sc.nextLine();

                if ((prop.indexOf('=') != -1) && (prop.indexOf('#') == -1)) {
                    pCode = prop.substring(0, prop.indexOf('='));
                    String pValue = prop.substring(prop.indexOf('=') + 1);
                    DuplicatedPropertyValue duplicatedPropertyValue = new DuplicatedPropertyValue(rowNum, pValue);
                    List<DuplicatedPropertyValue> duplicatedPropertyValues = new ArrayList<>();

                    if (result.keySet().contains(pCode)) {
                        result.get(pCode).add(duplicatedPropertyValue);
                    } else {
                        duplicatedPropertyValues.add(duplicatedPropertyValue);
                        result.put(pCode, duplicatedPropertyValues);
                    }
                }
                rowNum++;
            }

            return result;
        } catch (FileNotFoundException e) {
//            TODO: replace with some user exception
            e.printStackTrace();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error! Last parsed row "+ rowNum + ": " + prop);
        }
        return null;
    }

    /**
     * Splits unsorted duplicates Map into two Lists:
     *      keyDuplicates (equal keys, but different values) and
     *      fullDuplicates (both keys and value are equal)
     */
    private DuplicatesSearchResult sortDuplicatesByType(Map<String, List<DuplicatedPropertyValue>> propertiesMap, DuplicatesSearchResult result) {
        List<DuplicatedProperty> keyDuplicatesList = new ArrayList<>();
        List<DuplicatedProperty> fullDuplicatesList = new ArrayList<>();

        for (Map.Entry<String, List<DuplicatedPropertyValue>> entry : propertiesMap.entrySet()){
            if (entry.getValue().size() > 1){
                String propText = entry.getValue().get(0).getText();
                boolean isFullDuplicate = true;
                for (DuplicatedPropertyValue duplicatedPropertyValue : entry.getValue()) {
                    if(!propText.equals(duplicatedPropertyValue.getText())){
                        isFullDuplicate = false;
                        break;
                    }
                }

                DuplicatedProperty duplicatedProperty = new DuplicatedProperty(entry.getKey(), entry.getValue());
                if (isFullDuplicate) {
                    fullDuplicatesList.add(duplicatedProperty);
                } else {
                    keyDuplicatesList.add(duplicatedProperty);
                }
            }
        }

        result.setKeyDuplicates(keyDuplicatesList);
        result.setFullDuplicates(fullDuplicatesList);
        return result;
    }

}
