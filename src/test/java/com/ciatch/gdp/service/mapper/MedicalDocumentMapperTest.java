package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.MedicalDocumentAsserts.*;
import static com.ciatch.gdp.domain.MedicalDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalDocumentMapperTest {

    private MedicalDocumentMapper medicalDocumentMapper;

    @BeforeEach
    void setUp() {
        medicalDocumentMapper = new MedicalDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalDocumentSample1();
        var actual = medicalDocumentMapper.toEntity(medicalDocumentMapper.toDto(expected));
        assertMedicalDocumentAllPropertiesEquals(expected, actual);
    }
}
