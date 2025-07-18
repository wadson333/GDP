package com.ciatch.gdp.domain;

import com.ciatch.gdp.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Table centrale: contient les informations d'identification et administratives des patients.
 * @encryptedFields address, phone1, phone2, email, contactPersonName, contactPersonPhone, antecedents, allergies
 * @hashedFields nif, ninu, passportNumber
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "blood_type")
    private String bloodType;

    @Lob
    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "phone_1", nullable = false)
    private String phone1;

    @Column(name = "phone_2")
    private String phone2;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nif", unique = true)
    private String nif;

    @Column(name = "ninu", unique = true)
    private String ninu;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "passport_number", unique = true)
    private String passportNumber;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    @Lob
    @Column(name = "antecedents")
    private String antecedents;

    @Lob
    @Column(name = "allergies")
    private String allergies;

    /**
     * Un dossier patient est lié à un et un seul compte utilisateur (pour le portail).
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Patient firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Patient lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Patient birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Patient gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return this.bloodType;
    }

    public Patient bloodType(String bloodType) {
        this.setBloodType(bloodType);
        return this;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAddress() {
        return this.address;
    }

    public Patient address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return this.phone1;
    }

    public Patient phone1(String phone1) {
        this.setPhone1(phone1);
        return this;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return this.phone2;
    }

    public Patient phone2(String phone2) {
        this.setPhone2(phone2);
        return this;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return this.email;
    }

    public Patient email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNif() {
        return this.nif;
    }

    public Patient nif(String nif) {
        this.setNif(nif);
        return this;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNinu() {
        return this.ninu;
    }

    public Patient ninu(String ninu) {
        this.setNinu(ninu);
        return this;
    }

    public void setNinu(String ninu) {
        this.ninu = ninu;
    }

    public Integer getHeightCm() {
        return this.heightCm;
    }

    public Patient heightCm(Integer heightCm) {
        this.setHeightCm(heightCm);
        return this;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return this.weightKg;
    }

    public Patient weightKg(Double weightKg) {
        this.setWeightKg(weightKg);
        return this;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public Patient passportNumber(String passportNumber) {
        this.setPassportNumber(passportNumber);
        return this;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getContactPersonName() {
        return this.contactPersonName;
    }

    public Patient contactPersonName(String contactPersonName) {
        this.setContactPersonName(contactPersonName);
        return this;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhone() {
        return this.contactPersonPhone;
    }

    public Patient contactPersonPhone(String contactPersonPhone) {
        this.setContactPersonPhone(contactPersonPhone);
        return this;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getAntecedents() {
        return this.antecedents;
    }

    public Patient antecedents(String antecedents) {
        this.setAntecedents(antecedents);
        return this;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public String getAllergies() {
        return this.allergies;
    }

    public Patient allergies(String allergies) {
        this.setAllergies(allergies);
        return this;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Patient user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return getId() != null && getId().equals(((Patient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone1='" + getPhone1() + "'" +
            ", phone2='" + getPhone2() + "'" +
            ", email='" + getEmail() + "'" +
            ", nif='" + getNif() + "'" +
            ", ninu='" + getNinu() + "'" +
            ", heightCm=" + getHeightCm() +
            ", weightKg=" + getWeightKg() +
            ", passportNumber='" + getPassportNumber() + "'" +
            ", contactPersonName='" + getContactPersonName() + "'" +
            ", contactPersonPhone='" + getContactPersonPhone() + "'" +
            ", antecedents='" + getAntecedents() + "'" +
            ", allergies='" + getAllergies() + "'" +
            "}";
    }
}
