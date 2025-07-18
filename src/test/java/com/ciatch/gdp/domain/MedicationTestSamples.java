package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medication getMedicationSample1() {
        return new Medication().id(1L).name("name1");
    }

    public static Medication getMedicationSample2() {
        return new Medication().id(2L).name("name2");
    }

    public static Medication getMedicationRandomSampleGenerator() {
        return new Medication().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
