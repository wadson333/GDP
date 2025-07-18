package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.PrescriptionItem} entity.
 */
@Schema(description = "Ligne détaillée d'une prescription (médicament, posologie, etc.).\n@encryptedFields dosage, frequency, duration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrescriptionItemDTO implements Serializable {

    private Long id;

    private String dosage;

    @NotNull
    private String frequency;

    private String duration;

    @NotNull
    @Schema(description = "Une ligne de prescription est liée à un médicament du catalogue.")
    private MedicationDTO medication;

    @NotNull
    private PrescriptionDTO prescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public MedicationDTO getMedication() {
        return medication;
    }

    public void setMedication(MedicationDTO medication) {
        this.medication = medication;
    }

    public PrescriptionDTO getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionDTO prescription) {
        this.prescription = prescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrescriptionItemDTO)) {
            return false;
        }

        PrescriptionItemDTO prescriptionItemDTO = (PrescriptionItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prescriptionItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrescriptionItemDTO{" +
            "id=" + getId() +
            ", dosage='" + getDosage() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", duration='" + getDuration() + "'" +
            ", medication=" + getMedication() +
            ", prescription=" + getPrescription() +
            "}";
    }
}
