package com.ciatch.gdp.service.mapper;

import static com.ciatch.gdp.domain.UserConfigurationAsserts.*;
import static com.ciatch.gdp.domain.UserConfigurationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserConfigurationMapperTest {

    private UserConfigurationMapper userConfigurationMapper;

    @BeforeEach
    void setUp() {
        userConfigurationMapper = new UserConfigurationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserConfigurationSample1();
        var actual = userConfigurationMapper.toEntity(userConfigurationMapper.toDto(expected));
        assertUserConfigurationAllPropertiesEquals(expected, actual);
    }
}
