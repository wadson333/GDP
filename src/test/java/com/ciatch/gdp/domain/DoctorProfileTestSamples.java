package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DoctorProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DoctorProfile getDoctorProfileSample1() {
        return new DoctorProfile().id(1L).specialty("specialty1").licenseNumber("licenseNumber1").university("university1");
    }

    public static DoctorProfile getDoctorProfileSample2() {
        return new DoctorProfile().id(2L).specialty("specialty2").licenseNumber("licenseNumber2").university("university2");
    }

    public static DoctorProfile getDoctorProfileRandomSampleGenerator() {
        return new DoctorProfile()
            .id(longCount.incrementAndGet())
            .specialty(UUID.randomUUID().toString())
            .licenseNumber(UUID.randomUUID().toString())
            .university(UUID.randomUUID().toString());
    }
}
