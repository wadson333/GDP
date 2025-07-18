package com.ciatch.gdp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LabTestCatalogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LabTestCatalogDTO.class);
        LabTestCatalogDTO labTestCatalogDTO1 = new LabTestCatalogDTO();
        labTestCatalogDTO1.setId(1L);
        LabTestCatalogDTO labTestCatalogDTO2 = new LabTestCatalogDTO();
        assertThat(labTestCatalogDTO1).isNotEqualTo(labTestCatalogDTO2);
        labTestCatalogDTO2.setId(labTestCatalogDTO1.getId());
        assertThat(labTestCatalogDTO1).isEqualTo(labTestCatalogDTO2);
        labTestCatalogDTO2.setId(2L);
        assertThat(labTestCatalogDTO1).isNotEqualTo(labTestCatalogDTO2);
        labTestCatalogDTO1.setId(null);
        assertThat(labTestCatalogDTO1).isNotEqualTo(labTestCatalogDTO2);
    }
}
