package com.korolyovEgor.TestTaskBigFileSorting.sortingService.impl;

import com.korolyovEgor.TestTaskBigFileSorting.sortingService.AbstractSortedFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SortedFileMerge extends AbstractSortedFile {

    private int totalLinesCount;

    private long filesCount;

    @Value("${fileGenerationService.lines-count}")
    private int linesCount;

    private final String tmpDir = "src/main/resources/tmp/";

//    @PostConstruct
//    public void postConstructMethod() {
//        sort();
//    }

    /*
        1. разделить файл на N файлов, в каждом из которых содержится равное количество строк, за исключением,
        возможно, последнего файла. O(FILE_LENGTH)
        2. отсортировать каждые из N файлов (все N файлов поочередно загружаются в ОЗУ). O((N log(N)) * FILE_LENGTH / N)
        3. имея N указателей слить результаты в 1 файл. O(FILE_LENGTH)

        Сложность по времени: O(FILE_LENGTH * log(N))
        Пространственная сложность: O(FILE_LENGTH)
    */
    @Override
    public void sort() {
        splitBigFileAndSortSmallFiles();
        mergeSortedFiles();
        removeTmpFiles();
    }

    private void removeTmpFiles() {
        for (int i = 0; i < filesCount; i++) {
            String filePath = tmpDir + (i+1) + ".txt";
            File tmpFile = new File(filePath);
            tmpFile.delete();
        }
    }

    private void mergeSortedFiles() {
        try (
                RandomAccessFile rafSorted = new RandomAccessFile(sortedFile, "rw")
        ) {
            List<RandomAccessFile> rafs = new ArrayList<>();
            // init random access files
            for (int i = 0; i < filesCount; i++) {
                String filePath = tmpDir + (i+1) + ".txt";
                rafs.add(new RandomAccessFile(filePath, "rw"));
            }

            // хранит по i-у индексу значение текущей строки rafs[i]
            List<String> linesFromRUFs = new ArrayList<>();
            for (RandomAccessFile raf : rafs) {
                linesFromRUFs.add(raf.readLine());
            }

            // поиск минимального значения
            while (!linesFromRUFs.isEmpty()) {
                String minValue = linesFromRUFs.get(0);
                int indOfMin = 0;
                for (int i = 1; i < linesFromRUFs.size(); i++) {
                    if (minValue.compareTo(linesFromRUFs.get(i)) > 0) {
                        minValue = linesFromRUFs.get(i);
                        indOfMin = i;
                    }
                }

                // запись в итоговый файл найденное минимальное значение и обновление строки в списках
                rafSorted.writeBytes(minValue + '\n');
                linesFromRUFs.set(indOfMin, rafs.get(indOfMin).readLine());

                // при достижении указателя на конец файла соответствующие элементы из linesFromRUFs и rafs удаляются
                for (int i = 0; i < linesFromRUFs.size(); i++) {
                    if (linesFromRUFs.get(i) == null) {
                        rafs.get(i).close();
                        rafs.remove(i);
                        linesFromRUFs.remove(i);
                    }
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int calcTotalLinesCount() {
        int result = 0;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            while (raf.readLine() != null) {
                ++result;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    private void splitBigFileAndSortSmallFiles() {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            totalLinesCount = calcTotalLinesCount();
            filesCount = (long) Math.ceil(((double) totalLinesCount) / linesCount);

            String lineRead;
            for (int i = 0; i < filesCount; i++) {
                List<String> subFileStorage = new ArrayList<>(linesCount);
                for (int j = 0; j < linesCount; j++) {
                    if ((lineRead = raf.readLine()) != null)
                        subFileStorage.add(lineRead);
                }
                subFileStorage.sort(String::compareTo);

                String filePath = tmpDir + (i+1) + ".txt";
                File tmpFile = new File(filePath);
                if (!tmpFile.createNewFile()) {
                    log.error("файл " + tmpFile.getPath() + tmpFile.getName() + " не удалось создать");
                }
                try (RandomAccessFile raSubFile = new RandomAccessFile(filePath, "rw")) {
                    for (String line : subFileStorage) {
                        raSubFile.writeBytes(line + '\n');
                    }
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
