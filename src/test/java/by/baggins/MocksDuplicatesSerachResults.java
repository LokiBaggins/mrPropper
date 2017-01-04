package by.baggins;

import java.util.ArrayList;
import java.util.List;

import by.baggins.dto.DuplicatedProperty;
import by.baggins.dto.DuplicatedPropertyValue;
import by.baggins.dto.DuplicatesSearchResult;

public class MocksDuplicatesSerachResults {

    public static DuplicatesSearchResult getDuplicatesSearchResultMixed() {

        List<DuplicatedProperty> keyDups = new ArrayList<>();
        List<DuplicatedProperty> fullDups = new ArrayList<>();

//        1st key duplicate
        List<DuplicatedPropertyValue> keyDupValues1 =  new ArrayList<>();
        keyDupValues1.add(new DuplicatedPropertyValue(1, "one"));
        keyDupValues1.add(new DuplicatedPropertyValue(2, "раз"));
        keyDupValues1.add(new DuplicatedPropertyValue(8, "ещё раз"));
        DuplicatedProperty keyDup1 = new DuplicatedProperty("p1", keyDupValues1);
        keyDups.add(keyDup1);

//        1st full duplicate
        List<DuplicatedPropertyValue> fullDupValues1 =  new ArrayList<>();
        fullDupValues1.add(new DuplicatedPropertyValue(3, "two"));
        fullDupValues1.add(new DuplicatedPropertyValue(4, "two"));
        DuplicatedProperty fullDup1 = new DuplicatedProperty("p2", fullDupValues1);
        fullDups.add(fullDup1);

//        2nd full duplicate
        List<DuplicatedPropertyValue> fullDupValues2 =  new ArrayList<>();
        fullDupValues2.add(new DuplicatedPropertyValue(6, "four"));
        fullDupValues2.add(new DuplicatedPropertyValue(7, "four"));
        DuplicatedProperty fullDup2 = new DuplicatedProperty("p4", fullDupValues2);
        fullDups.add(fullDup2);

        return new DuplicatesSearchResult(keyDups, fullDups);
    }

    public static DuplicatesSearchResult getDuplicatesSearchResultKeyOnly() {
        DuplicatesSearchResult result = new DuplicatesSearchResult();

        List<DuplicatedProperty> keyDups = new ArrayList<>();

//        1st key duplicate
        List<DuplicatedPropertyValue> keyDupValues1 =  new ArrayList<>();
        keyDupValues1.add(new DuplicatedPropertyValue(1, "one"));
        keyDupValues1.add(new DuplicatedPropertyValue(2, "раз"));
        DuplicatedProperty keyDup1 = new DuplicatedProperty("p1", keyDupValues1);
        keyDups.add(keyDup1);

//        2nd key duplicate
        List<DuplicatedPropertyValue> keyDupValues2 =  new ArrayList<>();
        keyDupValues2.add(new DuplicatedPropertyValue(5, "four"));
        keyDupValues2.add(new DuplicatedPropertyValue(6, "четыре"));
        keyDupValues2.add(new DuplicatedPropertyValue(7, "ещё четыре"));
        DuplicatedProperty keyDup2 = new DuplicatedProperty("p4", keyDupValues2);
        keyDups.add(keyDup2);

        result.setKeyDuplicates(keyDups);
        return result;
    }

    public static DuplicatesSearchResult getDuplicatesSearchResultFullOnly() {
        DuplicatesSearchResult result = new DuplicatesSearchResult();

        List<DuplicatedProperty> fullDups = new ArrayList<>();

//        1st full duplicate
        List<DuplicatedPropertyValue> fullDupValues1 =  new ArrayList<>();
        fullDupValues1.add(new DuplicatedPropertyValue(1, "one"));
        fullDupValues1.add(new DuplicatedPropertyValue(2, "one"));
        fullDupValues1.add(new DuplicatedPropertyValue(7, "one"));
        DuplicatedProperty fullDup1 = new DuplicatedProperty("p1", fullDupValues1);
        fullDups.add(fullDup1);

//        2nd full duplicate
        List<DuplicatedPropertyValue> fullDupValues2 =  new ArrayList<>();
        fullDupValues2.add(new DuplicatedPropertyValue(5, "four"));
        fullDupValues2.add(new DuplicatedPropertyValue(6, "four"));
        DuplicatedProperty fullDup2 = new DuplicatedProperty("p4", fullDupValues2);
        fullDups.add(fullDup2);

        result.setFullDuplicates(fullDups);
        return result;
    }
}
