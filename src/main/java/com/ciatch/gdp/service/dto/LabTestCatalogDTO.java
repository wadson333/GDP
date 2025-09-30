package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.domain.enumeration.LabTestMethod;
import com.ciatch.gdp.domain.enumeration.LabTestType;
import com.ciatch.gdp.domain.enumeration.SampleType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.LabTestCatalog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LabTestCatalogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    @NotNull
    @Size(min = 2)
    private String unit;

    @Size(min = 10)
    private String description;

    @NotNull
    @Min(1)
    private Integer version;

    private Instant validFrom;

    private Instant validTo;

    private LabTestMethod method;

    private SampleType sampleType;

    private BigDecimal referenceRangeLow;

    private BigDecimal referenceRangeHigh;

    private Boolean active;

    private LabTestType type;

    @Pattern(regexp = "^[0-9]{1,3}-[0-9]{1,2}$")
    private String loincCode;

    @Min(1)
    private BigDecimal cost;

    @Min(30)
    private Integer turnaroundTime;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public LabTestMethod getMethod() {
        return method;
    }

    public void setMethod(LabTestMethod method) {
        this.method = method;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LabTestType getType() {
        return type;
    }

    public void setType(LabTestType type) {
        this.type = type;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(Integer turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
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
            ", description='" + getDescription() + "'" +
            ", version=" + getVersion() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            ", method='" + getMethod() + "'" +
            ", sampleType='" + getSampleType() + "'" +
            ", referenceRangeLow=" + getReferenceRangeLow() +
            ", referenceRangeHigh=" + getReferenceRangeHigh() +
            ", active='" + getActive() + "'" +
            ", type='" + getType() + "'" +
            ", loincCode='" + getLoincCode() + "'" +
            ", cost=" + getCost() +
            ", turnaroundTime=" + getTurnaroundTime() +
            "}";
    }
}
