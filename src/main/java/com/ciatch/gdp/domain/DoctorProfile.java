package com.ciatch.gdp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Profil professionnel détaillé pour chaque médecin.
 * @encryptedFields licenseNumber
 */
@Entity
@Table(name = "doctor_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "specialty", nullable = false)
    private String specialty;

    @NotNull
    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "university")
    private String university;

    @NotNull
    @Column(name = "start_date_of_practice", nullable = false)
    private LocalDate startDateOfPractice;

    /**
     * Un profil de médecin est lié à un et un seul compte utilisateur.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DoctorProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    public DoctorProfile specialty(String specialty) {
        this.setSpecialty(specialty);
        return this;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLicenseNumber() {
        return this.licenseNumber;
    }

    public DoctorProfile licenseNumber(String licenseNumber) {
        this.setLicenseNumber(licenseNumber);
        return this;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getUniversity() {
        return this.university;
    }

    public DoctorProfile university(String university) {
        this.setUniversity(university);
        return this;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public LocalDate getStartDateOfPractice() {
        return this.startDateOfPractice;
    }

    public DoctorProfile startDateOfPractice(LocalDate startDateOfPractice) {
        this.setStartDateOfPractice(startDateOfPractice);
        return this;
    }

    public void setStartDateOfPractice(LocalDate startDateOfPractice) {
        this.startDateOfPractice = startDateOfPractice;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DoctorProfile user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((DoctorProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorProfile{" +
            "id=" + getId() +
            ", specialty='" + getSpecialty() + "'" +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            ", university='" + getUniversity() + "'" +
            ", startDateOfPractice='" + getStartDateOfPractice() + "'" +
            "}";
    }
}
