package com.ciatch.gdp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Représente chaque rencontre médicale (visite).
 * @encryptedFields symptoms, diagnosis
 */
@Entity
@Table(name = "consultation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consultation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "consultation_date", nullable = false)
    private ZonedDateTime consultationDate;

    @Lob
    @Column(name = "symptoms")
    private String symptoms;

    @Lob
    @Column(name = "diagnosis")
    private String diagnosis;

    /**
     * Une consultation donne lieu à une et une seule prescription.
     */
    @JsonIgnoreProperties(value = { "items", "consultation" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Prescription prescription;

    /**
     * Une consultation est menée par un médecin (User) et concerne un patient.
     */
    @ManyToOne(optional = false)
    @NotNull
    private User doctor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consultation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getConsultationDate() {
        return this.consultationDate;
    }

    public Consultation consultationDate(ZonedDateTime consultationDate) {
        this.setConsultationDate(consultationDate);
        return this;
    }

    public void setConsultationDate(ZonedDateTime consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getSymptoms() {
        return this.symptoms;
    }

    public Consultation symptoms(String symptoms) {
        this.setSymptoms(symptoms);
        return this;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public Consultation diagnosis(String diagnosis) {
        this.setDiagnosis(diagnosis);
        return this;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Prescription getPrescription() {
        return this.prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Consultation prescription(Prescription prescription) {
        this.setPrescription(prescription);
        return this;
    }

    public User getDoctor() {
        return this.doctor;
    }

    public void setDoctor(User user) {
        this.doctor = user;
    }

    public Consultation doctor(User user) {
        this.setDoctor(user);
        return this;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Consultation patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultation)) {
            return false;
        }
        return getId() != null && getId().equals(((Consultation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consultation{" +
            "id=" + getId() +
            ", consultationDate='" + getConsultationDate() + "'" +
            ", symptoms='" + getSymptoms() + "'" +
            ", diagnosis='" + getDiagnosis() + "'" +
            "}";
    }
}
