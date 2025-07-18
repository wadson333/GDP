package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.ConsultationTestSamples.*;
import static com.ciatch.gdp.domain.LabTestCatalogTestSamples.*;
import static com.ciatch.gdp.domain.LabTestResultTestSamples.*;
import static com.ciatch.gdp.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LabTestResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LabTestResult.class);
        LabTestResult labTestResult1 = getLabTestResultSample1();
        LabTestResult labTestResult2 = new LabTestResult();
        assertThat(labTestResult1).isNotEqualTo(labTestResult2);

        labTestResult2.setId(labTestResult1.getId());
        assertThat(labTestResult1).isEqualTo(labTestResult2);

        labTestResult2 = getLabTestResultSample2();
        assertThat(labTestResult1).isNotEqualTo(labTestResult2);
    }

    @Test
    void patientTest() {
        LabTestResult labTestResult = getLabTestResultRandomSampleGenerator();
        Patient patientBack = getPatientRandomSampleGenerator();

        labTestResult.setPatient(patientBack);
        assertThat(labTestResult.getPatient()).isEqualTo(patientBack);

        labTestResult.patient(null);
        assertThat(labTestResult.getPatient()).isNull();
    }

    @Test
    void labTestTest() {
        LabTestResult labTestResult = getLabTestResultRandomSampleGenerator();
        LabTestCatalog labTestCatalogBack = getLabTestCatalogRandomSampleGenerator();

        labTestResult.setLabTest(labTestCatalogBack);
        assertThat(labTestResult.getLabTest()).isEqualTo(labTestCatalogBack);

        labTestResult.labTest(null);
        assertThat(labTestResult.getLabTest()).isNull();
    }

    @Test
    void consultationTest() {
        LabTestResult labTestResult = getLabTestResultRandomSampleGenerator();
        Consultation consultationBack = getConsultationRandomSampleGenerator();

        labTestResult.setConsultation(consultationBack);
        assertThat(labTestResult.getConsultation()).isEqualTo(consultationBack);

        labTestResult.consultation(null);
        assertThat(labTestResult.getConsultation()).isNull();
    }
}
