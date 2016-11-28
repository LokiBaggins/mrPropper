package by.baggins.service;

import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;

public interface CompareService {
    ComparisonSummary compareProperties(Map<String, Properties> incomingBundle);
}
