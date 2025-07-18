package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class HospitalizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Hospitalization getHospitalizationSample1() {
        return new Hospitalization().id(1L);
    }

    public static Hospitalization getHospitalizationSample2() {
        return new Hospitalization().id(2L);
    }

    public static Hospitalization getHospitalizationRandomSampleGenerator() {
        return new Hospitalization().id(longCount.incrementAndGet());
    }
}
