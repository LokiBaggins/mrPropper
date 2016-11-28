package by.baggins.service;

import java.io.File;

import by.baggins.dto.DuplicatesSearchResult;

public interface DuplicatedPropertiesFinder {
    DuplicatesSearchResult checkFileForDuplicates(File file);
}
