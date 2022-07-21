package com.korolyovEgor.TestTaskBigFileSorting.service.impl;

import com.korolyovEgor.TestTaskBigFileSorting.service.AbstractSortedFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

@Slf4j
@Component
public class SortedFileBubble extends AbstractSortedFile {

    private long swapNearbyLinesIfRequired(RandomAccessFile raf, long firstLineStartPosition) throws IOException {
        raf.seek(firstLineStartPosition);
        String firstLine = raf.readLine();
        String secondLine = raf.readLine();

        System.out.println("firstLine: " + firstLine);
        System.out.println("secondLine: " + secondLine);

        if (firstLine == null || secondLine == null) {
            return raf.getFilePointer();
        }

        if (firstLine.compareTo(secondLine) <= 0) {
            firstLineStartPosition += firstLine.length() + 1;
            raf.seek(firstLineStartPosition);
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
        long secondLineStartPosition = raf.getFilePointer();

        raf.writeBytes(firstLine);
        // удаляем неиспользуемые символы
        for (int i = 0; i < maxLength - firstLine.length() - 1; ++i) {
            raf.write(' ');
        }
        raf.write('\n');

        firstLineStartPosition = secondLineStartPosition;
        raf.seek(firstLineStartPosition);
        return firstLineStartPosition;
    }

    @Override
    public void sort() {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (!isSorted()) {
                raf.seek(0);
                long positionLinePrev = 0L;

                while (raf.readLine() != null) {
                    positionLinePrev = swapNearbyLinesIfRequired(raf, positionLinePrev);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostConstruct
    public void postConstructMethod() {
        sort();
    }
}


/*

aaa
ccc
bbb
ddd
fff
eee
ggg

 */