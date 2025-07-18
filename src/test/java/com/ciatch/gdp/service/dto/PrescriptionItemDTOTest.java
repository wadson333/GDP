package com.ciatch.gdp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrescriptionItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrescriptionItemDTO.class);
        PrescriptionItemDTO prescriptionItemDTO1 = new PrescriptionItemDTO();
        prescriptionItemDTO1.setId(1L);
        PrescriptionItemDTO prescriptionItemDTO2 = new PrescriptionItemDTO();
        assertThat(prescriptionItemDTO1).isNotEqualTo(prescriptionItemDTO2);
        prescriptionItemDTO2.setId(prescriptionItemDTO1.getId());
        assertThat(prescriptionItemDTO1).isEqualTo(prescriptionItemDTO2);
        prescriptionItemDTO2.setId(2L);
        assertThat(prescriptionItemDTO1).isNotEqualTo(prescriptionItemDTO2);
        prescriptionItemDTO1.setId(null);
        assertThat(prescriptionItemDTO1).isNotEqualTo(prescriptionItemDTO2);
    }
}
