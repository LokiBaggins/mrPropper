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

}
