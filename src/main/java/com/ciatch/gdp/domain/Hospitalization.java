package com.ciatch.gdp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Enregistrement d'un séjour hospitalier.
 * @encryptedFields reason
 */
@Entity
@Table(name = "hospitalization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hospitalization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "admission_date", nullable = false)
    private ZonedDateTime admissionDate;

    @Column(name = "discharge_date")
    private ZonedDateTime dischargeDate;

    @Lob
    @Column(name = "reason")
    private String reason;

    /**
     * Une hospitalisation concerne un patient et est supervisée par un médecin.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    private User attendingDoctor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hospitalization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAdmissionDate() {
        return this.admissionDate;
    }

    public Hospitalization admissionDate(ZonedDateTime admissionDate) {
        this.setAdmissionDate(admissionDate);
        return this;
    }

    public void setAdmissionDate(ZonedDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public ZonedDateTime getDischargeDate() {
        return this.dischargeDate;
    }

    public Hospitalization dischargeDate(ZonedDateTime dischargeDate) {
        this.setDischargeDate(dischargeDate);
        return this;
    }

    public void setDischargeDate(ZonedDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getReason() {
        return this.reason;
    }

    public Hospitalization reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Hospitalization patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public User getAttendingDoctor() {
        return this.attendingDoctor;
    }

    public void setAttendingDoctor(User user) {
        this.attendingDoctor = user;
    }

    public Hospitalization attendingDoctor(User user) {
        this.setAttendingDoctor(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hospitalization)) {
            return false;
        }
        return getId() != null && getId().equals(((Hospitalization) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hospitalization{" +
            "id=" + getId() +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", dischargeDate='" + getDischargeDate() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
