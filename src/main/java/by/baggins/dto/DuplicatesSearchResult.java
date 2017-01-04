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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DuplicatesSearchResult)) return false;

        DuplicatesSearchResult that = (DuplicatesSearchResult) o;

        if (getKeyDuplicates() != null ? !getKeyDuplicates().equals(that.getKeyDuplicates()) : that.getKeyDuplicates() != null)
            return false;
        return getFullDuplicates() != null ? getFullDuplicates().equals(that.getFullDuplicates()) : that.getFullDuplicates() == null;

    }

    @Override
    public int hashCode() {
        int result = getKeyDuplicates() != null ? getKeyDuplicates().hashCode() : 0;
        result = 31 * result + (getFullDuplicates() != null ? getFullDuplicates().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DuplicatesSearchResult{" +
                "keyDuplicates=" + keyDuplicates +
                ", fullDuplicates=" + fullDuplicates +
                '}';
    }
}
