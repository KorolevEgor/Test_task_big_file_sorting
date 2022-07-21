package com.korolyovEgor.TestTaskBigFileSorting.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SortedServiceAspect {

    @Around("execution(void com.korolyovEgor.TestTaskBigFileSorting.sortingService.SortedService.sort())")
    public void sortAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.nanoTime();
        proceedingJoinPoint.proceed();
        long finishTime = System.nanoTime();
        String timeExecution = String.format("%.2f", (finishTime - startTime) / 1_000_000.0);
        log.info("execution time of the sort method: " + timeExecution + "ms");
    }
}
