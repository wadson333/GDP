package com.ciatch.gdp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Ligne détaillée d'une prescription (médicament, posologie, etc.).
 * @encryptedFields dosage, frequency, duration
 */
@Entity
@Table(name = "prescription_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrescriptionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "dosage")
    private String dosage;

    @NotNull
    @Column(name = "frequency", nullable = false)
    private String frequency;

    @Column(name = "duration")
    private String duration;

    /**
     * Une ligne de prescription est liée à un médicament du catalogue.
     */
    @ManyToOne(optional = false)
    @NotNull
    private Medication medication;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "items", "consultation" }, allowSetters = true)
    private Prescription prescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrescriptionItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDosage() {
        return this.dosage;
    }

    public PrescriptionItem dosage(String dosage) {
        this.setDosage(dosage);
        return this;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public PrescriptionItem frequency(String frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDuration() {
        return this.duration;
    }

    public PrescriptionItem duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Medication getMedication() {
        return this.medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public PrescriptionItem medication(Medication medication) {
        this.setMedication(medication);
        return this;
    }

    public Prescription getPrescription() {
        return this.prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public PrescriptionItem prescription(Prescription prescription) {
        this.setPrescription(prescription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrescriptionItem)) {
            return false;
        }
        return getId() != null && getId().equals(((PrescriptionItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrescriptionItem{" +
            "id=" + getId() +
            ", dosage='" + getDosage() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
