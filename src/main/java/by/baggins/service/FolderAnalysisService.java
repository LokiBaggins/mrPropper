package by.baggins.service;

import by.baggins.dto.FolderAnalysisResult;

public interface FolderAnalysisService {
    DuplicatedPropertiesFinder duplicatesFinder = new DuplicatedPropertiesFinderImpl();

    FolderAnalysisResult analyzeDirectoryFiles(String dirPath);
}
