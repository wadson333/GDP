package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medication getMedicationSample1() {
        return new Medication()
            .id(1L)
            .name("name1")
            .internationalName("internationalName1")
            .codeAtc("codeAtc1")
            .formulation("formulation1")
            .strength("strength1")
            .manufacturer("manufacturer1")
            .marketingAuthorizationNumber("marketingAuthorizationNumber1")
            .packaging("packaging1")
            .description("description1")
            .barcode("barcode1")
            .storageCondition("storageCondition1")
            .image("image1")
            .composition("composition1")
            .contraindications("contraindications1")
            .sideEffects("sideEffects1");
    }

    public static Medication getMedicationSample2() {
        return new Medication()
            .id(2L)
            .name("name2")
            .internationalName("internationalName2")
            .codeAtc("codeAtc2")
            .formulation("formulation2")
            .strength("strength2")
            .manufacturer("manufacturer2")
            .marketingAuthorizationNumber("marketingAuthorizationNumber2")
            .packaging("packaging2")
            .description("description2")
            .barcode("barcode2")
            .storageCondition("storageCondition2")
            .image("image2")
            .composition("composition2")
            .contraindications("contraindications2")
            .sideEffects("sideEffects2");
    }

    public static Medication getMedicationRandomSampleGenerator() {
        return new Medication()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .internationalName(UUID.randomUUID().toString())
            .codeAtc(UUID.randomUUID().toString())
            .formulation(UUID.randomUUID().toString())
            .strength(UUID.randomUUID().toString())
            .manufacturer(UUID.randomUUID().toString())
            .marketingAuthorizationNumber(UUID.randomUUID().toString())
            .packaging(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .barcode(UUID.randomUUID().toString())
            .storageCondition(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString())
            .composition(UUID.randomUUID().toString())
            .contraindications(UUID.randomUUID().toString())
            .sideEffects(UUID.randomUUID().toString());
    }
}
