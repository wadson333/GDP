package com.ciatch.gdp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.LabTestCatalog} entity.
 */
@Schema(description = "Catalogue des types d'analyses de laboratoire disponibles.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LabTestCatalogDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String unit;

    private BigDecimal referenceRangeLow;

    private BigDecimal referenceRangeHigh;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getReferenceRangeLow() {
        return referenceRangeLow;
    }

    public void setReferenceRangeLow(BigDecimal referenceRangeLow) {
        this.referenceRangeLow = referenceRangeLow;
    }

    public BigDecimal getReferenceRangeHigh() {
        return referenceRangeHigh;
    }

    public void setReferenceRangeHigh(BigDecimal referenceRangeHigh) {
        this.referenceRangeHigh = referenceRangeHigh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabTestCatalogDTO)) {
            return false;
        }

        LabTestCatalogDTO labTestCatalogDTO = (LabTestCatalogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, labTestCatalogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LabTestCatalogDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", referenceRangeLow=" + getReferenceRangeLow() +
            ", referenceRangeHigh=" + getReferenceRangeHigh() +
            "}";
    }
}
