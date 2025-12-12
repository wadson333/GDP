package com.ciatch.gdp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DoctorProfileCriteriaTest {

    @Test
    void newDoctorProfileCriteriaHasAllFiltersNullTest() {
        var doctorProfileCriteria = new DoctorProfileCriteria();
        assertThat(doctorProfileCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void doctorProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var doctorProfileCriteria = new DoctorProfileCriteria();

        setAllFilters(doctorProfileCriteria);

        assertThat(doctorProfileCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void doctorProfileCriteriaCopyCreatesNullFilterTest() {
        var doctorProfileCriteria = new DoctorProfileCriteria();
        var copy = doctorProfileCriteria.copy();

        assertThat(doctorProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(doctorProfileCriteria)
        );
    }

    @Test
    void doctorProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var doctorProfileCriteria = new DoctorProfileCriteria();
        setAllFilters(doctorProfileCriteria);

        var copy = doctorProfileCriteria.copy();

        assertThat(doctorProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(doctorProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var doctorProfileCriteria = new DoctorProfileCriteria();

        assertThat(doctorProfileCriteria).hasToString("DoctorProfileCriteria{}");
    }

    private static void setAllFilters(DoctorProfileCriteria doctorProfileCriteria) {
        doctorProfileCriteria.id();
        doctorProfileCriteria.codeClinic();
        doctorProfileCriteria.uid();
        doctorProfileCriteria.medicalLicenseNumber();
        doctorProfileCriteria.firstName();
        doctorProfileCriteria.lastName();
        doctorProfileCriteria.birthDate();
        doctorProfileCriteria.gender();
        doctorProfileCriteria.bloodType();
        doctorProfileCriteria.primarySpecialty();
        doctorProfileCriteria.university();
        doctorProfileCriteria.graduationYear();
        doctorProfileCriteria.startDateOfPractice();
        doctorProfileCriteria.consultationDurationMinutes();
        doctorProfileCriteria.acceptingNewPatients();
        doctorProfileCriteria.allowsTeleconsultation();
        doctorProfileCriteria.consultationFee();
        doctorProfileCriteria.teleconsultationFee();
        doctorProfileCriteria.spokenLanguages();
        doctorProfileCriteria.websiteUrl();
        doctorProfileCriteria.officePhone();
        doctorProfileCriteria.status();
        doctorProfileCriteria.isVerified();
        doctorProfileCriteria.verifiedAt();
        doctorProfileCriteria.nif();
        doctorProfileCriteria.ninu();
        doctorProfileCriteria.averageRating();
        doctorProfileCriteria.reviewCount();
        doctorProfileCriteria.version();
        doctorProfileCriteria.userId();
        doctorProfileCriteria.distinct();
    }

    private static Condition<DoctorProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodeClinic()) &&
                condition.apply(criteria.getUid()) &&
                condition.apply(criteria.getMedicalLicenseNumber()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getBloodType()) &&
                condition.apply(criteria.getPrimarySpecialty()) &&
                condition.apply(criteria.getUniversity()) &&
                condition.apply(criteria.getGraduationYear()) &&
                condition.apply(criteria.getStartDateOfPractice()) &&
                condition.apply(criteria.getConsultationDurationMinutes()) &&
                condition.apply(criteria.getAcceptingNewPatients()) &&
                condition.apply(criteria.getAllowsTeleconsultation()) &&
                condition.apply(criteria.getConsultationFee()) &&
                condition.apply(criteria.getTeleconsultationFee()) &&
                condition.apply(criteria.getSpokenLanguages()) &&
                condition.apply(criteria.getWebsiteUrl()) &&
                condition.apply(criteria.getOfficePhone()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getIsVerified()) &&
                condition.apply(criteria.getVerifiedAt()) &&
                condition.apply(criteria.getNif()) &&
                condition.apply(criteria.getNinu()) &&
                condition.apply(criteria.getAverageRating()) &&
                condition.apply(criteria.getReviewCount()) &&
                condition.apply(criteria.getVersion()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DoctorProfileCriteria> copyFiltersAre(
        DoctorProfileCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodeClinic(), copy.getCodeClinic()) &&
                condition.apply(criteria.getUid(), copy.getUid()) &&
                condition.apply(criteria.getMedicalLicenseNumber(), copy.getMedicalLicenseNumber()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getBloodType(), copy.getBloodType()) &&
                condition.apply(criteria.getPrimarySpecialty(), copy.getPrimarySpecialty()) &&
                condition.apply(criteria.getUniversity(), copy.getUniversity()) &&
                condition.apply(criteria.getGraduationYear(), copy.getGraduationYear()) &&
                condition.apply(criteria.getStartDateOfPractice(), copy.getStartDateOfPractice()) &&
                condition.apply(criteria.getConsultationDurationMinutes(), copy.getConsultationDurationMinutes()) &&
                condition.apply(criteria.getAcceptingNewPatients(), copy.getAcceptingNewPatients()) &&
                condition.apply(criteria.getAllowsTeleconsultation(), copy.getAllowsTeleconsultation()) &&
                condition.apply(criteria.getConsultationFee(), copy.getConsultationFee()) &&
                condition.apply(criteria.getTeleconsultationFee(), copy.getTeleconsultationFee()) &&
                condition.apply(criteria.getSpokenLanguages(), copy.getSpokenLanguages()) &&
                condition.apply(criteria.getWebsiteUrl(), copy.getWebsiteUrl()) &&
                condition.apply(criteria.getOfficePhone(), copy.getOfficePhone()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getIsVerified(), copy.getIsVerified()) &&
                condition.apply(criteria.getVerifiedAt(), copy.getVerifiedAt()) &&
                condition.apply(criteria.getNif(), copy.getNif()) &&
                condition.apply(criteria.getNinu(), copy.getNinu()) &&
                condition.apply(criteria.getAverageRating(), copy.getAverageRating()) &&
                condition.apply(criteria.getReviewCount(), copy.getReviewCount()) &&
                condition.apply(criteria.getVersion(), copy.getVersion()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
