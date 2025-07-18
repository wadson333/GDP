package com.ciatch.gdp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserConfigurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConfigurationDTO.class);
        UserConfigurationDTO userConfigurationDTO1 = new UserConfigurationDTO();
        userConfigurationDTO1.setId(1L);
        UserConfigurationDTO userConfigurationDTO2 = new UserConfigurationDTO();
        assertThat(userConfigurationDTO1).isNotEqualTo(userConfigurationDTO2);
        userConfigurationDTO2.setId(userConfigurationDTO1.getId());
        assertThat(userConfigurationDTO1).isEqualTo(userConfigurationDTO2);
        userConfigurationDTO2.setId(2L);
        assertThat(userConfigurationDTO1).isNotEqualTo(userConfigurationDTO2);
        userConfigurationDTO1.setId(null);
        assertThat(userConfigurationDTO1).isNotEqualTo(userConfigurationDTO2);
    }
}
