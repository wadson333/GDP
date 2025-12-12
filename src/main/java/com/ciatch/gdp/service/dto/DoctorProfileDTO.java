package com.ciatch.gdp.service.dto;

import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.DoctorStatus;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.MedicalSpecialty;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.ciatch.gdp.domain.DoctorProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorProfileDTO implements Serializable {

    private Long id;

    @Size(max = 10)
    private String codeClinic;

    @NotNull
    private UUID uid;

    @NotNull
    @Size(min = 5, max = 50)
    private String medicalLicenseNumber;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

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

    @NotNull
    private DoctorStatus status;

    private Boolean isVerified;

    private Instant verifiedAt;

    @Size(min = 10, max = 10)
    private String nif;

    @Size(min = 10, max = 10)
    private String ninu;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double averageRating;

    @Min(value = 0)
    private Integer reviewCount;

    private Long version;

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

    public String getCodeClinic() {
        return codeClinic;
    }

    public void setCodeClinic(String codeClinic) {
        this.codeClinic = codeClinic;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
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

    public DoctorStatus getStatus() {
        return status;
    }

    public void setStatus(DoctorStatus status) {
        this.status = status;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
        if (!(o instanceof DoctorProfileDTO)) {
            return false;
        }

        DoctorProfileDTO doctorProfileDTO = (DoctorProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, doctorProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorProfileDTO{" +
            "id=" + getId() +
            ", codeClinic='" + getCodeClinic() + "'" +
            ", uid='" + getUid() + "'" +
            ", medicalLicenseNumber='" + getMedicalLicenseNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", bloodType='" + getBloodType() + "'" +
            ", primarySpecialty='" + getPrimarySpecialty() + "'" +
            ", otherSpecialties='" + getOtherSpecialties() + "'" +
            ", university='" + getUniversity() + "'" +
            ", graduationYear=" + getGraduationYear() +
            ", startDateOfPractice='" + getStartDateOfPractice() + "'" +
            ", consultationDurationMinutes=" + getConsultationDurationMinutes() +
            ", acceptingNewPatients='" + getAcceptingNewPatients() + "'" +
            ", allowsTeleconsultation='" + getAllowsTeleconsultation() + "'" +
            ", consultationFee=" + getConsultationFee() +
            ", teleconsultationFee=" + getTeleconsultationFee() +
            ", bio='" + getBio() + "'" +
            ", spokenLanguages='" + getSpokenLanguages() + "'" +
            ", websiteUrl='" + getWebsiteUrl() + "'" +
            ", officePhone='" + getOfficePhone() + "'" +
            ", officeAddress='" + getOfficeAddress() + "'" +
            ", status='" + getStatus() + "'" +
            ", isVerified='" + getIsVerified() + "'" +
            ", verifiedAt='" + getVerifiedAt() + "'" +
            ", nif='" + getNif() + "'" +
            ", ninu='" + getNinu() + "'" +
            ", averageRating=" + getAverageRating() +
            ", reviewCount=" + getReviewCount() +
            ", version=" + getVersion() +
            ", user=" + getUser() +
            "}";
    }
}
