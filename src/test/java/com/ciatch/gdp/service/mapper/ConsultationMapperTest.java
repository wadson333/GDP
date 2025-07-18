package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.ConsultationAsserts.*;
import static com.ciatch.gdp.domain.ConsultationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultationMapperTest {

    private ConsultationMapper consultationMapper;

    @BeforeEach
    void setUp() {
        consultationMapper = new ConsultationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConsultationSample1();
        var actual = consultationMapper.toEntity(consultationMapper.toDto(expected));
        assertConsultationAllPropertiesEquals(expected, actual);
    }
}
