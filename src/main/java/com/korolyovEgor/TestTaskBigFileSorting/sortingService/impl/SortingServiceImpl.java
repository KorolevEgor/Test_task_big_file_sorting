package com.korolyovEgor.TestTaskBigFileSorting.sortingService.impl;

import com.korolyovEgor.TestTaskBigFileSorting.sortingService.SortedFile;
import com.korolyovEgor.TestTaskBigFileSorting.sortingService.SortedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class SortingServiceImpl implements SortedService {

    private final SortedFile sortedFile;

    SortingServiceImpl(
            @Qualifier("sortedFileMerge")
            @Autowired SortedFile sortedFile) {
        this.sortedFile = sortedFile;
    }

    @PostConstruct
    public void postConstructMethod() {
        sort();
    }

    @Override
    public void sort() {
        sortedFile.sort();
    }
}
