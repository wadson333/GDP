package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.ConsultationTestSamples.*;
import static com.ciatch.gdp.domain.PatientTestSamples.*;
import static com.ciatch.gdp.domain.PrescriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consultation.class);
        Consultation consultation1 = getConsultationSample1();
        Consultation consultation2 = new Consultation();
        assertThat(consultation1).isNotEqualTo(consultation2);

        consultation2.setId(consultation1.getId());
        assertThat(consultation1).isEqualTo(consultation2);

        consultation2 = getConsultationSample2();
        assertThat(consultation1).isNotEqualTo(consultation2);
    }

    @Test
    void prescriptionTest() {
        Consultation consultation = getConsultationRandomSampleGenerator();
        Prescription prescriptionBack = getPrescriptionRandomSampleGenerator();

        consultation.setPrescription(prescriptionBack);
        assertThat(consultation.getPrescription()).isEqualTo(prescriptionBack);

        consultation.prescription(null);
        assertThat(consultation.getPrescription()).isNull();
    }

    @Test
    void patientTest() {
        Consultation consultation = getConsultationRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        consultation.setPatient(patientBack);
        assertThat(consultation.getPatient()).isEqualTo(patientBack);

        consultation.patient(null);
        assertThat(consultation.getPatient()).isNull();
    }
}
