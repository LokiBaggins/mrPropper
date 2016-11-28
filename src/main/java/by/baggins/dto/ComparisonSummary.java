package by.baggins.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ComparisonSummary {
    private Map<String, Properties> toBeTranslated;

    public ComparisonSummary() {
        this.toBeTranslated = new HashMap<>();
    }

    public ComparisonSummary(Map<String, Properties> toBeTranslated) {
        this.toBeTranslated = toBeTranslated;
    }

    public Map<String, Properties> getToBeTranslated() {
        return toBeTranslated;
    }

    public void setToBeTranslated(Map<String, Properties> toBeTranslated) {
        this.toBeTranslated = toBeTranslated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComparisonSummary that = (ComparisonSummary) o;

        return toBeTranslated != null ? toBeTranslated.equals(that.toBeTranslated) : that.toBeTranslated == null;

    }

    @Override
    public int hashCode() {
        return toBeTranslated != null ? toBeTranslated.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ComparisonSummary{" +
                "toBeTranslated=" + toBeTranslated +
                '}';
    }
}
