package com.korolyovEgor.TestTaskBigFileSorting.sortingService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

@Slf4j
public abstract class AbstractSortedFile implements SortedFile {

    @Value("${sortedService.file}")
    protected String file;

    @Value("${sortedService.sorted-file}")
    protected String sortedFile;

    public abstract void sort();

    @Override
    public boolean isSorted() {
        try(
                RandomAccessFile raf = new RandomAccessFile(file, "r");
        ) {
            String line = raf.readLine();
            String linePrev = line;

            // файл пуст
            if (line == null) {
                return true;
            }

            boolean flag = true;
            System.out.println('|' + line + '|');
            while ((line = raf.readLine()) != null) {
                System.out.println('|' + line + '|');
                if (linePrev.compareTo(line) > 0) {
                    flag = false;
//                    return false;
                }
                linePrev = line;
            }
            return flag;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
