package com.korolyovEgor.TestTaskBigFileSorting.sortingService.impl;

import com.korolyovEgor.TestTaskBigFileSorting.sortingService.AbstractSortedFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class SortedFileBubble extends AbstractSortedFile {

//    @PostConstruct
//    public void postConstructMethod() {
//        sort();
//    }

    // меняет местами последовательно расположенные строки, если вторая строка лексикографически больше первой
    // первая строка имеет позицию firstLineStartPosition
    private long swapNearbyLinesIfRequired(RandomAccessFile raf, long firstLineStartPosition) throws IOException {
        raf.seek(firstLineStartPosition);
        String firstLine = raf.readLine();
        if ("".equals(firstLine))
            firstLine = raf.readLine();
        String secondLine = raf.readLine();
        long secondLineStartPosition = raf.getFilePointer();

        if (firstLine == null || secondLine == null) {
            return raf.getFilePointer();
        }

        if (firstLine.compareTo(secondLine) <= 0) {
            firstLineStartPosition = raf.getFilePointer() - secondLine.length() - 1;
            return firstLineStartPosition;
        }

        int maxLength = Math.max(firstLine.length(), secondLine.length());
        raf.seek(firstLineStartPosition);
        raf.writeBytes(secondLine);

        // удаляем неиспользуемые символы
        for (int i = 0; i < maxLength - secondLine.length() - 1; ++i) {
            raf.write(' ');
        }
        raf.write('\n');
        secondLineStartPosition = raf.getFilePointer();

        raf.writeBytes(firstLine);
        // удаляем неиспользуемые символы
        for (int i = 0; i < maxLength - firstLine.length() - 1; ++i) {
            raf.write(' ');
        }
        raf.write('\n');

        firstLineStartPosition = secondLineStartPosition;
        return firstLineStartPosition;
    }

    @Override
    public void sort() {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (!isSorted()) {
                raf.seek(0);
                long linePrevPosition = 0L;

                while (raf.readLine() != null) {
                    linePrevPosition = swapNearbyLinesIfRequired(raf, linePrevPosition);
                    raf.seek(linePrevPosition);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
