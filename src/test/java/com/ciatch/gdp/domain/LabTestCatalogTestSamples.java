package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LabTestCatalogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LabTestCatalog getLabTestCatalogSample1() {
        return new LabTestCatalog()
            .id(1L)
            .name("name1")
            .unit("unit1")
            .description("description1")
            .version(1)
            .loincCode("loincCode1")
            .turnaroundTime(1);
    }

    public static LabTestCatalog getLabTestCatalogSample2() {
        return new LabTestCatalog()
            .id(2L)
            .name("name2")
            .unit("unit2")
            .description("description2")
            .version(2)
            .loincCode("loincCode2")
            .turnaroundTime(2);
    }

    public static LabTestCatalog getLabTestCatalogRandomSampleGenerator() {
        return new LabTestCatalog()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .unit(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet())
            .loincCode(UUID.randomUUID().toString())
            .turnaroundTime(intCount.incrementAndGet());
    }
}
