package by.baggins.dto;

import java.util.HashMap;
import java.util.Map;

public class DuplicatesSearchSummary {
    private Map<String, DuplicatesSearchResult> duplicatedProperties;

    public DuplicatesSearchSummary() {
        this.duplicatedProperties = new HashMap<>();
    }

    public DuplicatesSearchSummary(Map<String, DuplicatesSearchResult> duplicatedProperties) {
        this.duplicatedProperties = duplicatedProperties;
    }

    public Map<String, DuplicatesSearchResult> getDuplicatedProperties() {
        return duplicatedProperties;
    }

    public void setDuplicatedProperties(Map<String, DuplicatesSearchResult> duplicatedProperties) {
        this.duplicatedProperties = duplicatedProperties;
    }
}
