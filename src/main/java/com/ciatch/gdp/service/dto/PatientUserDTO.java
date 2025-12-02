package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.config.Constants;
import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.PatientStatus;
import com.ciatch.gdp.domain.enumeration.SmokingStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A DTO representing a Patient and their associated User account.
 * Combines all fields from AdminUserDTO and PatientDTO.
 */
public class PatientUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ========== SYSTEM FIELDS (READ-ONLY for UPDATE) ==========
    private UUID uid; // Required for updates, ignored for creates

    // ========== USER FIELDS ==========
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login; // READ-ONLY on update

    @NotNull
    @Size(max = 50)
    private String firstName;

    @NotNull
    @Size(max = 50)
    private String lastName;

    @NotNull
    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(min = 2, max = 10)
    private String langKey;

    // ========== PATIENT BASIC INFO ==========
    @NotNull
    private LocalDate birthDate;

    private Gender gender;

    private BloodType bloodType;

    private PatientStatus status;

    // ========== CONTACT INFORMATION ==========
    private String address;

    @NotNull
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phone1;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phone2;

    // ========== IDENTITY DOCUMENTS ==========
    @Size(min = 10, max = 10)
    private String nif;

    @Size(min = 10, max = 10)
    private String ninu;

    @Size(min = 3, max = 15)
    private String passportNumber;

    // ========== PHYSICAL PARAMETERS ==========
    @Min(value = 0)
    @Max(value = 300)
    private Integer heightCm;

    @DecimalMin(value = "0")
    @DecimalMax(value = "500")
    private BigDecimal weightKg;

    // ========== EMERGENCY CONTACT ==========
    @Size(max = 100)
    private String contactPersonName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String contactPersonPhone;

    // ========== MEDICAL INFORMATION ==========
    private String antecedents;

    private String allergies;

    private String clinicalNotes;

    private SmokingStatus smokingStatus;

    // ========== GDPR & LEGAL ==========
    private ZonedDateTime gdprConsentDate;

    private ZonedDateTime deceasedDate;

    // ========== INSURANCE INFORMATION ==========
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

    // ========== CONTROL FLAGS (CREATE-ONLY) ==========
    private boolean sendActivationEmail = true;

    private boolean activatedOnCreate = false;

    // ========== GETTERS AND SETTERS ==========

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
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

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus status) {
        this.status = status;
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

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
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

    public boolean isSendActivationEmail() {
        return sendActivationEmail;
    }

    public void setSendActivationEmail(boolean sendActivationEmail) {
        this.sendActivationEmail = sendActivationEmail;
    }

    public boolean isActivatedOnCreate() {
        return activatedOnCreate;
    }

    public void setActivatedOnCreate(boolean activatedOnCreate) {
        this.activatedOnCreate = activatedOnCreate;
    }

    @Override
    public String toString() {
        return (
            "PatientUserDTO{" +
            "uid=" +
            uid +
            ", login='" +
            login +
            '\'' +
            ", firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", birthDate=" +
            birthDate +
            ", gender=" +
            gender +
            ", nif='" +
            (nif != null ? "***" : "null") +
            '\'' +
            ", status=" +
            status +
            '}'
        );
    }
}
