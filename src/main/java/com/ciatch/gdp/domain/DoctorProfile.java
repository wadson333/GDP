package com.ciatch.gdp.domain;

import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.DoctorStatus;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.MedicalSpecialty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DoctorProfile.
 */
@Entity
@Table(name = "doctor_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorProfile extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 10)
    @Column(name = "code_clinic", length = 10)
    private String codeClinic;

    @NotNull
    @Column(name = "uid", nullable = false, unique = true)
    private UUID uid;

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "medical_license_number", length = 50, nullable = false, unique = true)
    private String medicalLicenseNumber;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "primary_specialty", nullable = false)
    private MedicalSpecialty primarySpecialty;

    // @Lob
    @Column(name = "other_specialties")
    private String otherSpecialties;

    @Size(max = 100)
    @Column(name = "university", length = 100)
    private String university;

    @Min(value = 1950)
    @Max(value = 2100)
    @Column(name = "graduation_year")
    private Integer graduationYear;

    @NotNull
    @Column(name = "start_date_of_practice", nullable = false)
    private LocalDate startDateOfPractice;

    @NotNull
    @Min(value = 5)
    @Max(value = 120)
    @Column(name = "consultation_duration_minutes", nullable = false)
    private Integer consultationDurationMinutes;

    @NotNull
    @Column(name = "accepting_new_patients", nullable = false)
    private Boolean acceptingNewPatients;

    @NotNull
    @Column(name = "allows_teleconsultation", nullable = false)
    private Boolean allowsTeleconsultation;

    @DecimalMin(value = "0")
    @Column(name = "consultation_fee", precision = 21, scale = 2)
    private BigDecimal consultationFee;

    @DecimalMin(value = "0")
    @Column(name = "teleconsultation_fee", precision = 21, scale = 2)
    private BigDecimal teleconsultationFee;

    // @Lob
    @Column(name = "bio")
    private String bio;

    @Size(max = 255)
    @Column(name = "spoken_languages", length = 255)
    private String spokenLanguages;

    @Size(max = 255)
    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(name = "office_phone")
    private String officePhone;

    // @Lob
    @Column(name = "office_address")
    private String officeAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DoctorStatus status;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Size(min = 10, max = 10)
    @Column(name = "nif", length = 10, unique = true)
    private String nif;

    @Size(min = 10, max = 10)
    @Column(name = "ninu", length = 10, unique = true)
    private String ninu;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "average_rating")
    private Double averageRating;

    @Min(value = 0)
    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "version")
    private Long version;

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

    public String getCodeClinic() {
        return this.codeClinic;
    }

    public DoctorProfile codeClinic(String codeClinic) {
        this.setCodeClinic(codeClinic);
        return this;
    }

    public void setCodeClinic(String codeClinic) {
        this.codeClinic = codeClinic;
    }

    public UUID getUid() {
        return this.uid;
    }

    public DoctorProfile uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getMedicalLicenseNumber() {
        return this.medicalLicenseNumber;
    }

    public DoctorProfile medicalLicenseNumber(String medicalLicenseNumber) {
        this.setMedicalLicenseNumber(medicalLicenseNumber);
        return this;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public DoctorProfile firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public DoctorProfile lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public DoctorProfile birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public DoctorProfile gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BloodType getBloodType() {
        return this.bloodType;
    }

    public DoctorProfile bloodType(BloodType bloodType) {
        this.setBloodType(bloodType);
        return this;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public MedicalSpecialty getPrimarySpecialty() {
        return this.primarySpecialty;
    }

    public DoctorProfile primarySpecialty(MedicalSpecialty primarySpecialty) {
        this.setPrimarySpecialty(primarySpecialty);
        return this;
    }

    public void setPrimarySpecialty(MedicalSpecialty primarySpecialty) {
        this.primarySpecialty = primarySpecialty;
    }

    public String getOtherSpecialties() {
        return this.otherSpecialties;
    }

    public DoctorProfile otherSpecialties(String otherSpecialties) {
        this.setOtherSpecialties(otherSpecialties);
        return this;
    }

    public void setOtherSpecialties(String otherSpecialties) {
        this.otherSpecialties = otherSpecialties;
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

    public Integer getGraduationYear() {
        return this.graduationYear;
    }

    public DoctorProfile graduationYear(Integer graduationYear) {
        this.setGraduationYear(graduationYear);
        return this;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
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

    public Integer getConsultationDurationMinutes() {
        return this.consultationDurationMinutes;
    }

    public DoctorProfile consultationDurationMinutes(Integer consultationDurationMinutes) {
        this.setConsultationDurationMinutes(consultationDurationMinutes);
        return this;
    }

    public void setConsultationDurationMinutes(Integer consultationDurationMinutes) {
        this.consultationDurationMinutes = consultationDurationMinutes;
    }

    public Boolean getAcceptingNewPatients() {
        return this.acceptingNewPatients;
    }

    public DoctorProfile acceptingNewPatients(Boolean acceptingNewPatients) {
        this.setAcceptingNewPatients(acceptingNewPatients);
        return this;
    }

    public void setAcceptingNewPatients(Boolean acceptingNewPatients) {
        this.acceptingNewPatients = acceptingNewPatients;
    }

    public Boolean getAllowsTeleconsultation() {
        return this.allowsTeleconsultation;
    }

    public DoctorProfile allowsTeleconsultation(Boolean allowsTeleconsultation) {
        this.setAllowsTeleconsultation(allowsTeleconsultation);
        return this;
    }

    public void setAllowsTeleconsultation(Boolean allowsTeleconsultation) {
        this.allowsTeleconsultation = allowsTeleconsultation;
    }

    public BigDecimal getConsultationFee() {
        return this.consultationFee;
    }

    public DoctorProfile consultationFee(BigDecimal consultationFee) {
        this.setConsultationFee(consultationFee);
        return this;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimal getTeleconsultationFee() {
        return this.teleconsultationFee;
    }

    public DoctorProfile teleconsultationFee(BigDecimal teleconsultationFee) {
        this.setTeleconsultationFee(teleconsultationFee);
        return this;
    }

    public void setTeleconsultationFee(BigDecimal teleconsultationFee) {
        this.teleconsultationFee = teleconsultationFee;
    }

    public String getBio() {
        return this.bio;
    }

    public DoctorProfile bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpokenLanguages() {
        return this.spokenLanguages;
    }

    public DoctorProfile spokenLanguages(String spokenLanguages) {
        this.setSpokenLanguages(spokenLanguages);
        return this;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public DoctorProfile websiteUrl(String websiteUrl) {
        this.setWebsiteUrl(websiteUrl);
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getOfficePhone() {
        return this.officePhone;
    }

    public DoctorProfile officePhone(String officePhone) {
        this.setOfficePhone(officePhone);
        return this;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeAddress() {
        return this.officeAddress;
    }

    public DoctorProfile officeAddress(String officeAddress) {
        this.setOfficeAddress(officeAddress);
        return this;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public DoctorStatus getStatus() {
        return this.status;
    }

    public DoctorProfile status(DoctorStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DoctorStatus status) {
        this.status = status;
    }

    public Boolean getIsVerified() {
        return this.isVerified;
    }

    public DoctorProfile isVerified(Boolean isVerified) {
        this.setIsVerified(isVerified);
        return this;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Instant getVerifiedAt() {
        return this.verifiedAt;
    }

    public DoctorProfile verifiedAt(Instant verifiedAt) {
        this.setVerifiedAt(verifiedAt);
        return this;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getNif() {
        return this.nif;
    }

    public DoctorProfile nif(String nif) {
        this.setNif(nif);
        return this;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNinu() {
        return this.ninu;
    }

    public DoctorProfile ninu(String ninu) {
        this.setNinu(ninu);
        return this;
    }

    public void setNinu(String ninu) {
        this.ninu = ninu;
    }

    public Double getAverageRating() {
        return this.averageRating;
    }

    public DoctorProfile averageRating(Double averageRating) {
        this.setAverageRating(averageRating);
        return this;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return this.reviewCount;
    }

    public DoctorProfile reviewCount(Integer reviewCount) {
        this.setReviewCount(reviewCount);
        return this;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getVersion() {
        return this.version;
    }

    public DoctorProfile version(Long version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Long version) {
        this.version = version;
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
            "}";
    }
}
