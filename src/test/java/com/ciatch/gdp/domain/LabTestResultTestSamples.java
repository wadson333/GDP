package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class LabTestResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LabTestResult getLabTestResultSample1() {
        return new LabTestResult().id(1L);
    }

    public static LabTestResult getLabTestResultSample2() {
        return new LabTestResult().id(2L);
    }

    public static LabTestResult getLabTestResultRandomSampleGenerator() {
        return new LabTestResult().id(longCount.incrementAndGet());
    }
}
