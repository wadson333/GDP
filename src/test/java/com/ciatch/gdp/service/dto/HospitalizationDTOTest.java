package com.ciatch.gdp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HospitalizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HospitalizationDTO.class);
        HospitalizationDTO hospitalizationDTO1 = new HospitalizationDTO();
        hospitalizationDTO1.setId(1L);
        HospitalizationDTO hospitalizationDTO2 = new HospitalizationDTO();
        assertThat(hospitalizationDTO1).isNotEqualTo(hospitalizationDTO2);
        hospitalizationDTO2.setId(hospitalizationDTO1.getId());
        assertThat(hospitalizationDTO1).isEqualTo(hospitalizationDTO2);
        hospitalizationDTO2.setId(2L);
        assertThat(hospitalizationDTO1).isNotEqualTo(hospitalizationDTO2);
        hospitalizationDTO1.setId(null);
        assertThat(hospitalizationDTO1).isNotEqualTo(hospitalizationDTO2);
    }
}
