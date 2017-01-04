package by.baggins.service;

import java.util.Map;
import java.util.Properties;

import by.baggins.dto.ComparisonSummary;
import by.baggins.dto.FileGroup;

public interface CompareService {
    ComparisonSummary compareProperties(FileGroup fileGroup);
    ComparisonSummary compareProperties(Map<String, Properties> incomingBundle);
}
