package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalDocument getMedicalDocumentSample1() {
        return new MedicalDocument().id(1L).documentName("documentName1").filePath("filePath1").fileType("fileType1").desc("desc1");
    }

    public static MedicalDocument getMedicalDocumentSample2() {
        return new MedicalDocument().id(2L).documentName("documentName2").filePath("filePath2").fileType("fileType2").desc("desc2");
    }

    public static MedicalDocument getMedicalDocumentRandomSampleGenerator() {
        return new MedicalDocument()
            .id(longCount.incrementAndGet())
            .documentName(UUID.randomUUID().toString())
            .filePath(UUID.randomUUID().toString())
            .fileType(UUID.randomUUID().toString())
            .desc(UUID.randomUUID().toString());
    }
}
