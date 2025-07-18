package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.MedicationAsserts.*;
import static com.ciatch.gdp.domain.MedicationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicationMapperTest {

    private MedicationMapper medicationMapper;

    @BeforeEach
    void setUp() {
        medicationMapper = new MedicationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicationSample1();
        var actual = medicationMapper.toEntity(medicationMapper.toDto(expected));
        assertMedicationAllPropertiesEquals(expected, actual);
    }
}
