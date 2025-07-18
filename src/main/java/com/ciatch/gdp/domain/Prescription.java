package com.ciatch.gdp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * En-tête d'une ordonnance, liée à une consultation.
 */
@Entity
@Table(name = "prescription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "prescription_date", nullable = false)
    private LocalDate prescriptionDate;

    /**
     * Une prescription contient plusieurs lignes de médicaments.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "prescription")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medication", "prescription" }, allowSetters = true)
    private Set<PrescriptionItem> items = new HashSet<>();

    @JsonIgnoreProperties(value = { "prescription", "doctor", "patient" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "prescription")
    private Consultation consultation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prescription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPrescriptionDate() {
        return this.prescriptionDate;
    }

    public Prescription prescriptionDate(LocalDate prescriptionDate) {
        this.setPrescriptionDate(prescriptionDate);
        return this;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public Set<PrescriptionItem> getItems() {
        return this.items;
    }

    public void setItems(Set<PrescriptionItem> prescriptionItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setPrescription(null));
        }
        if (prescriptionItems != null) {
            prescriptionItems.forEach(i -> i.setPrescription(this));
        }
        this.items = prescriptionItems;
    }

    public Prescription items(Set<PrescriptionItem> prescriptionItems) {
        this.setItems(prescriptionItems);
        return this;
    }

    public Prescription addItems(PrescriptionItem prescriptionItem) {
        this.items.add(prescriptionItem);
        prescriptionItem.setPrescription(this);
        return this;
    }

    public Prescription removeItems(PrescriptionItem prescriptionItem) {
        this.items.remove(prescriptionItem);
        prescriptionItem.setPrescription(null);
        return this;
    }

    public Consultation getConsultation() {
        return this.consultation;
    }

    public void setConsultation(Consultation consultation) {
        if (this.consultation != null) {
            this.consultation.setPrescription(null);
        }
        if (consultation != null) {
            consultation.setPrescription(this);
        }
        this.consultation = consultation;
    }

    public Prescription consultation(Consultation consultation) {
        this.setConsultation(consultation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prescription)) {
            return false;
        }
        return getId() != null && getId().equals(((Prescription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prescription{" +
            "id=" + getId() +
            ", prescriptionDate='" + getPrescriptionDate() + "'" +
            "}";
    }
}
