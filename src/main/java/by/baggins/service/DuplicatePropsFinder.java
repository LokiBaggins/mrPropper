package by.baggins.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import by.baggins.dto.PropertySet;
import by.baggins.dto.PropertyValue;


/**
 * Finds duplicates of properties keys in file and generates pair-list with values
 */
public class DuplicatePropsFinder {


////    TODO: move to controller method
//    public static void main(String[] args) {
//        String locale = "de";
//        String propertiesFile = "D:\\Projects\\smartbank\\web-client\\src\\main\\resources\\messages\\messages_"+locale+".properties";
//
//        List<PropertySet> keyDuplicates = new ArrayList<>();
//        List<PropertySet> fullDuplicates = new ArrayList<>();
//
//        try {
//            Map<String, List<PropertyValue>> propertiesMap = parsePropertiesFile(propertiesFile);
////            TODO: join propertiesMap+keyDuplicates+fullDuplicates into new DTO
//            sortDuplicatesMap(propertiesMap, keyDuplicates, fullDuplicates);
//
//            System.out.println("Full duplicates found: " + fullDuplicates.size());
//            System.out.println("Code duplicates found: " + keyDuplicates.size());
//
//            printPropertySet(keyDuplicates, "CODE");
//            printPropertySet(fullDuplicates, "FULL");
//
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Parses incoming .properties file line-by-line to find key duplicates.
     * @throws FileNotFoundException
     * @throws RuntimeException
     * */
    private static Map<String, List<PropertyValue>> parsePropertiesFile(String filePath){
        Map<String, List<PropertyValue>> result = new HashMap<>();
        int rowNum = 1;
        String prop = "";

        try {
            Scanner sc = new Scanner(new FileInputStream(filePath), "UTF-8");
            String pCode = "";
            while(sc.hasNext()) {
                prop = sc.nextLine();

                if ((prop.indexOf('=') != -1) && (prop.indexOf('#') == -1)) {
                    pCode = prop.substring(0, prop.indexOf('='));
                    String pValue = prop.substring(prop.indexOf('=') + 1);
                    PropertyValue propertyValue = new PropertyValue(rowNum, pValue);
                    List<PropertyValue> propertyValues = new ArrayList<>();

                    if (result.keySet().contains(pCode)) {
                        result.get(pCode).add(propertyValue);
                    } else {
                        propertyValues.add(propertyValue);
                        result.put(pCode, propertyValues);
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
     * Splits unsorted duplicates Map into two Lists: keyDuplicates (equal keys, but different values) and full duplicates (both keys and value are equal)
     */
    private static void sortDuplicatesMap(Map<String, List<PropertyValue>> propertiesMap, List<PropertySet> keyDuplicatesList, List<PropertySet> fullDuplicatesList) {
        keyDuplicatesList.clear();
        fullDuplicatesList.clear();

        for (Map.Entry<String, List<PropertyValue>> entry : propertiesMap.entrySet()){
            if (entry.getValue().size() > 1){
                String propText = entry.getValue().get(0).getText();
                boolean isFullDuplicate = true;
                for (PropertyValue propertyValue : entry.getValue()) {
                    if(!propText.equals(propertyValue.getText())){
                        isFullDuplicate = false;
                        break;
                    }
                }

                PropertySet propertySet = new PropertySet(entry.getKey(), entry.getValue());
                if (isFullDuplicate) {
                    fullDuplicatesList.add(propertySet);
                } else {
                    keyDuplicatesList.add(propertySet);
                }
            }
        }
    }

    private static void printPropertySet (List<PropertySet> propertySet, String duplicatesType) {
        if (propertySet.isEmpty()) {
            return;
        }
        if (duplicatesType.equals("FULL")){
            printFullDuplicatesSet(propertySet);
            return;
        }

        printCodeDuplicatesSet(propertySet);
    }

    private static void printCodeDuplicatesSet(List<PropertySet> propertySets) {
        System.out.println("\n\n CODE duplicates list:");
        for (int i = 0; i < propertySets.size(); i++) {
            PropertySet propertySet = propertySets.get(i);
            System.out.print(i + ". " + propertySet.getCode() + "\n");
            for (PropertyValue propertyValue : propertySet.getValues()) {
                System.out.println("\t  " + propertyValue);
            }
        }
    }

    private static void printFullDuplicatesSet(List<PropertySet> propertySets) {
        System.out.println("\n\n FULL duplicates list:");
        for (int i = 0; i < propertySets.size(); i++) {
            PropertySet propertySet = propertySets.get(i);
            StringBuilder msg = new StringBuilder((i+1) + ". " + propertySet.getCode() + "   Used in rows: ");

            for (PropertyValue propertyValue : propertySet.getValues()) {
                msg.append(propertyValue.getRowNum()).append("; ");
            }
            System.out.println(msg.toString());
        }
    }
}
