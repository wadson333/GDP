package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.LabTestResult} entity.
 */
@Schema(description = "Résultat numérique d'une analyse de laboratoire spécifique.\n@encryptedFields resultValue")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LabTestResultDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal resultValue;

    @NotNull
    private LocalDate resultDate;

    @NotNull
    private Boolean isAbnormal;

    @NotNull
    @Schema(description = "Un résultat de labo appartient à un patient, est d'un certain type, et peut venir d'une consultation.")
    private PatientDTO patient;

    @NotNull
    private LabTestCatalogDTO labTest;

    private ConsultationDTO consultation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getResultValue() {
        return resultValue;
    }

    public void setResultValue(BigDecimal resultValue) {
        this.resultValue = resultValue;
    }

    public LocalDate getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }

    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public LabTestCatalogDTO getLabTest() {
        return labTest;
    }

    public void setLabTest(LabTestCatalogDTO labTest) {
        this.labTest = labTest;
    }

    public ConsultationDTO getConsultation() {
        return consultation;
    }

    public void setConsultation(ConsultationDTO consultation) {
        this.consultation = consultation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabTestResultDTO)) {
            return false;
        }

        LabTestResultDTO labTestResultDTO = (LabTestResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, labTestResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LabTestResultDTO{" +
            "id=" + getId() +
            ", resultValue=" + getResultValue() +
            ", resultDate='" + getResultDate() + "'" +
            ", isAbnormal='" + getIsAbnormal() + "'" +
            ", patient=" + getPatient() +
            ", labTest=" + getLabTest() +
            ", consultation=" + getConsultation() +
            "}";
    }
}
