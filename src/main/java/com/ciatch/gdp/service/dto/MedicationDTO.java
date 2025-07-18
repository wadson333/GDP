package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.Medication} entity.
 */
@Schema(description = "Dictionnaire central des médicaments.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationDTO)) {
            return false;
        }

        MedicationDTO medicationDTO = (MedicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
