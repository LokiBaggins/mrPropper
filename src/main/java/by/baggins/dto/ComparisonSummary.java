package by.baggins.dto;

import java.util.Properties;

/**
 * Contains sets of properties for each locale that are missed in corresponding locale, but presented in one or more other.
 */
public class ComparisonSummary {
    public Properties translateIntoRU;
    public Properties translateIntoKK;
    public Properties translateIntoEN;
    public Properties translateIntoDE;
    public Properties translateIntoFR;
    public Properties translateIntoTR;

    public ComparisonSummary() {
        this.translateIntoRU = new Properties();
        this.translateIntoDE = new Properties();
        this.translateIntoEN = new Properties();
        this.translateIntoFR = new Properties();
        this.translateIntoKK = new Properties();
        this.translateIntoTR = new Properties();
    }

    public ComparisonSummary(Properties translateIntoRU, Properties translateIntoDE, Properties translateIntoEN, Properties translateIntoFR, Properties translateIntoKK, Properties translateIntoTR) {
        this.translateIntoRU = translateIntoRU;
        this.translateIntoDE = translateIntoDE;
        this.translateIntoEN = translateIntoEN;
        this.translateIntoFR = translateIntoFR;
        this.translateIntoKK = translateIntoKK;
        this.translateIntoTR = translateIntoTR;
    }

    @Override
    public String toString() {
        return "ComparisonSummary{" +
                "translateIntoDE=" + translateIntoDE.keySet().toString() +
                ", translateIntoRU=" + translateIntoRU.keySet().toString() +
                ", translateIntoKK=" + translateIntoKK.keySet().toString() +
                ", translateIntoEN=" + translateIntoEN.keySet().toString() +
                ", translateIntoFR=" + translateIntoFR.keySet().toString() +
                ", translateIntoTR=" + translateIntoTR.keySet().toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComparisonSummary that = (ComparisonSummary) o;

        if (translateIntoRU != null ? !translateIntoRU.equals(that.translateIntoRU) : that.translateIntoRU != null)
            return false;
        if (translateIntoKK != null ? !translateIntoKK.equals(that.translateIntoKK) : that.translateIntoKK != null)
            return false;
        if (translateIntoEN != null ? !translateIntoEN.equals(that.translateIntoEN) : that.translateIntoEN != null)
            return false;
        if (translateIntoDE != null ? !translateIntoDE.equals(that.translateIntoDE) : that.translateIntoDE != null)
            return false;
        if (translateIntoFR != null ? !translateIntoFR.equals(that.translateIntoFR) : that.translateIntoFR != null)
            return false;
        return translateIntoTR != null ? translateIntoTR.equals(that.translateIntoTR) : that.translateIntoTR == null;

    }

    @Override
    public int hashCode() {
        int result = translateIntoRU != null ? translateIntoRU.hashCode() : 0;
        result = 31 * result + (translateIntoKK != null ? translateIntoKK.hashCode() : 0);
        result = 31 * result + (translateIntoEN != null ? translateIntoEN.hashCode() : 0);
        result = 31 * result + (translateIntoDE != null ? translateIntoDE.hashCode() : 0);
        result = 31 * result + (translateIntoFR != null ? translateIntoFR.hashCode() : 0);
        result = 31 * result + (translateIntoTR != null ? translateIntoTR.hashCode() : 0);
        return result;
    }
}
