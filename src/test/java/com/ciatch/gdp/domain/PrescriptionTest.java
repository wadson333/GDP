package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.ConsultationTestSamples.*;
import static com.ciatch.gdp.domain.PrescriptionItemTestSamples.*;
import static com.ciatch.gdp.domain.PrescriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PrescriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prescription.class);
        Prescription prescription1 = getPrescriptionSample1();
        Prescription prescription2 = new Prescription();
        assertThat(prescription1).isNotEqualTo(prescription2);

        prescription2.setId(prescription1.getId());
        assertThat(prescription1).isEqualTo(prescription2);

        prescription2 = getPrescriptionSample2();
        assertThat(prescription1).isNotEqualTo(prescription2);
    }

    @Test
    void itemsTest() {
        Prescription prescription = getPrescriptionRandomSampleGenerator();
        PrescriptionItem prescriptionItemBack = getPrescriptionItemRandomSampleGenerator();

        prescription.addItems(prescriptionItemBack);
        assertThat(prescription.getItems()).containsOnly(prescriptionItemBack);
        assertThat(prescriptionItemBack.getPrescription()).isEqualTo(prescription);

        prescription.removeItems(prescriptionItemBack);
        assertThat(prescription.getItems()).doesNotContain(prescriptionItemBack);
        assertThat(prescriptionItemBack.getPrescription()).isNull();

        prescription.items(new HashSet<>(Set.of(prescriptionItemBack)));
        assertThat(prescription.getItems()).containsOnly(prescriptionItemBack);
        assertThat(prescriptionItemBack.getPrescription()).isEqualTo(prescription);

        prescription.setItems(new HashSet<>());
        assertThat(prescription.getItems()).doesNotContain(prescriptionItemBack);
        assertThat(prescriptionItemBack.getPrescription()).isNull();
    }

    @Test
    void consultationTest() {
        Prescription prescription = getPrescriptionRandomSampleGenerator();
        Consultation consultationBack = getConsultationRandomSampleGenerator();

        prescription.setConsultation(consultationBack);
        assertThat(prescription.getConsultation()).isEqualTo(consultationBack);
        assertThat(consultationBack.getPrescription()).isEqualTo(prescription);

        prescription.consultation(null);
        assertThat(prescription.getConsultation()).isNull();
        assertThat(consultationBack.getPrescription()).isNull();
    }
}
