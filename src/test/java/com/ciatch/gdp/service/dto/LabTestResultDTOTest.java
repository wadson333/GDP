package com.ciatch.gdp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LabTestResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LabTestResultDTO.class);
        LabTestResultDTO labTestResultDTO1 = new LabTestResultDTO();
        labTestResultDTO1.setId(1L);
        LabTestResultDTO labTestResultDTO2 = new LabTestResultDTO();
        assertThat(labTestResultDTO1).isNotEqualTo(labTestResultDTO2);
        labTestResultDTO2.setId(labTestResultDTO1.getId());
        assertThat(labTestResultDTO1).isEqualTo(labTestResultDTO2);
        labTestResultDTO2.setId(2L);
        assertThat(labTestResultDTO1).isNotEqualTo(labTestResultDTO2);
        labTestResultDTO1.setId(null);
        assertThat(labTestResultDTO1).isNotEqualTo(labTestResultDTO2);
    }
}
