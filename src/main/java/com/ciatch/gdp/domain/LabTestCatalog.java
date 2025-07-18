package com.ciatch.gdp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Catalogue des types d'analyses de laboratoire disponibles.
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
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "reference_range_low", precision = 21, scale = 2)
    private BigDecimal referenceRangeLow;

    @Column(name = "reference_range_high", precision = 21, scale = 2)
    private BigDecimal referenceRangeHigh;

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
            ", referenceRangeLow=" + getReferenceRangeLow() +
            ", referenceRangeHigh=" + getReferenceRangeHigh() +
            "}";
    }
}
