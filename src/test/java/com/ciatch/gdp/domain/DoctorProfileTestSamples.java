package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DoctorProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DoctorProfile getDoctorProfileSample1() {
        return new DoctorProfile()
            .id(1L)
            .codeClinic("codeClinic1")
            .uid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .medicalLicenseNumber("medicalLicenseNumber1")
            .firstName("firstName1")
            .lastName("lastName1")
            .university("university1")
            .graduationYear(1)
            .consultationDurationMinutes(1)
            .spokenLanguages("spokenLanguages1")
            .websiteUrl("websiteUrl1")
            .officePhone("officePhone1")
            .nif("nif1")
            .ninu("ninu1")
            .reviewCount(1)
            .version(1L);
    }

    public static DoctorProfile getDoctorProfileSample2() {
        return new DoctorProfile()
            .id(2L)
            .codeClinic("codeClinic2")
            .uid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .medicalLicenseNumber("medicalLicenseNumber2")
            .firstName("firstName2")
            .lastName("lastName2")
            .university("university2")
            .graduationYear(2)
            .consultationDurationMinutes(2)
            .spokenLanguages("spokenLanguages2")
            .websiteUrl("websiteUrl2")
            .officePhone("officePhone2")
            .nif("nif2")
            .ninu("ninu2")
            .reviewCount(2)
            .version(2L);
    }

    public static DoctorProfile getDoctorProfileRandomSampleGenerator() {
        return new DoctorProfile()
            .id(longCount.incrementAndGet())
            .codeClinic(UUID.randomUUID().toString())
            .uid(UUID.randomUUID())
            .medicalLicenseNumber(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .university(UUID.randomUUID().toString())
            .graduationYear(intCount.incrementAndGet())
            .consultationDurationMinutes(intCount.incrementAndGet())
            .spokenLanguages(UUID.randomUUID().toString())
            .websiteUrl(UUID.randomUUID().toString())
            .officePhone(UUID.randomUUID().toString())
            .nif(UUID.randomUUID().toString())
            .ninu(UUID.randomUUID().toString())
            .reviewCount(intCount.incrementAndGet())
            .version(longCount.incrementAndGet());
    }
}
