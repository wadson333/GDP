package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.MedicationTestSamples.*;
import static com.ciatch.gdp.domain.PrescriptionItemTestSamples.*;
import static com.ciatch.gdp.domain.PrescriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrescriptionItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrescriptionItem.class);
        PrescriptionItem prescriptionItem1 = getPrescriptionItemSample1();
        PrescriptionItem prescriptionItem2 = new PrescriptionItem();
        assertThat(prescriptionItem1).isNotEqualTo(prescriptionItem2);

        prescriptionItem2.setId(prescriptionItem1.getId());
        assertThat(prescriptionItem1).isEqualTo(prescriptionItem2);

        prescriptionItem2 = getPrescriptionItemSample2();
        assertThat(prescriptionItem1).isNotEqualTo(prescriptionItem2);
    }

    @Test
    void medicationTest() {
        PrescriptionItem prescriptionItem = getPrescriptionItemRandomSampleGenerator();
        Medication medicationBack = getMedicationRandomSampleGenerator();

        prescriptionItem.setMedication(medicationBack);
        assertThat(prescriptionItem.getMedication()).isEqualTo(medicationBack);

        prescriptionItem.medication(null);
        assertThat(prescriptionItem.getMedication()).isNull();
    }

    @Test
    void prescriptionTest() {
        PrescriptionItem prescriptionItem = getPrescriptionItemRandomSampleGenerator();
        Prescription prescriptionBack = getPrescriptionRandomSampleGenerator();

        prescriptionItem.setPrescription(prescriptionBack);
        assertThat(prescriptionItem.getPrescription()).isEqualTo(prescriptionBack);

        prescriptionItem.prescription(null);
        assertThat(prescriptionItem.getPrescription()).isNull();
    }
}
