package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.Prescription} entity.
 */
@Schema(description = "En-tête d'une ordonnance, liée à une consultation.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrescriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate prescriptionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrescriptionDTO)) {
            return false;
        }

        PrescriptionDTO prescriptionDTO = (PrescriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prescriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrescriptionDTO{" +
            "id=" + getId() +
            ", prescriptionDate='" + getPrescriptionDate() + "'" +
            "}";
    }
}
