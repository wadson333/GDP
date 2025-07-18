package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PrescriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Prescription getPrescriptionSample1() {
        return new Prescription().id(1L);
    }

    public static Prescription getPrescriptionSample2() {
        return new Prescription().id(2L);
    }

    public static Prescription getPrescriptionRandomSampleGenerator() {
        return new Prescription().id(longCount.incrementAndGet());
    }
}
