package com.korolyovEgor.TestTaskBigFileSorting.fileGeneratorService.impl;

import com.korolyovEgor.TestTaskBigFileSorting.fileGeneratorService.FileGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Slf4j
@Service
public class FileGeneratorServiceImpl implements FileGeneratorService {

    @Value("${fileGenerationService.file}")
    private String file;

    @Value("${fileGenerationService.max-length-lines}")
    private int maxLengthLines;

    @Value("${fileGenerationService.lines-count}")
    private int linesCount;

    @PostConstruct
    private void postConstructMethod() {
        generate();
    }

    @Override
    public void generate() {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            for (int i = 0; i < linesCount; i++) {
                raf.writeBytes(genRandomString(maxLengthLines) + '\n');
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String genRandomString(int maxLine) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = new Random().nextInt(maxLine) + 1;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
}
