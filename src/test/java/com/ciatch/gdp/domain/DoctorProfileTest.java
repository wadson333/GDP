package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.DoctorProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DoctorProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorProfile.class);
        DoctorProfile doctorProfile1 = getDoctorProfileSample1();
        DoctorProfile doctorProfile2 = new DoctorProfile();
        assertThat(doctorProfile1).isNotEqualTo(doctorProfile2);

        doctorProfile2.setId(doctorProfile1.getId());
        assertThat(doctorProfile1).isEqualTo(doctorProfile2);

        doctorProfile2 = getDoctorProfileSample2();
        assertThat(doctorProfile1).isNotEqualTo(doctorProfile2);
    }
}
