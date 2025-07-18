package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.MedicalDocumentTestSamples.*;
import static com.ciatch.gdp.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalDocument.class);
        MedicalDocument medicalDocument1 = getMedicalDocumentSample1();
        MedicalDocument medicalDocument2 = new MedicalDocument();
        assertThat(medicalDocument1).isNotEqualTo(medicalDocument2);

        medicalDocument2.setId(medicalDocument1.getId());
        assertThat(medicalDocument1).isEqualTo(medicalDocument2);

        medicalDocument2 = getMedicalDocumentSample2();
        assertThat(medicalDocument1).isNotEqualTo(medicalDocument2);
    }

    @Test
    void patientTest() {
        MedicalDocument medicalDocument = getMedicalDocumentRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        medicalDocument.setPatient(patientBack);
        assertThat(medicalDocument.getPatient()).isEqualTo(patientBack);

        medicalDocument.patient(null);
        assertThat(medicalDocument.getPatient()).isNull();
    }
}
