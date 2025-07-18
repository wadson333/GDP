package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.HospitalizationAsserts.*;
import static com.ciatch.gdp.domain.HospitalizationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HospitalizationMapperTest {

    private HospitalizationMapper hospitalizationMapper;

    @BeforeEach
    void setUp() {
        hospitalizationMapper = new HospitalizationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHospitalizationSample1();
        var actual = hospitalizationMapper.toEntity(hospitalizationMapper.toDto(expected));
        assertHospitalizationAllPropertiesEquals(expected, actual);
    }
}
