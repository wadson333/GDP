package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.PrescriptionItemAsserts.*;
import static com.ciatch.gdp.domain.PrescriptionItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrescriptionItemMapperTest {

    private PrescriptionItemMapper prescriptionItemMapper;

    @BeforeEach
    void setUp() {
        prescriptionItemMapper = new PrescriptionItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrescriptionItemSample1();
        var actual = prescriptionItemMapper.toEntity(prescriptionItemMapper.toDto(expected));
        assertPrescriptionItemAllPropertiesEquals(expected, actual);
    }
}
