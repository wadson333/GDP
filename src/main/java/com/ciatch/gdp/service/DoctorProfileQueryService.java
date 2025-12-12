package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.*; // for static metamodels
import com.ciatch.gdp.repository.DoctorProfileRepository;
import com.ciatch.gdp.service.criteria.DoctorProfileCriteria;
import com.ciatch.gdp.service.dto.DoctorProfileDTO;
import com.ciatch.gdp.service.mapper.DoctorProfileMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DoctorProfile} entities in the database.
 * The main input is a {@link DoctorProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DoctorProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DoctorProfileQueryService extends QueryService<DoctorProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorProfileQueryService.class);

    private final DoctorProfileRepository doctorProfileRepository;

    private final DoctorProfileMapper doctorProfileMapper;

    public DoctorProfileQueryService(DoctorProfileRepository doctorProfileRepository, DoctorProfileMapper doctorProfileMapper) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.doctorProfileMapper = doctorProfileMapper;
    }

    /**
     * Return a {@link Page} of {@link DoctorProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorProfileDTO> findByCriteria(DoctorProfileCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DoctorProfile> specification = createSpecification(criteria);
        return doctorProfileRepository.findAll(specification, page).map(doctorProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DoctorProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DoctorProfile> specification = createSpecification(criteria);
        return doctorProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link DoctorProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DoctorProfile> createSpecification(DoctorProfileCriteria criteria) {
        Specification<DoctorProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DoctorProfile_.id));
            }
            if (criteria.getCodeClinic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeClinic(), DoctorProfile_.codeClinic));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildSpecification(criteria.getUid(), DoctorProfile_.uid));
            }
            if (criteria.getMedicalLicenseNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getMedicalLicenseNumber(), DoctorProfile_.medicalLicenseNumber)
                );
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), DoctorProfile_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), DoctorProfile_.lastName));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), DoctorProfile_.birthDate));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), DoctorProfile_.gender));
            }
            if (criteria.getBloodType() != null) {
                specification = specification.and(buildSpecification(criteria.getBloodType(), DoctorProfile_.bloodType));
            }
            if (criteria.getPrimarySpecialty() != null) {
                specification = specification.and(buildSpecification(criteria.getPrimarySpecialty(), DoctorProfile_.primarySpecialty));
            }
            if (criteria.getUniversity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUniversity(), DoctorProfile_.university));
            }
            if (criteria.getGraduationYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGraduationYear(), DoctorProfile_.graduationYear));
            }
            if (criteria.getStartDateOfPractice() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getStartDateOfPractice(), DoctorProfile_.startDateOfPractice)
                );
            }
            if (criteria.getConsultationDurationMinutes() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getConsultationDurationMinutes(), DoctorProfile_.consultationDurationMinutes)
                );
            }
            if (criteria.getAcceptingNewPatients() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAcceptingNewPatients(), DoctorProfile_.acceptingNewPatients)
                );
            }
            if (criteria.getAllowsTeleconsultation() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAllowsTeleconsultation(), DoctorProfile_.allowsTeleconsultation)
                );
            }
            if (criteria.getConsultationFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConsultationFee(), DoctorProfile_.consultationFee));
            }
            if (criteria.getGlobalFilter() != null) {
                Specification<DoctorProfile> globalSpec = Specification.where(null);

                // On construit une condition OR pour chaque champ texte pertinent
                globalSpec = globalSpec.or(buildStringSpecification(criteria.getGlobalFilter(), DoctorProfile_.firstName));
                globalSpec = globalSpec.or(buildStringSpecification(criteria.getGlobalFilter(), DoctorProfile_.lastName));
                globalSpec = globalSpec.or(buildStringSpecification(criteria.getGlobalFilter(), DoctorProfile_.medicalLicenseNumber));
                globalSpec = globalSpec.or(buildStringSpecification(criteria.getGlobalFilter(), DoctorProfile_.codeClinic)); // Si accessible via metamodel
                globalSpec = globalSpec.or(buildStringSpecification(criteria.getGlobalFilter(), DoctorProfile_.otherSpecialties)); // Si accessible via metamodel

                // On ajoute ce gros bloc OR à la spécification principale avec un AND
                // Résultat : (Filtres spécifiques) AND (Nom OU Prénom OU Licence)
                specification = specification.and(globalSpec);
            }
            if (criteria.getTeleconsultationFee() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getTeleconsultationFee(), DoctorProfile_.teleconsultationFee)
                );
            }
            if (criteria.getSpokenLanguages() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpokenLanguages(), DoctorProfile_.spokenLanguages));
            }
            if (criteria.getWebsiteUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWebsiteUrl(), DoctorProfile_.websiteUrl));
            }
            if (criteria.getOfficePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfficePhone(), DoctorProfile_.officePhone));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), DoctorProfile_.status));
            }
            if (criteria.getIsVerified() != null) {
                specification = specification.and(buildSpecification(criteria.getIsVerified(), DoctorProfile_.isVerified));
            }
            if (criteria.getVerifiedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVerifiedAt(), DoctorProfile_.verifiedAt));
            }
            if (criteria.getNif() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNif(), DoctorProfile_.nif));
            }
            if (criteria.getNinu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNinu(), DoctorProfile_.ninu));
            }
            if (criteria.getAverageRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAverageRating(), DoctorProfile_.averageRating));
            }
            if (criteria.getReviewCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewCount(), DoctorProfile_.reviewCount));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVersion(), DoctorProfile_.version));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(DoctorProfile_.user, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
