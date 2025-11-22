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
            .uid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .firstName("firstName1")
            .lastName("lastName1")
            .phone1("phone11")
            .phone2("phone21")
            .nif("nif1")
            .ninu("ninu1")
            .medicalRecordNumber("medicalRecordNumber1")
            .heightCm(1)
            .passportNumber("passportNumber1")
            .contactPersonName("contactPersonName1")
            .contactPersonPhone("contactPersonPhone1")
            .insuranceCompanyName("insuranceCompanyName1")
            .patientInsuranceId("patientInsuranceId1")
            .insurancePolicyNumber("insurancePolicyNumber1")
            .insuranceCoverageType("insuranceCoverageType1");
    }

    public static Patient getPatientSample2() {
        return new Patient()
            .id(2L)
            .uid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .firstName("firstName2")
            .lastName("lastName2")
            .phone1("phone12")
            .phone2("phone22")
            .nif("nif2")
            .ninu("ninu2")
            .medicalRecordNumber("medicalRecordNumber2")
            .heightCm(2)
            .passportNumber("passportNumber2")
            .contactPersonName("contactPersonName2")
            .contactPersonPhone("contactPersonPhone2")
            .insuranceCompanyName("insuranceCompanyName2")
            .patientInsuranceId("patientInsuranceId2")
            .insurancePolicyNumber("insurancePolicyNumber2")
            .insuranceCoverageType("insuranceCoverageType2");
    }

    public static Patient getPatientRandomSampleGenerator() {
        return new Patient()
            .id(longCount.incrementAndGet())
            .uid(UUID.randomUUID())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .phone1(UUID.randomUUID().toString())
            .phone2(UUID.randomUUID().toString())
            .nif(UUID.randomUUID().toString())
            .ninu(UUID.randomUUID().toString())
            .medicalRecordNumber(UUID.randomUUID().toString())
            .heightCm(intCount.incrementAndGet())
            .passportNumber(UUID.randomUUID().toString())
            .contactPersonName(UUID.randomUUID().toString())
            .contactPersonPhone(UUID.randomUUID().toString())
            .insuranceCompanyName(UUID.randomUUID().toString())
            .patientInsuranceId(UUID.randomUUID().toString())
            .insurancePolicyNumber(UUID.randomUUID().toString())
            .insuranceCoverageType(UUID.randomUUID().toString());
    }
}
