package com.korolyovEgor.TestTaskBigFileSorting;

import com.korolyovEgor.TestTaskBigFileSorting.fileGeneratorService.FileGeneratorService;
import com.korolyovEgor.TestTaskBigFileSorting.sortingService.SortedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateAndSort {

    public GenerateAndSort(@Autowired SortedService sortedService,
                           @Autowired FileGeneratorService fileGeneratorService) {
        fileGeneratorService.generate();
        sortedService.sort();
    }
}
