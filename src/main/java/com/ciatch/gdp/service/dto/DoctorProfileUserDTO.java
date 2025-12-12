package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.config.Constants;
import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.MedicalSpecialty;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A DTO combining User and DoctorProfile data for atomic creation.
 */
public class DoctorProfileUserDTO implements Serializable {

    // ========== SYSTEM FIELDS (READ-ONLY for UPDATE) ==========
    private UUID uid; // Required for updates, ignored for creates
    private String codeClinic;

    // User fields
    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(min = 2, max = 10)
    private String langKey;

    private boolean sendActivationEmail = true;

    private boolean activatedOnCreate = false;

    // DoctorProfile fields (uid and codeClinic excluded - will be generated)
    @NotNull
    @Size(min = 5, max = 50)
    private String medicalLicenseNumber;

    @NotNull
    private LocalDate birthDate;

    private Gender gender;

    private BloodType bloodType;

    @NotNull
    private MedicalSpecialty primarySpecialty;

    @Lob
    private String otherSpecialties;

    @Size(max = 100)
    private String university;

    @Min(value = 1950)
    @Max(value = 2100)
    private Integer graduationYear;

    @NotNull
    private LocalDate startDateOfPractice;

    @NotNull
    @Min(value = 5)
    @Max(value = 120)
    private Integer consultationDurationMinutes;

    @NotNull
    private Boolean acceptingNewPatients;

    @NotNull
    private Boolean allowsTeleconsultation;

    @DecimalMin(value = "0")
    private BigDecimal consultationFee;

    @DecimalMin(value = "0")
    private BigDecimal teleconsultationFee;

    @Lob
    private String bio;

    @Size(max = 255)
    private String spokenLanguages;

    @Size(max = 255)
    private String websiteUrl;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String officePhone;

    @Lob
    private String officeAddress;

    @Size(min = 10, max = 10)
    private String nif;

    @Size(min = 10, max = 10)
    private String ninu;

    // ========== GETTERS AND SETTERS ==========

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getCodeClinic() {
        return codeClinic;
    }

    public void setCodeClinic(String codeClinic) {
        this.codeClinic = codeClinic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
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

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
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

    public MedicalSpecialty getPrimarySpecialty() {
        return primarySpecialty;
    }

    public void setPrimarySpecialty(MedicalSpecialty primarySpecialty) {
        this.primarySpecialty = primarySpecialty;
    }

    public String getOtherSpecialties() {
        return otherSpecialties;
    }

    public void setOtherSpecialties(String otherSpecialties) {
        this.otherSpecialties = otherSpecialties;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public LocalDate getStartDateOfPractice() {
        return startDateOfPractice;
    }

    public void setStartDateOfPractice(LocalDate startDateOfPractice) {
        this.startDateOfPractice = startDateOfPractice;
    }

    public Integer getConsultationDurationMinutes() {
        return consultationDurationMinutes;
    }

    public void setConsultationDurationMinutes(Integer consultationDurationMinutes) {
        this.consultationDurationMinutes = consultationDurationMinutes;
    }

    public Boolean getAcceptingNewPatients() {
        return acceptingNewPatients;
    }

    public void setAcceptingNewPatients(Boolean acceptingNewPatients) {
        this.acceptingNewPatients = acceptingNewPatients;
    }

    public Boolean getAllowsTeleconsultation() {
        return allowsTeleconsultation;
    }

    public void setAllowsTeleconsultation(Boolean allowsTeleconsultation) {
        this.allowsTeleconsultation = allowsTeleconsultation;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimal getTeleconsultationFee() {
        return teleconsultationFee;
    }

    public void setTeleconsultationFee(BigDecimal teleconsultationFee) {
        this.teleconsultationFee = teleconsultationFee;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
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

    @Override
    public String toString() {
        return (
            "DoctorProfileUserDTO{" +
            "login='" +
            login +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", medicalLicenseNumber='" +
            medicalLicenseNumber +
            '\'' +
            ", primarySpecialty=" +
            primarySpecialty +
            '}'
        );
    }
}
