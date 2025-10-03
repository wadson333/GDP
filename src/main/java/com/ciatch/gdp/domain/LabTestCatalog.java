package com.ciatch.gdp.domain;

import com.ciatch.gdp.domain.enumeration.LabTestMethod;
import com.ciatch.gdp.domain.enumeration.LabTestType;
import com.ciatch.gdp.domain.enumeration.SampleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LabTestCatalog.
 */
@Entity
@Table(name = "lab_test_catalog")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LabTestCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @Size(min = 3)
    private String name;

    @NotNull
    @Column(name = "unit", nullable = false)
    @Size(min = 2)
    private String unit;

    @Column(name = "description")
    @Size(min = 10)
    private String description;

    @NotNull
    @Column(name = "version", nullable = false)
    @Min(1)
    private Integer version;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private LabTestMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "sample_type")
    private SampleType sampleType;

    @Column(name = "reference_range_low", precision = 21, scale = 2)
    private BigDecimal referenceRangeLow;

    @Column(name = "reference_range_high", precision = 21, scale = 2)
    private BigDecimal referenceRangeHigh;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LabTestType type;

    @Column(name = "loinc_code")
    @Pattern(regexp = "^[0-9]{1,3}-[0-9]{1,2}$")
    private String loincCode;

    @Min(1)
    @Column(name = "cost", precision = 21, scale = 2)
    private BigDecimal cost;

    @Column(name = "turnaround_time")
    @Min(30)
    private Integer turnaroundTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LabTestCatalog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public LabTestCatalog name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public LabTestCatalog unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return this.description;
    }

    public LabTestCatalog description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return this.version;
    }

    public LabTestCatalog version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getValidFrom() {
        return this.validFrom;
    }

    public LabTestCatalog validFrom(Instant validFrom) {
        this.setValidFrom(validFrom);
        return this;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return this.validTo;
    }

    public LabTestCatalog validTo(Instant validTo) {
        this.setValidTo(validTo);
        return this;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public LabTestMethod getMethod() {
        return this.method;
    }

    public LabTestCatalog method(LabTestMethod method) {
        this.setMethod(method);
        return this;
    }

    public void setMethod(LabTestMethod method) {
        this.method = method;
    }

    public SampleType getSampleType() {
        return this.sampleType;
    }

    public LabTestCatalog sampleType(SampleType sampleType) {
        this.setSampleType(sampleType);
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public BigDecimal getReferenceRangeLow() {
        return this.referenceRangeLow;
    }

    public LabTestCatalog referenceRangeLow(BigDecimal referenceRangeLow) {
        this.setReferenceRangeLow(referenceRangeLow);
        return this;
    }

    public void setReferenceRangeLow(BigDecimal referenceRangeLow) {
        this.referenceRangeLow = referenceRangeLow;
    }

    public BigDecimal getReferenceRangeHigh() {
        return this.referenceRangeHigh;
    }

    public LabTestCatalog referenceRangeHigh(BigDecimal referenceRangeHigh) {
        this.setReferenceRangeHigh(referenceRangeHigh);
        return this;
    }

    public void setReferenceRangeHigh(BigDecimal referenceRangeHigh) {
        this.referenceRangeHigh = referenceRangeHigh;
    }

    public Boolean getActive() {
        return this.active;
    }

    public LabTestCatalog active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LabTestType getType() {
        return this.type;
    }

    public LabTestCatalog type(LabTestType type) {
        this.setType(type);
        return this;
    }

    public void setType(LabTestType type) {
        this.type = type;
    }

    public String getLoincCode() {
        return this.loincCode;
    }

    public LabTestCatalog loincCode(String loincCode) {
        this.setLoincCode(loincCode);
        return this;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public LabTestCatalog cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getTurnaroundTime() {
        return this.turnaroundTime;
    }

    public LabTestCatalog turnaroundTime(Integer turnaroundTime) {
        this.setTurnaroundTime(turnaroundTime);
        return this;
    }

    public void setTurnaroundTime(Integer turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabTestCatalog)) {
            return false;
        }
        return getId() != null && getId().equals(((LabTestCatalog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LabTestCatalog{" +
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
