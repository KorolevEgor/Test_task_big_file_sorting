package com.korolyovEgor.TestTaskBigFileSorting.service.impl;

import com.korolyovEgor.TestTaskBigFileSorting.service.AbstractSortedFile;
import org.springframework.stereotype.Component;

@Component
public class SortedFileMerge extends AbstractSortedFile {

    @Override
    public void sort() {
        /*
        1. разделить файл на N файлов, в каждом из которых содержится равное количество строк, за исключением,
        возможно, последнего файла. O(FILE_LENGTH)
        2. отсортировать каждые из N файлов (все N файлов поочередно загружаются в ОЗУ). O((N log(N)) * FILE_LENGTH / N)
        3. имея N указателей слить результаты в 1 файл. O(FILE_LENGTH)

        Сложность по времени: O(FILE_LENGTH * log(N))
        Пространственная сложность: O(FILE_LENGTH)
         */
    }
}
