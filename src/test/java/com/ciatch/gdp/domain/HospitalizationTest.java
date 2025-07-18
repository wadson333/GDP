package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.HospitalizationTestSamples.*;
import static com.ciatch.gdp.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HospitalizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hospitalization.class);
        Hospitalization hospitalization1 = getHospitalizationSample1();
        Hospitalization hospitalization2 = new Hospitalization();
        assertThat(hospitalization1).isNotEqualTo(hospitalization2);

        hospitalization2.setId(hospitalization1.getId());
        assertThat(hospitalization1).isEqualTo(hospitalization2);

        hospitalization2 = getHospitalizationSample2();
        assertThat(hospitalization1).isNotEqualTo(hospitalization2);
    }

    @Test
    void patientTest() {
        Hospitalization hospitalization = getHospitalizationRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        hospitalization.setPatient(patientBack);
        assertThat(hospitalization.getPatient()).isEqualTo(patientBack);

        hospitalization.patient(null);
        assertThat(hospitalization.getPatient()).isNull();
    }
}
