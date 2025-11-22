package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.PatientStatus;
import com.ciatch.gdp.domain.enumeration.SmokingStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.Patient} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientDTO implements Serializable {

    private Long id;

    private UUID uid;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    private Gender gender;

    private BloodType bloodType;

    // @Lob
    private String address;

    @NotNull
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phone1;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phone2;

    @Size(min = 10, max = 10)
    private String nif;

    @Size(min = 10, max = 10)
    private String ninu;

    private String medicalRecordNumber;

    @Min(value = 0)
    @Max(value = 300)
    private Integer heightCm;

    @DecimalMin(value = "0")
    @DecimalMax(value = "500")
    private BigDecimal weightKg;

    @Size(min = 3, max = 15)
    private String passportNumber;

    @Size(max = 100)
    private String contactPersonName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String contactPersonPhone;

    // @Lob
    private String antecedents;

    // @Lob
    private String allergies;

    // @Lob
    private String clinicalNotes;

    private SmokingStatus smokingStatus;

    private ZonedDateTime gdprConsentDate;

    private PatientStatus status;

    private ZonedDateTime deceasedDate;

    @Size(max = 200)
    private String insuranceCompanyName;

    @Size(max = 100)
    private String patientInsuranceId;

    @Size(max = 100)
    private String insurancePolicyNumber;

    @Size(max = 100)
    private String insuranceCoverageType;

    private LocalDate insuranceValidFrom;

    private LocalDate insuranceValidTo;

    @NotNull
    private UserDTO user;

    // Champs d'audit (lecture seule)
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNinu() {
        return ninu;
    }

    public void setNinu(String ninu) {
        this.ninu = ninu;
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(String medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public Integer getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Integer heightCm) {
        this.heightCm = heightCm;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public SmokingStatus getSmokingStatus() {
        return smokingStatus;
    }

    public void setSmokingStatus(SmokingStatus smokingStatus) {
        this.smokingStatus = smokingStatus;
    }

    public ZonedDateTime getGdprConsentDate() {
        return gdprConsentDate;
    }

    public void setGdprConsentDate(ZonedDateTime gdprConsentDate) {
        this.gdprConsentDate = gdprConsentDate;
    }

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDeceasedDate() {
        return deceasedDate;
    }

    public void setDeceasedDate(ZonedDateTime deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getPatientInsuranceId() {
        return patientInsuranceId;
    }

    public void setPatientInsuranceId(String patientInsuranceId) {
        this.patientInsuranceId = patientInsuranceId;
    }

    public String getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public void setInsurancePolicyNumber(String insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

    public String getInsuranceCoverageType() {
        return insuranceCoverageType;
    }

    public void setInsuranceCoverageType(String insuranceCoverageType) {
        this.insuranceCoverageType = insuranceCoverageType;
    }

    public LocalDate getInsuranceValidFrom() {
        return insuranceValidFrom;
    }

    public void setInsuranceValidFrom(LocalDate insuranceValidFrom) {
        this.insuranceValidFrom = insuranceValidFrom;
    }

    public LocalDate getInsuranceValidTo() {
        return insuranceValidTo;
    }

    public void setInsuranceValidTo(LocalDate insuranceValidTo) {
        this.insuranceValidTo = insuranceValidTo;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientDTO)) {
            return false;
        }

        PatientDTO patientDTO = (PatientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDTO{" +
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
            ", user=" + getUser() +
            "}";
    }
}
