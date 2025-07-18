package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.Consultation} entity.
 */
@Schema(description = "Représente chaque rencontre médicale (visite).\n@encryptedFields symptoms, diagnosis")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultationDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime consultationDate;

    @Lob
    private String symptoms;

    @Lob
    private String diagnosis;

    @Schema(description = "Une consultation donne lieu à une et une seule prescription.")
    private PrescriptionDTO prescription;

    @NotNull
    @Schema(description = "Une consultation est menée par un médecin (User) et concerne un patient.")
    private UserDTO doctor;

    @NotNull
    private PatientDTO patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(ZonedDateTime consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public PrescriptionDTO getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionDTO prescription) {
        this.prescription = prescription;
    }

    public UserDTO getDoctor() {
        return doctor;
    }

    public void setDoctor(UserDTO doctor) {
        this.doctor = doctor;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultationDTO)) {
            return false;
        }

        ConsultationDTO consultationDTO = (ConsultationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consultationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultationDTO{" +
            "id=" + getId() +
            ", consultationDate='" + getConsultationDate() + "'" +
            ", symptoms='" + getSymptoms() + "'" +
            ", diagnosis='" + getDiagnosis() + "'" +
            ", prescription=" + getPrescription() +
            ", doctor=" + getDoctor() +
            ", patient=" + getPatient() +
            "}";
    }
}
