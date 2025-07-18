package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PrescriptionItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PrescriptionItem getPrescriptionItemSample1() {
        return new PrescriptionItem().id(1L).dosage("dosage1").frequency("frequency1").duration("duration1");
    }

    public static PrescriptionItem getPrescriptionItemSample2() {
        return new PrescriptionItem().id(2L).dosage("dosage2").frequency("frequency2").duration("duration2");
    }

    public static PrescriptionItem getPrescriptionItemRandomSampleGenerator() {
        return new PrescriptionItem()
            .id(longCount.incrementAndGet())
            .dosage(UUID.randomUUID().toString())
            .frequency(UUID.randomUUID().toString())
            .duration(UUID.randomUUID().toString());
    }
}
