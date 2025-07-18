package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.LabTestResultAsserts.*;
import static com.ciatch.gdp.domain.LabTestResultTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LabTestResultMapperTest {

    private LabTestResultMapper labTestResultMapper;

    @BeforeEach
    void setUp() {
        labTestResultMapper = new LabTestResultMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLabTestResultSample1();
        var actual = labTestResultMapper.toEntity(labTestResultMapper.toDto(expected));
        assertLabTestResultAllPropertiesEquals(expected, actual);
    }
}
