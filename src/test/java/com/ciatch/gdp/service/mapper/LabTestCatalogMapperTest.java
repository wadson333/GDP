package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.LabTestCatalogAsserts.*;
import static com.ciatch.gdp.domain.LabTestCatalogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LabTestCatalogMapperTest {

    private LabTestCatalogMapper labTestCatalogMapper;

    @BeforeEach
    void setUp() {
        labTestCatalogMapper = new LabTestCatalogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLabTestCatalogSample1();
        var actual = labTestCatalogMapper.toEntity(labTestCatalogMapper.toDto(expected));
        assertLabTestCatalogAllPropertiesEquals(expected, actual);
    }
}
