package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.domain.DoctorProfile_;
import com.ciatch.gdp.domain.User_;
import com.ciatch.gdp.domain.enumeration.DoctorStatus;
import com.ciatch.gdp.repository.DoctorProfileRepository;
import com.ciatch.gdp.service.criteria.DoctorProfileCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing queries for public Doctor Profile access.
 * Returns only ACTIVE doctors and uses entity results directly.
 */
@Service
@Transactional(readOnly = true)
public class DoctorPublicService extends QueryService<DoctorProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorPublicService.class);

    private final DoctorProfileRepository doctorProfileRepository;

    public DoctorPublicService(DoctorProfileRepository doctorProfileRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
    }

    /**
     * Find all ACTIVE doctors matching the criteria.
     *
     * @param criteria The filter criteria
     * @param pageable The pagination information
     * @return Page of active DoctorProfile entities
     */
    @Transactional(readOnly = true)
    public Page<DoctorProfile> findActiveDoctors(DoctorProfileCriteria criteria, Pageable pageable) {
        LOG.debug("Find active doctors by criteria: {}, page: {}", criteria, pageable);

        // Force status to ACTIVE
        DoctorProfileCriteria.DoctorStatusFilter statusFilter = new DoctorProfileCriteria.DoctorStatusFilter();
        statusFilter.setEquals(DoctorStatus.ACTIVE);
        criteria.setStatus(statusFilter);

        final Specification<DoctorProfile> specification = createSpecification(criteria);
        return doctorProfileRepository.findAll(specification, pageable);
    }

    /**
     * Find a single ACTIVE doctor by UID.
     *
     * @param uid The doctor's UID
     * @return Optional containing the doctor if found and active
     */
    @Transactional(readOnly = true)
    public Optional<DoctorProfile> findActiveDoctorByUid(UUID uid) {
        LOG.debug("Find active doctor by UID: {}", uid);
        return doctorProfileRepository.findByUid(uid).filter(doctor -> DoctorStatus.ACTIVE.equals(doctor.getStatus()));
    }

    /**
     * Count all ACTIVE doctors matching the criteria.
     *
     * @param criteria The filter criteria
     * @return Count of matching active doctors
     */
    @Transactional(readOnly = true)
    public long countActiveDoctors(DoctorProfileCriteria criteria) {
        LOG.debug("Count active doctors by criteria: {}", criteria);

        // Force status to ACTIVE
        DoctorProfileCriteria.DoctorStatusFilter statusFilter = new DoctorProfileCriteria.DoctorStatusFilter();
        statusFilter.setEquals(DoctorStatus.ACTIVE);
        criteria.setStatus(statusFilter);

        final Specification<DoctorProfile> specification = createSpecification(criteria);
        return doctorProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link DoctorProfileCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters
     * @return the matching {@link Specification} of the entity
     */
    protected Specification<DoctorProfile> createSpecification(DoctorProfileCriteria criteria) {
        Specification<DoctorProfile> specification = Specification.where(null);
        if (criteria != null) {
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
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), DoctorProfile_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), DoctorProfile_.lastName));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), DoctorProfile_.gender));
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
            if (criteria.getConsultationFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConsultationFee(), DoctorProfile_.consultationFee));
            }
            if (criteria.getTeleconsultationFee() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getTeleconsultationFee(), DoctorProfile_.teleconsultationFee)
                );
            }
            if (criteria.getSpokenLanguages() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpokenLanguages(), DoctorProfile_.spokenLanguages));
            }
            if (criteria.getAverageRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAverageRating(), DoctorProfile_.averageRating));
            }
            if (criteria.getReviewCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewCount(), DoctorProfile_.reviewCount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), DoctorProfile_.status));
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
