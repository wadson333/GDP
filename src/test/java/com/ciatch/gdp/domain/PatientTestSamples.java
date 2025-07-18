package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PatientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Patient getPatientSample1() {
        return new Patient()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .bloodType("bloodType1")
            .phone1("phone11")
            .phone2("phone21")
            .email("email1")
            .nif("nif1")
            .ninu("ninu1")
            .heightCm(1)
            .passportNumber("passportNumber1")
            .contactPersonName("contactPersonName1")
            .contactPersonPhone("contactPersonPhone1");
    }

    public static Patient getPatientSample2() {
        return new Patient()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .bloodType("bloodType2")
            .phone1("phone12")
            .phone2("phone22")
            .email("email2")
            .nif("nif2")
            .ninu("ninu2")
            .heightCm(2)
            .passportNumber("passportNumber2")
            .contactPersonName("contactPersonName2")
            .contactPersonPhone("contactPersonPhone2");
    }

    public static Patient getPatientRandomSampleGenerator() {
        return new Patient()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .bloodType(UUID.randomUUID().toString())
            .phone1(UUID.randomUUID().toString())
            .phone2(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .nif(UUID.randomUUID().toString())
            .ninu(UUID.randomUUID().toString())
            .heightCm(intCount.incrementAndGet())
            .passportNumber(UUID.randomUUID().toString())
            .contactPersonName(UUID.randomUUID().toString())
            .contactPersonPhone(UUID.randomUUID().toString());
    }
}
