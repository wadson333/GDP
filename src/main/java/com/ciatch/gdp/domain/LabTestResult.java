package com.ciatch.gdp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Résultat numérique d'une analyse de laboratoire spécifique.
 * @encryptedFields resultValue
 */
@Entity
@Table(name = "lab_test_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LabTestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "result_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal resultValue;

    @NotNull
    @Column(name = "result_date", nullable = false)
    private LocalDate resultDate;

    @NotNull
    @Column(name = "is_abnormal", nullable = false)
    private Boolean isAbnormal;

    /**
     * Un résultat de labo appartient à un patient, est d'un certain type, et peut venir d'une consultation.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Patient patient;

    @ManyToOne(optional = false)
    @NotNull
    private LabTestCatalog labTest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "prescription", "doctor", "patient" }, allowSetters = true)
    private Consultation consultation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LabTestResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getResultValue() {
        return this.resultValue;
    }

    public LabTestResult resultValue(BigDecimal resultValue) {
        this.setResultValue(resultValue);
        return this;
    }

    public void setResultValue(BigDecimal resultValue) {
        this.resultValue = resultValue;
    }

    public LocalDate getResultDate() {
        return this.resultDate;
    }

    public LabTestResult resultDate(LocalDate resultDate) {
        this.setResultDate(resultDate);
        return this;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }

    public Boolean getIsAbnormal() {
        return this.isAbnormal;
    }

    public LabTestResult isAbnormal(Boolean isAbnormal) {
        this.setIsAbnormal(isAbnormal);
        return this;
    }

    public void setIsAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LabTestResult patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public LabTestCatalog getLabTest() {
        return this.labTest;
    }

    public void setLabTest(LabTestCatalog labTestCatalog) {
        this.labTest = labTestCatalog;
    }

    public LabTestResult labTest(LabTestCatalog labTestCatalog) {
        this.setLabTest(labTestCatalog);
        return this;
    }

    public Consultation getConsultation() {
        return this.consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public LabTestResult consultation(Consultation consultation) {
        this.setConsultation(consultation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabTestResult)) {
            return false;
        }
        return getId() != null && getId().equals(((LabTestResult) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LabTestResult{" +
            "id=" + getId() +
            ", resultValue=" + getResultValue() +
            ", resultDate='" + getResultDate() + "'" +
            ", isAbnormal='" + getIsAbnormal() + "'" +
            "}";
    }
}
