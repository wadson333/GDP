package com.ciatch.gdp.service.criteria;

import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.DoctorStatus;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.MedicalSpecialty;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ciatch.gdp.domain.DoctorProfile} entity. This class is used
 * in {@link com.ciatch.gdp.web.rest.DoctorProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doctor-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering BloodType
     */
    public static class BloodTypeFilter extends Filter<BloodType> {

        public BloodTypeFilter() {}

        public BloodTypeFilter(BloodTypeFilter filter) {
            super(filter);
        }

        @Override
        public BloodTypeFilter copy() {
            return new BloodTypeFilter(this);
        }
    }

    /**
     * Class for filtering MedicalSpecialty
     */
    public static class MedicalSpecialtyFilter extends Filter<MedicalSpecialty> {

        public MedicalSpecialtyFilter() {}

        public MedicalSpecialtyFilter(MedicalSpecialtyFilter filter) {
            super(filter);
        }

        @Override
        public MedicalSpecialtyFilter copy() {
            return new MedicalSpecialtyFilter(this);
        }
    }

    /**
     * Class for filtering DoctorStatus
     */
    public static class DoctorStatusFilter extends Filter<DoctorStatus> {

        public DoctorStatusFilter() {}

        public DoctorStatusFilter(DoctorStatusFilter filter) {
            super(filter);
        }

        @Override
        public DoctorStatusFilter copy() {
            return new DoctorStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private StringFilter globalFilter;

    private LongFilter id;

    private StringFilter codeClinic;

    private UUIDFilter uid;

    private StringFilter medicalLicenseNumber;

    private StringFilter firstName;

    private StringFilter lastName;

    private LocalDateFilter birthDate;

    private GenderFilter gender;

    private BloodTypeFilter bloodType;

    private MedicalSpecialtyFilter primarySpecialty;

    private StringFilter university;

    private IntegerFilter graduationYear;

    private LocalDateFilter startDateOfPractice;

    private IntegerFilter consultationDurationMinutes;

    private BooleanFilter acceptingNewPatients;

    private BooleanFilter allowsTeleconsultation;

    private BigDecimalFilter consultationFee;

    private BigDecimalFilter teleconsultationFee;

    private StringFilter spokenLanguages;

    private StringFilter websiteUrl;

    private StringFilter officePhone;

    private DoctorStatusFilter status;

    private BooleanFilter isVerified;

    private InstantFilter verifiedAt;

    private StringFilter nif;

    private StringFilter ninu;

    private DoubleFilter averageRating;

    private IntegerFilter reviewCount;

    private LongFilter version;

    private LongFilter userId;

    private Boolean distinct;

    public DoctorProfileCriteria() {}

    public DoctorProfileCriteria(DoctorProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.globalFilter = other.globalFilter == null ? null : other.globalFilter.copy();
        this.codeClinic = other.optionalCodeClinic().map(StringFilter::copy).orElse(null);
        this.uid = other.optionalUid().map(UUIDFilter::copy).orElse(null);
        this.medicalLicenseNumber = other.optionalMedicalLicenseNumber().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.bloodType = other.optionalBloodType().map(BloodTypeFilter::copy).orElse(null);
        this.primarySpecialty = other.optionalPrimarySpecialty().map(MedicalSpecialtyFilter::copy).orElse(null);
        this.university = other.optionalUniversity().map(StringFilter::copy).orElse(null);
        this.graduationYear = other.optionalGraduationYear().map(IntegerFilter::copy).orElse(null);
        this.startDateOfPractice = other.optionalStartDateOfPractice().map(LocalDateFilter::copy).orElse(null);
        this.consultationDurationMinutes = other.optionalConsultationDurationMinutes().map(IntegerFilter::copy).orElse(null);
        this.acceptingNewPatients = other.optionalAcceptingNewPatients().map(BooleanFilter::copy).orElse(null);
        this.allowsTeleconsultation = other.optionalAllowsTeleconsultation().map(BooleanFilter::copy).orElse(null);
        this.consultationFee = other.optionalConsultationFee().map(BigDecimalFilter::copy).orElse(null);
        this.teleconsultationFee = other.optionalTeleconsultationFee().map(BigDecimalFilter::copy).orElse(null);
        this.spokenLanguages = other.optionalSpokenLanguages().map(StringFilter::copy).orElse(null);
        this.websiteUrl = other.optionalWebsiteUrl().map(StringFilter::copy).orElse(null);
        this.officePhone = other.optionalOfficePhone().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(DoctorStatusFilter::copy).orElse(null);
        this.isVerified = other.optionalIsVerified().map(BooleanFilter::copy).orElse(null);
        this.verifiedAt = other.optionalVerifiedAt().map(InstantFilter::copy).orElse(null);
        this.nif = other.optionalNif().map(StringFilter::copy).orElse(null);
        this.ninu = other.optionalNinu().map(StringFilter::copy).orElse(null);
        this.averageRating = other.optionalAverageRating().map(DoubleFilter::copy).orElse(null);
        this.reviewCount = other.optionalReviewCount().map(IntegerFilter::copy).orElse(null);
        this.version = other.optionalVersion().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DoctorProfileCriteria copy() {
        return new DoctorProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCodeClinic() {
        return codeClinic;
    }

    public Optional<StringFilter> optionalCodeClinic() {
        return Optional.ofNullable(codeClinic);
    }

    public StringFilter codeClinic() {
        if (codeClinic == null) {
            setCodeClinic(new StringFilter());
        }
        return codeClinic;
    }

    public void setCodeClinic(StringFilter codeClinic) {
        this.codeClinic = codeClinic;
    }

    public UUIDFilter getUid() {
        return uid;
    }

    public Optional<UUIDFilter> optionalUid() {
        return Optional.ofNullable(uid);
    }

    public UUIDFilter uid() {
        if (uid == null) {
            setUid(new UUIDFilter());
        }
        return uid;
    }

    public void setUid(UUIDFilter uid) {
        this.uid = uid;
    }

    public StringFilter getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public Optional<StringFilter> optionalMedicalLicenseNumber() {
        return Optional.ofNullable(medicalLicenseNumber);
    }

    public StringFilter medicalLicenseNumber() {
        if (medicalLicenseNumber == null) {
            setMedicalLicenseNumber(new StringFilter());
        }
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(StringFilter medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public Optional<LocalDateFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new LocalDateFilter());
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public Optional<GenderFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderFilter gender() {
        if (gender == null) {
            setGender(new GenderFilter());
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public BloodTypeFilter getBloodType() {
        return bloodType;
    }

    public Optional<BloodTypeFilter> optionalBloodType() {
        return Optional.ofNullable(bloodType);
    }

    public BloodTypeFilter bloodType() {
        if (bloodType == null) {
            setBloodType(new BloodTypeFilter());
        }
        return bloodType;
    }

    public void setBloodType(BloodTypeFilter bloodType) {
        this.bloodType = bloodType;
    }

    public MedicalSpecialtyFilter getPrimarySpecialty() {
        return primarySpecialty;
    }

    public Optional<MedicalSpecialtyFilter> optionalPrimarySpecialty() {
        return Optional.ofNullable(primarySpecialty);
    }

    public MedicalSpecialtyFilter primarySpecialty() {
        if (primarySpecialty == null) {
            setPrimarySpecialty(new MedicalSpecialtyFilter());
        }
        return primarySpecialty;
    }

    public void setPrimarySpecialty(MedicalSpecialtyFilter primarySpecialty) {
        this.primarySpecialty = primarySpecialty;
    }

    public StringFilter getUniversity() {
        return university;
    }

    public Optional<StringFilter> optionalUniversity() {
        return Optional.ofNullable(university);
    }

    public StringFilter university() {
        if (university == null) {
            setUniversity(new StringFilter());
        }
        return university;
    }

    public void setUniversity(StringFilter university) {
        this.university = university;
    }

    public IntegerFilter getGraduationYear() {
        return graduationYear;
    }

    public Optional<IntegerFilter> optionalGraduationYear() {
        return Optional.ofNullable(graduationYear);
    }

    public IntegerFilter graduationYear() {
        if (graduationYear == null) {
            setGraduationYear(new IntegerFilter());
        }
        return graduationYear;
    }

    public void setGraduationYear(IntegerFilter graduationYear) {
        this.graduationYear = graduationYear;
    }

    public LocalDateFilter getStartDateOfPractice() {
        return startDateOfPractice;
    }

    public Optional<LocalDateFilter> optionalStartDateOfPractice() {
        return Optional.ofNullable(startDateOfPractice);
    }

    public LocalDateFilter startDateOfPractice() {
        if (startDateOfPractice == null) {
            setStartDateOfPractice(new LocalDateFilter());
        }
        return startDateOfPractice;
    }

    public void setStartDateOfPractice(LocalDateFilter startDateOfPractice) {
        this.startDateOfPractice = startDateOfPractice;
    }

    public IntegerFilter getConsultationDurationMinutes() {
        return consultationDurationMinutes;
    }

    public Optional<IntegerFilter> optionalConsultationDurationMinutes() {
        return Optional.ofNullable(consultationDurationMinutes);
    }

    public IntegerFilter consultationDurationMinutes() {
        if (consultationDurationMinutes == null) {
            setConsultationDurationMinutes(new IntegerFilter());
        }
        return consultationDurationMinutes;
    }

    public void setConsultationDurationMinutes(IntegerFilter consultationDurationMinutes) {
        this.consultationDurationMinutes = consultationDurationMinutes;
    }

    public BooleanFilter getAcceptingNewPatients() {
        return acceptingNewPatients;
    }

    public Optional<BooleanFilter> optionalAcceptingNewPatients() {
        return Optional.ofNullable(acceptingNewPatients);
    }

    public BooleanFilter acceptingNewPatients() {
        if (acceptingNewPatients == null) {
            setAcceptingNewPatients(new BooleanFilter());
        }
        return acceptingNewPatients;
    }

    public void setAcceptingNewPatients(BooleanFilter acceptingNewPatients) {
        this.acceptingNewPatients = acceptingNewPatients;
    }

    public BooleanFilter getAllowsTeleconsultation() {
        return allowsTeleconsultation;
    }

    public Optional<BooleanFilter> optionalAllowsTeleconsultation() {
        return Optional.ofNullable(allowsTeleconsultation);
    }

    public BooleanFilter allowsTeleconsultation() {
        if (allowsTeleconsultation == null) {
            setAllowsTeleconsultation(new BooleanFilter());
        }
        return allowsTeleconsultation;
    }

    public void setAllowsTeleconsultation(BooleanFilter allowsTeleconsultation) {
        this.allowsTeleconsultation = allowsTeleconsultation;
    }

    public BigDecimalFilter getConsultationFee() {
        return consultationFee;
    }

    public Optional<BigDecimalFilter> optionalConsultationFee() {
        return Optional.ofNullable(consultationFee);
    }

    public BigDecimalFilter consultationFee() {
        if (consultationFee == null) {
            setConsultationFee(new BigDecimalFilter());
        }
        return consultationFee;
    }

    public void setConsultationFee(BigDecimalFilter consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimalFilter getTeleconsultationFee() {
        return teleconsultationFee;
    }

    public Optional<BigDecimalFilter> optionalTeleconsultationFee() {
        return Optional.ofNullable(teleconsultationFee);
    }

    public BigDecimalFilter teleconsultationFee() {
        if (teleconsultationFee == null) {
            setTeleconsultationFee(new BigDecimalFilter());
        }
        return teleconsultationFee;
    }

    public void setTeleconsultationFee(BigDecimalFilter teleconsultationFee) {
        this.teleconsultationFee = teleconsultationFee;
    }

    public StringFilter getSpokenLanguages() {
        return spokenLanguages;
    }

    public Optional<StringFilter> optionalSpokenLanguages() {
        return Optional.ofNullable(spokenLanguages);
    }

    public StringFilter spokenLanguages() {
        if (spokenLanguages == null) {
            setSpokenLanguages(new StringFilter());
        }
        return spokenLanguages;
    }

    public void setSpokenLanguages(StringFilter spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public StringFilter getWebsiteUrl() {
        return websiteUrl;
    }

    public Optional<StringFilter> optionalWebsiteUrl() {
        return Optional.ofNullable(websiteUrl);
    }

    public StringFilter websiteUrl() {
        if (websiteUrl == null) {
            setWebsiteUrl(new StringFilter());
        }
        return websiteUrl;
    }

    public void setWebsiteUrl(StringFilter websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public StringFilter getOfficePhone() {
        return officePhone;
    }

    public Optional<StringFilter> optionalOfficePhone() {
        return Optional.ofNullable(officePhone);
    }

    public StringFilter officePhone() {
        if (officePhone == null) {
            setOfficePhone(new StringFilter());
        }
        return officePhone;
    }

    public void setOfficePhone(StringFilter officePhone) {
        this.officePhone = officePhone;
    }

    public DoctorStatusFilter getStatus() {
        return status;
    }

    public Optional<DoctorStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public DoctorStatusFilter status() {
        if (status == null) {
            setStatus(new DoctorStatusFilter());
        }
        return status;
    }

    public void setStatus(DoctorStatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getIsVerified() {
        return isVerified;
    }

    public Optional<BooleanFilter> optionalIsVerified() {
        return Optional.ofNullable(isVerified);
    }

    public BooleanFilter isVerified() {
        if (isVerified == null) {
            setIsVerified(new BooleanFilter());
        }
        return isVerified;
    }

    public void setIsVerified(BooleanFilter isVerified) {
        this.isVerified = isVerified;
    }

    public InstantFilter getVerifiedAt() {
        return verifiedAt;
    }

    public Optional<InstantFilter> optionalVerifiedAt() {
        return Optional.ofNullable(verifiedAt);
    }

    public InstantFilter verifiedAt() {
        if (verifiedAt == null) {
            setVerifiedAt(new InstantFilter());
        }
        return verifiedAt;
    }

    public void setVerifiedAt(InstantFilter verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public StringFilter getNif() {
        return nif;
    }

    public Optional<StringFilter> optionalNif() {
        return Optional.ofNullable(nif);
    }

    public StringFilter nif() {
        if (nif == null) {
            setNif(new StringFilter());
        }
        return nif;
    }

    public void setNif(StringFilter nif) {
        this.nif = nif;
    }

    public StringFilter getNinu() {
        return ninu;
    }

    public Optional<StringFilter> optionalNinu() {
        return Optional.ofNullable(ninu);
    }

    public StringFilter ninu() {
        if (ninu == null) {
            setNinu(new StringFilter());
        }
        return ninu;
    }

    public void setNinu(StringFilter ninu) {
        this.ninu = ninu;
    }

    public DoubleFilter getAverageRating() {
        return averageRating;
    }

    public Optional<DoubleFilter> optionalAverageRating() {
        return Optional.ofNullable(averageRating);
    }

    public DoubleFilter averageRating() {
        if (averageRating == null) {
            setAverageRating(new DoubleFilter());
        }
        return averageRating;
    }

    public void setAverageRating(DoubleFilter averageRating) {
        this.averageRating = averageRating;
    }

    public IntegerFilter getReviewCount() {
        return reviewCount;
    }

    public Optional<IntegerFilter> optionalReviewCount() {
        return Optional.ofNullable(reviewCount);
    }

    public IntegerFilter reviewCount() {
        if (reviewCount == null) {
            setReviewCount(new IntegerFilter());
        }
        return reviewCount;
    }

    public void setReviewCount(IntegerFilter reviewCount) {
        this.reviewCount = reviewCount;
    }

    public LongFilter getVersion() {
        return version;
    }

    public Optional<LongFilter> optionalVersion() {
        return Optional.ofNullable(version);
    }

    public LongFilter version() {
        if (version == null) {
            setVersion(new LongFilter());
        }
        return version;
    }

    public void setVersion(LongFilter version) {
        this.version = version;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(StringFilter globalFilter) {
        this.globalFilter = globalFilter;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DoctorProfileCriteria that = (DoctorProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeClinic, that.codeClinic) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(medicalLicenseNumber, that.medicalLicenseNumber) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(bloodType, that.bloodType) &&
            Objects.equals(primarySpecialty, that.primarySpecialty) &&
            Objects.equals(university, that.university) &&
            Objects.equals(graduationYear, that.graduationYear) &&
            Objects.equals(startDateOfPractice, that.startDateOfPractice) &&
            Objects.equals(consultationDurationMinutes, that.consultationDurationMinutes) &&
            Objects.equals(acceptingNewPatients, that.acceptingNewPatients) &&
            Objects.equals(allowsTeleconsultation, that.allowsTeleconsultation) &&
            Objects.equals(consultationFee, that.consultationFee) &&
            Objects.equals(teleconsultationFee, that.teleconsultationFee) &&
            Objects.equals(spokenLanguages, that.spokenLanguages) &&
            Objects.equals(websiteUrl, that.websiteUrl) &&
            Objects.equals(officePhone, that.officePhone) &&
            Objects.equals(status, that.status) &&
            Objects.equals(isVerified, that.isVerified) &&
            Objects.equals(verifiedAt, that.verifiedAt) &&
            Objects.equals(nif, that.nif) &&
            Objects.equals(ninu, that.ninu) &&
            Objects.equals(averageRating, that.averageRating) &&
            Objects.equals(reviewCount, that.reviewCount) &&
            Objects.equals(version, that.version) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codeClinic,
            uid,
            medicalLicenseNumber,
            firstName,
            lastName,
            birthDate,
            gender,
            bloodType,
            primarySpecialty,
            university,
            graduationYear,
            startDateOfPractice,
            consultationDurationMinutes,
            acceptingNewPatients,
            allowsTeleconsultation,
            consultationFee,
            teleconsultationFee,
            spokenLanguages,
            websiteUrl,
            officePhone,
            status,
            isVerified,
            verifiedAt,
            nif,
            ninu,
            averageRating,
            reviewCount,
            version,
            userId,
            distinct,
            globalFilter
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodeClinic().map(f -> "codeClinic=" + f + ", ").orElse("") +
            optionalUid().map(f -> "uid=" + f + ", ").orElse("") +
            optionalMedicalLicenseNumber().map(f -> "medicalLicenseNumber=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalBloodType().map(f -> "bloodType=" + f + ", ").orElse("") +
            optionalPrimarySpecialty().map(f -> "primarySpecialty=" + f + ", ").orElse("") +
            optionalUniversity().map(f -> "university=" + f + ", ").orElse("") +
            optionalGraduationYear().map(f -> "graduationYear=" + f + ", ").orElse("") +
            optionalStartDateOfPractice().map(f -> "startDateOfPractice=" + f + ", ").orElse("") +
            optionalConsultationDurationMinutes().map(f -> "consultationDurationMinutes=" + f + ", ").orElse("") +
            optionalAcceptingNewPatients().map(f -> "acceptingNewPatients=" + f + ", ").orElse("") +
            optionalAllowsTeleconsultation().map(f -> "allowsTeleconsultation=" + f + ", ").orElse("") +
            optionalConsultationFee().map(f -> "consultationFee=" + f + ", ").orElse("") +
            optionalTeleconsultationFee().map(f -> "teleconsultationFee=" + f + ", ").orElse("") +
            optionalSpokenLanguages().map(f -> "spokenLanguages=" + f + ", ").orElse("") +
            optionalWebsiteUrl().map(f -> "websiteUrl=" + f + ", ").orElse("") +
            optionalOfficePhone().map(f -> "officePhone=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalIsVerified().map(f -> "isVerified=" + f + ", ").orElse("") +
            optionalVerifiedAt().map(f -> "verifiedAt=" + f + ", ").orElse("") +
            optionalNif().map(f -> "nif=" + f + ", ").orElse("") +
            optionalNinu().map(f -> "ninu=" + f + ", ").orElse("") +
            optionalAverageRating().map(f -> "averageRating=" + f + ", ").orElse("") +
            optionalReviewCount().map(f -> "reviewCount=" + f + ", ").orElse("") +
            optionalVersion().map(f -> "version=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            globalFilter != null ? "globalFilter=" + globalFilter + ", " : "" +
        "}";
    }
}
