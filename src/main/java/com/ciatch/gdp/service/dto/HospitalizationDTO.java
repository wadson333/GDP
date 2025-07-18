package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.Hospitalization} entity.
 */
@Schema(description = "Enregistrement d'un séjour hospitalier.\n@encryptedFields reason")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HospitalizationDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime admissionDate;

    private ZonedDateTime dischargeDate;

    @Lob
    private String reason;

    @NotNull
    @Schema(description = "Une hospitalisation concerne un patient et est supervisée par un médecin.")
    private PatientDTO patient;

    private UserDTO attendingDoctor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(ZonedDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public ZonedDateTime getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(ZonedDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public UserDTO getAttendingDoctor() {
        return attendingDoctor;
    }

    public void setAttendingDoctor(UserDTO attendingDoctor) {
        this.attendingDoctor = attendingDoctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HospitalizationDTO)) {
            return false;
        }

        HospitalizationDTO hospitalizationDTO = (HospitalizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hospitalizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HospitalizationDTO{" +
            "id=" + getId() +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", dischargeDate='" + getDischargeDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", patient=" + getPatient() +
            ", attendingDoctor=" + getAttendingDoctor() +
            "}";
    }
}
