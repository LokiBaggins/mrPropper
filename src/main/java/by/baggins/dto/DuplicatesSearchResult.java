package by.baggins.dto;

import java.util.ArrayList;
import java.util.List;

public class DuplicatesSearchResult {
    List<DuplicatedProperty> keyDuplicates;
    List<DuplicatedProperty> fullDuplicates;

    public DuplicatesSearchResult() {
        this.keyDuplicates = new ArrayList<>();
        this.fullDuplicates = new ArrayList<>();
    }

    public DuplicatesSearchResult(List<DuplicatedProperty> keyDuplicates, List<DuplicatedProperty> fullDuplicates) {
        this.keyDuplicates = keyDuplicates;
        this.fullDuplicates = fullDuplicates;
    }

    public List<DuplicatedProperty> getKeyDuplicates() {
        return keyDuplicates;
    }

    public void setKeyDuplicates(List<DuplicatedProperty> keyDuplicates) {
        this.keyDuplicates = keyDuplicates;
    }

    public List<DuplicatedProperty> getFullDuplicates() {
        return fullDuplicates;
    }

    public void setFullDuplicates(List<DuplicatedProperty> fullDuplicates) {
        this.fullDuplicates = fullDuplicates;
    }
}
