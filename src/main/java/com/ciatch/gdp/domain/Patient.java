package com.ciatch.gdp.domain;

import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.PatientStatus;
import com.ciatch.gdp.domain.enumeration.SmokingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private UUID uid;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type")
    private BloodType bloodType;

    // @Lob
    @Column(name = "address")
    private String address;

    @NotNull
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "phone_1", nullable = false)
    private String phone1;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "phone_2")
    private String phone2;

    @Size(min = 10, max = 10)
    @Column(name = "nif", length = 10, unique = true)
    private String nif;

    @Size(min = 10, max = 10)
    @Column(name = "ninu", length = 10, unique = true)
    private String ninu;

    @Column(name = "medical_record_number", unique = true)
    private String medicalRecordNumber;

    @Min(value = 0)
    @Max(value = 300)
    @Column(name = "height_cm")
    private Integer heightCm;

    @DecimalMin(value = "0")
    @DecimalMax(value = "500")
    @Column(name = "weight_kg", precision = 21, scale = 2)
    private BigDecimal weightKg;

    @Size(min = 3, max = 15)
    @Column(name = "passport_number", length = 15, unique = true)
    private String passportNumber;

    @Size(max = 100)
    @Column(name = "contact_person_name", length = 100)
    private String contactPersonName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    // @Lob
    @Column(name = "antecedents")
    private String antecedents;

    // @Lob
    @Column(name = "allergies")
    private String allergies;

    // @Lob
    @Column(name = "clinical_notes")
    private String clinicalNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "smoking_status")
    private SmokingStatus smokingStatus;

    @Column(name = "gdpr_consent_date")
    private ZonedDateTime gdprConsentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PatientStatus status;

    @Column(name = "deceased_date")
    private ZonedDateTime deceasedDate;

    @Size(max = 200)
    @Column(name = "insurance_company_name", length = 200)
    private String insuranceCompanyName;

    @Size(max = 100)
    @Column(name = "patient_insurance_id", length = 100)
    private String patientInsuranceId;

    @Size(max = 100)
    @Column(name = "insurance_policy_number", length = 100)
    private String insurancePolicyNumber;

    @Size(max = 100)
    @Column(name = "insurance_coverage_type", length = 100)
    private String insuranceCoverageType;

    @Column(name = "insurance_valid_from")
    private LocalDate insuranceValidFrom;

    @Column(name = "insurance_valid_to")
    private LocalDate insuranceValidTo;

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

    public UUID getUid() {
        return this.uid;
    }

    public Patient uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
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

    public BloodType getBloodType() {
        return this.bloodType;
    }

    public Patient bloodType(BloodType bloodType) {
        this.setBloodType(bloodType);
        return this;
    }

    public void setBloodType(BloodType bloodType) {
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

    public String getMedicalRecordNumber() {
        return this.medicalRecordNumber;
    }

    public Patient medicalRecordNumber(String medicalRecordNumber) {
        this.setMedicalRecordNumber(medicalRecordNumber);
        return this;
    }

    public void setMedicalRecordNumber(String medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
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

    public BigDecimal getWeightKg() {
        return this.weightKg;
    }

    public Patient weightKg(BigDecimal weightKg) {
        this.setWeightKg(weightKg);
        return this;
    }

    public void setWeightKg(BigDecimal weightKg) {
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

    public String getClinicalNotes() {
        return this.clinicalNotes;
    }

    public Patient clinicalNotes(String clinicalNotes) {
        this.setClinicalNotes(clinicalNotes);
        return this;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public SmokingStatus getSmokingStatus() {
        return this.smokingStatus;
    }

    public Patient smokingStatus(SmokingStatus smokingStatus) {
        this.setSmokingStatus(smokingStatus);
        return this;
    }

    public void setSmokingStatus(SmokingStatus smokingStatus) {
        this.smokingStatus = smokingStatus;
    }

    public ZonedDateTime getGdprConsentDate() {
        return this.gdprConsentDate;
    }

    public Patient gdprConsentDate(ZonedDateTime gdprConsentDate) {
        this.setGdprConsentDate(gdprConsentDate);
        return this;
    }

    public void setGdprConsentDate(ZonedDateTime gdprConsentDate) {
        this.gdprConsentDate = gdprConsentDate;
    }

    public PatientStatus getStatus() {
        return this.status;
    }

    public Patient status(PatientStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PatientStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDeceasedDate() {
        return this.deceasedDate;
    }

    public Patient deceasedDate(ZonedDateTime deceasedDate) {
        this.setDeceasedDate(deceasedDate);
        return this;
    }

    public void setDeceasedDate(ZonedDateTime deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public String getInsuranceCompanyName() {
        return this.insuranceCompanyName;
    }

    public Patient insuranceCompanyName(String insuranceCompanyName) {
        this.setInsuranceCompanyName(insuranceCompanyName);
        return this;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getPatientInsuranceId() {
        return this.patientInsuranceId;
    }

    public Patient patientInsuranceId(String patientInsuranceId) {
        this.setPatientInsuranceId(patientInsuranceId);
        return this;
    }

    public void setPatientInsuranceId(String patientInsuranceId) {
        this.patientInsuranceId = patientInsuranceId;
    }

    public String getInsurancePolicyNumber() {
        return this.insurancePolicyNumber;
    }

    public Patient insurancePolicyNumber(String insurancePolicyNumber) {
        this.setInsurancePolicyNumber(insurancePolicyNumber);
        return this;
    }

    public void setInsurancePolicyNumber(String insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

    public String getInsuranceCoverageType() {
        return this.insuranceCoverageType;
    }

    public Patient insuranceCoverageType(String insuranceCoverageType) {
        this.setInsuranceCoverageType(insuranceCoverageType);
        return this;
    }

    public void setInsuranceCoverageType(String insuranceCoverageType) {
        this.insuranceCoverageType = insuranceCoverageType;
    }

    public LocalDate getInsuranceValidFrom() {
        return this.insuranceValidFrom;
    }

    public Patient insuranceValidFrom(LocalDate insuranceValidFrom) {
        this.setInsuranceValidFrom(insuranceValidFrom);
        return this;
    }

    public void setInsuranceValidFrom(LocalDate insuranceValidFrom) {
        this.insuranceValidFrom = insuranceValidFrom;
    }

    public LocalDate getInsuranceValidTo() {
        return this.insuranceValidTo;
    }

    public Patient insuranceValidTo(LocalDate insuranceValidTo) {
        this.setInsuranceValidTo(insuranceValidTo);
        return this;
    }

    public void setInsuranceValidTo(LocalDate insuranceValidTo) {
        this.insuranceValidTo = insuranceValidTo;
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
            ", uid='" + getUid() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone1='" + getPhone1() + "'" +
            ", phone2='" + getPhone2() + "'" +
            ", nif='" + getNif() + "'" +
            ", ninu='" + getNinu() + "'" +
            ", medicalRecordNumber='" + getMedicalRecordNumber() + "'" +
            ", heightCm=" + getHeightCm() +
            ", weightKg=" + getWeightKg() +
            ", passportNumber='" + getPassportNumber() + "'" +
            ", contactPersonName='" + getContactPersonName() + "'" +
            ", contactPersonPhone='" + getContactPersonPhone() + "'" +
            ", antecedents='" + getAntecedents() + "'" +
            ", allergies='" + getAllergies() + "'" +
            ", clinicalNotes='" + getClinicalNotes() + "'" +
            ", smokingStatus='" + getSmokingStatus() + "'" +
            ", gdprConsentDate='" + getGdprConsentDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", deceasedDate='" + getDeceasedDate() + "'" +
            ", insuranceCompanyName='" + getInsuranceCompanyName() + "'" +
            ", patientInsuranceId='" + getPatientInsuranceId() + "'" +
            ", insurancePolicyNumber='" + getInsurancePolicyNumber() + "'" +
            ", insuranceCoverageType='" + getInsuranceCoverageType() + "'" +
            ", insuranceValidFrom='" + getInsuranceValidFrom() + "'" +
            ", insuranceValidTo='" + getInsuranceValidTo() + "'" +
            "}";
    }
}
