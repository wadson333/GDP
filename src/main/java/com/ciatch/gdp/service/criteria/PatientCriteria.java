package com.ciatch.gdp.service.criteria;

import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.PatientStatus;
import com.ciatch.gdp.domain.enumeration.SmokingStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ciatch.gdp.domain.Patient} entity. This class is used
 * in {@link com.ciatch.gdp.web.rest.PatientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatientCriteria implements Serializable, Criteria {

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
     * Class for filtering SmokingStatus
     */
    public static class SmokingStatusFilter extends Filter<SmokingStatus> {

        public SmokingStatusFilter() {}

        public SmokingStatusFilter(SmokingStatusFilter filter) {
            super(filter);
        }

        @Override
        public SmokingStatusFilter copy() {
            return new SmokingStatusFilter(this);
        }
    }

    /**
     * Class for filtering PatientStatus
     */
    public static class PatientStatusFilter extends Filter<PatientStatus> {

        public PatientStatusFilter() {}

        public PatientStatusFilter(PatientStatusFilter filter) {
            super(filter);
        }

        @Override
        public PatientStatusFilter copy() {
            return new PatientStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uid;

    private StringFilter firstName;

    private StringFilter lastName;

    private LocalDateFilter birthDate;

    private GenderFilter gender;

    private BloodTypeFilter bloodType;

    private StringFilter address;

    private StringFilter phone1;

    private StringFilter phone2;

    private StringFilter nif;

    private StringFilter ninu;

    private StringFilter medicalRecordNumber;

    private IntegerFilter heightCm;

    private BigDecimalFilter weightKg;

    private StringFilter passportNumber;

    private StringFilter contactPersonName;

    private StringFilter contactPersonPhone;

    private StringFilter antecedents;

    private StringFilter allergies;

    private StringFilter clinicalNotes;

    private SmokingStatusFilter smokingStatus;

    private ZonedDateTimeFilter gdprConsentDate;

    private PatientStatusFilter status;

    private ZonedDateTimeFilter deceasedDate;

    private StringFilter insuranceCompanyName;

    private StringFilter patientInsuranceId;

    private StringFilter insurancePolicyNumber;

    private StringFilter insuranceCoverageType;

    private LocalDateFilter insuranceValidFrom;

    private LocalDateFilter insuranceValidTo;

    private LongFilter userId;

    private StringFilter userEmail;

    private StringFilter userLogin;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private StringFilter fullTextSearch;

    private Boolean distinct;

    public PatientCriteria() {}

    public PatientCriteria(PatientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uid = other.optionalUid().map(UUIDFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.bloodType = other.optionalBloodType().map(BloodTypeFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.phone1 = other.optionalPhone1().map(StringFilter::copy).orElse(null);
        this.phone2 = other.optionalPhone2().map(StringFilter::copy).orElse(null);
        this.nif = other.optionalNif().map(StringFilter::copy).orElse(null);
        this.ninu = other.optionalNinu().map(StringFilter::copy).orElse(null);
        this.medicalRecordNumber = other.optionalMedicalRecordNumber().map(StringFilter::copy).orElse(null);
        this.heightCm = other.optionalHeightCm().map(IntegerFilter::copy).orElse(null);
        this.weightKg = other.optionalWeightKg().map(BigDecimalFilter::copy).orElse(null);
        this.passportNumber = other.optionalPassportNumber().map(StringFilter::copy).orElse(null);
        this.contactPersonName = other.optionalContactPersonName().map(StringFilter::copy).orElse(null);
        this.contactPersonPhone = other.optionalContactPersonPhone().map(StringFilter::copy).orElse(null);
        this.antecedents = other.optionalAntecedents().map(StringFilter::copy).orElse(null);
        this.allergies = other.optionalAllergies().map(StringFilter::copy).orElse(null);
        this.clinicalNotes = other.optionalClinicalNotes().map(StringFilter::copy).orElse(null);
        this.smokingStatus = other.optionalSmokingStatus().map(SmokingStatusFilter::copy).orElse(null);
        this.gdprConsentDate = other.optionalGdprConsentDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(PatientStatusFilter::copy).orElse(null);
        this.deceasedDate = other.optionalDeceasedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.insuranceCompanyName = other.optionalInsuranceCompanyName().map(StringFilter::copy).orElse(null);
        this.patientInsuranceId = other.optionalPatientInsuranceId().map(StringFilter::copy).orElse(null);
        this.insurancePolicyNumber = other.optionalInsurancePolicyNumber().map(StringFilter::copy).orElse(null);
        this.insuranceCoverageType = other.optionalInsuranceCoverageType().map(StringFilter::copy).orElse(null);
        this.insuranceValidFrom = other.optionalInsuranceValidFrom().map(LocalDateFilter::copy).orElse(null);
        this.insuranceValidTo = other.optionalInsuranceValidTo().map(LocalDateFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.userEmail = other.optionalUserEmail().map(StringFilter::copy).orElse(null);
        this.userLogin = other.optionalUserLogin().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.fullTextSearch = other.optionalFullTextSearch().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PatientCriteria copy() {
        return new PatientCriteria(this);
    }

    // Getters and Setters with Optional support

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

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone1() {
        return phone1;
    }

    public Optional<StringFilter> optionalPhone1() {
        return Optional.ofNullable(phone1);
    }

    public StringFilter phone1() {
        if (phone1 == null) {
            setPhone1(new StringFilter());
        }
        return phone1;
    }

    public void setPhone1(StringFilter phone1) {
        this.phone1 = phone1;
    }

    public StringFilter getPhone2() {
        return phone2;
    }

    public Optional<StringFilter> optionalPhone2() {
        return Optional.ofNullable(phone2);
    }

    public StringFilter phone2() {
        if (phone2 == null) {
            setPhone2(new StringFilter());
        }
        return phone2;
    }

    public void setPhone2(StringFilter phone2) {
        this.phone2 = phone2;
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

    public StringFilter getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public Optional<StringFilter> optionalMedicalRecordNumber() {
        return Optional.ofNullable(medicalRecordNumber);
    }

    public StringFilter medicalRecordNumber() {
        if (medicalRecordNumber == null) {
            setMedicalRecordNumber(new StringFilter());
        }
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(StringFilter medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public IntegerFilter getHeightCm() {
        return heightCm;
    }

    public Optional<IntegerFilter> optionalHeightCm() {
        return Optional.ofNullable(heightCm);
    }

    public IntegerFilter heightCm() {
        if (heightCm == null) {
            setHeightCm(new IntegerFilter());
        }
        return heightCm;
    }

    public void setHeightCm(IntegerFilter heightCm) {
        this.heightCm = heightCm;
    }

    public BigDecimalFilter getWeightKg() {
        return weightKg;
    }

    public Optional<BigDecimalFilter> optionalWeightKg() {
        return Optional.ofNullable(weightKg);
    }

    public BigDecimalFilter weightKg() {
        if (weightKg == null) {
            setWeightKg(new BigDecimalFilter());
        }
        return weightKg;
    }

    public void setWeightKg(BigDecimalFilter weightKg) {
        this.weightKg = weightKg;
    }

    public StringFilter getPassportNumber() {
        return passportNumber;
    }

    public Optional<StringFilter> optionalPassportNumber() {
        return Optional.ofNullable(passportNumber);
    }

    public StringFilter passportNumber() {
        if (passportNumber == null) {
            setPassportNumber(new StringFilter());
        }
        return passportNumber;
    }

    public void setPassportNumber(StringFilter passportNumber) {
        this.passportNumber = passportNumber;
    }

    public StringFilter getContactPersonName() {
        return contactPersonName;
    }

    public Optional<StringFilter> optionalContactPersonName() {
        return Optional.ofNullable(contactPersonName);
    }

    public StringFilter contactPersonName() {
        if (contactPersonName == null) {
            setContactPersonName(new StringFilter());
        }
        return contactPersonName;
    }

    public void setContactPersonName(StringFilter contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public StringFilter getContactPersonPhone() {
        return contactPersonPhone;
    }

    public Optional<StringFilter> optionalContactPersonPhone() {
        return Optional.ofNullable(contactPersonPhone);
    }

    public StringFilter contactPersonPhone() {
        if (contactPersonPhone == null) {
            setContactPersonPhone(new StringFilter());
        }
        return contactPersonPhone;
    }

    public void setContactPersonPhone(StringFilter contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public StringFilter getAntecedents() {
        return antecedents;
    }

    public Optional<StringFilter> optionalAntecedents() {
        return Optional.ofNullable(antecedents);
    }

    public StringFilter antecedents() {
        if (antecedents == null) {
            setAntecedents(new StringFilter());
        }
        return antecedents;
    }

    public void setAntecedents(StringFilter antecedents) {
        this.antecedents = antecedents;
    }

    public StringFilter getAllergies() {
        return allergies;
    }

    public Optional<StringFilter> optionalAllergies() {
        return Optional.ofNullable(allergies);
    }

    public StringFilter allergies() {
        if (allergies == null) {
            setAllergies(new StringFilter());
        }
        return allergies;
    }

    public void setAllergies(StringFilter allergies) {
        this.allergies = allergies;
    }

    public StringFilter getClinicalNotes() {
        return clinicalNotes;
    }

    public Optional<StringFilter> optionalClinicalNotes() {
        return Optional.ofNullable(clinicalNotes);
    }

    public StringFilter clinicalNotes() {
        if (clinicalNotes == null) {
            setClinicalNotes(new StringFilter());
        }
        return clinicalNotes;
    }

    public void setClinicalNotes(StringFilter clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public SmokingStatusFilter getSmokingStatus() {
        return smokingStatus;
    }

    public Optional<SmokingStatusFilter> optionalSmokingStatus() {
        return Optional.ofNullable(smokingStatus);
    }

    public SmokingStatusFilter smokingStatus() {
        if (smokingStatus == null) {
            setSmokingStatus(new SmokingStatusFilter());
        }
        return smokingStatus;
    }

    public void setSmokingStatus(SmokingStatusFilter smokingStatus) {
        this.smokingStatus = smokingStatus;
    }

    public ZonedDateTimeFilter getGdprConsentDate() {
        return gdprConsentDate;
    }

    public Optional<ZonedDateTimeFilter> optionalGdprConsentDate() {
        return Optional.ofNullable(gdprConsentDate);
    }

    public ZonedDateTimeFilter gdprConsentDate() {
        if (gdprConsentDate == null) {
            setGdprConsentDate(new ZonedDateTimeFilter());
        }
        return gdprConsentDate;
    }

    public void setGdprConsentDate(ZonedDateTimeFilter gdprConsentDate) {
        this.gdprConsentDate = gdprConsentDate;
    }

    public PatientStatusFilter getStatus() {
        return status;
    }

    public Optional<PatientStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public PatientStatusFilter status() {
        if (status == null) {
            setStatus(new PatientStatusFilter());
        }
        return status;
    }

    public void setStatus(PatientStatusFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getDeceasedDate() {
        return deceasedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalDeceasedDate() {
        return Optional.ofNullable(deceasedDate);
    }

    public ZonedDateTimeFilter deceasedDate() {
        if (deceasedDate == null) {
            setDeceasedDate(new ZonedDateTimeFilter());
        }
        return deceasedDate;
    }

    public void setDeceasedDate(ZonedDateTimeFilter deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public StringFilter getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public Optional<StringFilter> optionalInsuranceCompanyName() {
        return Optional.ofNullable(insuranceCompanyName);
    }

    public StringFilter insuranceCompanyName() {
        if (insuranceCompanyName == null) {
            setInsuranceCompanyName(new StringFilter());
        }
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(StringFilter insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public StringFilter getPatientInsuranceId() {
        return patientInsuranceId;
    }

    public Optional<StringFilter> optionalPatientInsuranceId() {
        return Optional.ofNullable(patientInsuranceId);
    }

    public StringFilter patientInsuranceId() {
        if (patientInsuranceId == null) {
            setPatientInsuranceId(new StringFilter());
        }
        return patientInsuranceId;
    }

    public void setPatientInsuranceId(StringFilter patientInsuranceId) {
        this.patientInsuranceId = patientInsuranceId;
    }

    public StringFilter getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public Optional<StringFilter> optionalInsurancePolicyNumber() {
        return Optional.ofNullable(insurancePolicyNumber);
    }

    public StringFilter insurancePolicyNumber() {
        if (insurancePolicyNumber == null) {
            setInsurancePolicyNumber(new StringFilter());
        }
        return insurancePolicyNumber;
    }

    public void setInsurancePolicyNumber(StringFilter insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

    public StringFilter getInsuranceCoverageType() {
        return insuranceCoverageType;
    }

    public Optional<StringFilter> optionalInsuranceCoverageType() {
        return Optional.ofNullable(insuranceCoverageType);
    }

    public StringFilter insuranceCoverageType() {
        if (insuranceCoverageType == null) {
            setInsuranceCoverageType(new StringFilter());
        }
        return insuranceCoverageType;
    }

    public void setInsuranceCoverageType(StringFilter insuranceCoverageType) {
        this.insuranceCoverageType = insuranceCoverageType;
    }

    public LocalDateFilter getInsuranceValidFrom() {
        return insuranceValidFrom;
    }

    public Optional<LocalDateFilter> optionalInsuranceValidFrom() {
        return Optional.ofNullable(insuranceValidFrom);
    }

    public LocalDateFilter insuranceValidFrom() {
        if (insuranceValidFrom == null) {
            setInsuranceValidFrom(new LocalDateFilter());
        }
        return insuranceValidFrom;
    }

    public void setInsuranceValidFrom(LocalDateFilter insuranceValidFrom) {
        this.insuranceValidFrom = insuranceValidFrom;
    }

    public LocalDateFilter getInsuranceValidTo() {
        return insuranceValidTo;
    }

    public Optional<LocalDateFilter> optionalInsuranceValidTo() {
        return Optional.ofNullable(insuranceValidTo);
    }

    public LocalDateFilter insuranceValidTo() {
        if (insuranceValidTo == null) {
            setInsuranceValidTo(new LocalDateFilter());
        }
        return insuranceValidTo;
    }

    public void setInsuranceValidTo(LocalDateFilter insuranceValidTo) {
        this.insuranceValidTo = insuranceValidTo;
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

    public StringFilter getUserEmail() {
        return userEmail;
    }

    public Optional<StringFilter> optionalUserEmail() {
        return Optional.ofNullable(userEmail);
    }

    public StringFilter userEmail() {
        if (userEmail == null) {
            setUserEmail(new StringFilter());
        }
        return userEmail;
    }

    public void setUserEmail(StringFilter userEmail) {
        this.userEmail = userEmail;
    }

    public StringFilter getUserLogin() {
        return userLogin;
    }

    public Optional<StringFilter> optionalUserLogin() {
        return Optional.ofNullable(userLogin);
    }

    public StringFilter userLogin() {
        if (userLogin == null) {
            setUserLogin(new StringFilter());
        }
        return userLogin;
    }

    public void setUserLogin(StringFilter userLogin) {
        this.userLogin = userLogin;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public StringFilter getFullTextSearch() {
        return fullTextSearch;
    }

    public Optional<StringFilter> optionalFullTextSearch() {
        return Optional.ofNullable(fullTextSearch);
    }

    public StringFilter fullTextSearch() {
        if (fullTextSearch == null) {
            setFullTextSearch(new StringFilter());
        }
        return fullTextSearch;
    }

    public void setFullTextSearch(StringFilter fullTextSearch) {
        this.fullTextSearch = fullTextSearch;
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
        final PatientCriteria that = (PatientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(bloodType, that.bloodType) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phone1, that.phone1) &&
            Objects.equals(phone2, that.phone2) &&
            Objects.equals(nif, that.nif) &&
            Objects.equals(ninu, that.ninu) &&
            Objects.equals(medicalRecordNumber, that.medicalRecordNumber) &&
            Objects.equals(heightCm, that.heightCm) &&
            Objects.equals(weightKg, that.weightKg) &&
            Objects.equals(passportNumber, that.passportNumber) &&
            Objects.equals(contactPersonName, that.contactPersonName) &&
            Objects.equals(contactPersonPhone, that.contactPersonPhone) &&
            Objects.equals(antecedents, that.antecedents) &&
            Objects.equals(allergies, that.allergies) &&
            Objects.equals(clinicalNotes, that.clinicalNotes) &&
            Objects.equals(smokingStatus, that.smokingStatus) &&
            Objects.equals(gdprConsentDate, that.gdprConsentDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(deceasedDate, that.deceasedDate) &&
            Objects.equals(insuranceCompanyName, that.insuranceCompanyName) &&
            Objects.equals(patientInsuranceId, that.patientInsuranceId) &&
            Objects.equals(insurancePolicyNumber, that.insurancePolicyNumber) &&
            Objects.equals(insuranceCoverageType, that.insuranceCoverageType) &&
            Objects.equals(insuranceValidFrom, that.insuranceValidFrom) &&
            Objects.equals(insuranceValidTo, that.insuranceValidTo) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userEmail, that.userEmail) &&
            Objects.equals(userLogin, that.userLogin) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(fullTextSearch, that.fullTextSearch) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uid,
            firstName,
            lastName,
            birthDate,
            gender,
            bloodType,
            address,
            phone1,
            phone2,
            nif,
            ninu,
            medicalRecordNumber,
            heightCm,
            weightKg,
            passportNumber,
            contactPersonName,
            contactPersonPhone,
            antecedents,
            allergies,
            clinicalNotes,
            smokingStatus,
            gdprConsentDate,
            status,
            deceasedDate,
            insuranceCompanyName,
            patientInsuranceId,
            insurancePolicyNumber,
            insuranceCoverageType,
            insuranceValidFrom,
            insuranceValidTo,
            userId,
            userEmail,
            userLogin,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            fullTextSearch,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "PatientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUid().map(f -> "uid=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalBloodType().map(f -> "bloodType=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalPhone1().map(f -> "phone1=" + f + ", ").orElse("") +
            optionalPhone2().map(f -> "phone2=" + f + ", ").orElse("") +
            optionalNif().map(f -> "nif=" + f + ", ").orElse("") +
            optionalNinu().map(f -> "ninu=" + f + ", ").orElse("") +
            optionalMedicalRecordNumber().map(f -> "medicalRecordNumber=" + f + ", ").orElse("") +
            optionalHeightCm().map(f -> "heightCm=" + f + ", ").orElse("") +
            optionalWeightKg().map(f -> "weightKg=" + f + ", ").orElse("") +
            optionalPassportNumber().map(f -> "passportNumber=" + f + ", ").orElse("") +
            optionalContactPersonName().map(f -> "contactPersonName=" + f + ", ").orElse("") +
            optionalContactPersonPhone().map(f -> "contactPersonPhone=" + f + ", ").orElse("") +
            optionalAntecedents().map(f -> "antecedents=" + f + ", ").orElse("") +
            optionalAllergies().map(f -> "allergies=" + f + ", ").orElse("") +
            optionalClinicalNotes().map(f -> "clinicalNotes=" + f + ", ").orElse("") +
            optionalSmokingStatus().map(f -> "smokingStatus=" + f + ", ").orElse("") +
            optionalGdprConsentDate().map(f -> "gdprConsentDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDeceasedDate().map(f -> "deceasedDate=" + f + ", ").orElse("") +
            optionalInsuranceCompanyName().map(f -> "insuranceCompanyName=" + f + ", ").orElse("") +
            optionalPatientInsuranceId().map(f -> "patientInsuranceId=" + f + ", ").orElse("") +
            optionalInsurancePolicyNumber().map(f -> "insurancePolicyNumber=" + f + ", ").orElse("") +
            optionalInsuranceCoverageType().map(f -> "insuranceCoverageType=" + f + ", ").orElse("") +
            optionalInsuranceValidFrom().map(f -> "insuranceValidFrom=" + f + ", ").orElse("") +
            optionalInsuranceValidTo().map(f -> "insuranceValidTo=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalUserEmail().map(f -> "userEmail=" + f + ", ").orElse("") +
            optionalUserLogin().map(f -> "userLogin=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalFullTextSearch().map(f -> "fullTextSearch=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}"
        );
    }
}
