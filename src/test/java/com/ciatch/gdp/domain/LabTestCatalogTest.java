package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.LabTestCatalogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LabTestCatalogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LabTestCatalog.class);
        LabTestCatalog labTestCatalog1 = getLabTestCatalogSample1();
        LabTestCatalog labTestCatalog2 = new LabTestCatalog();
        assertThat(labTestCatalog1).isNotEqualTo(labTestCatalog2);

        labTestCatalog2.setId(labTestCatalog1.getId());
        assertThat(labTestCatalog1).isEqualTo(labTestCatalog2);

        labTestCatalog2 = getLabTestCatalogSample2();
        assertThat(labTestCatalog1).isNotEqualTo(labTestCatalog2);
    }
}
