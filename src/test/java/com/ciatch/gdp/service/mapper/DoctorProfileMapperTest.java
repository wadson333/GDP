package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.DoctorProfileAsserts.*;
import static com.ciatch.gdp.domain.DoctorProfileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoctorProfileMapperTest {

    private DoctorProfileMapper doctorProfileMapper;

    @BeforeEach
    void setUp() {
        doctorProfileMapper = new DoctorProfileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDoctorProfileSample1();
        var actual = doctorProfileMapper.toEntity(doctorProfileMapper.toDto(expected));
        assertDoctorProfileAllPropertiesEquals(expected, actual);
    }
}
