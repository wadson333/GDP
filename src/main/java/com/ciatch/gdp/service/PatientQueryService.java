package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.*;
import com.ciatch.gdp.repository.PatientRepository;
import com.ciatch.gdp.service.criteria.PatientCriteria;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.mapper.PatientMapper;
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
 * Service for executing complex queries for {@link Patient} entities in the database.
 * The main input is a {@link PatientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PatientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQueryService extends QueryService<Patient> {

    private static final Logger LOG = LoggerFactory.getLogger(PatientQueryService.class);

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientQueryService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    /**
     * Return a {@link Page} of {@link PatientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientDTO> findByCriteria(PatientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification, page).map(patientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Patient> createSpecification(PatientCriteria criteria) {
        Specification<Patient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Patient_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildSpecification(criteria.getUid(), Patient_.uid));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Patient_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Patient_.lastName));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Patient_.birthDate));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Patient_.gender));
            }
            if (criteria.getBloodType() != null) {
                specification = specification.and(buildSpecification(criteria.getBloodType(), Patient_.bloodType));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Patient_.address));
            }
            if (criteria.getPhone1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone1(), Patient_.phone1));
            }
            if (criteria.getPhone2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone2(), Patient_.phone2));
            }
            if (criteria.getNif() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNif(), Patient_.nif));
            }
            if (criteria.getNinu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNinu(), Patient_.ninu));
            }
            if (criteria.getMedicalRecordNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getMedicalRecordNumber(), Patient_.medicalRecordNumber)
                );
            }
            if (criteria.getHeightCm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeightCm(), Patient_.heightCm));
            }
            if (criteria.getWeightKg() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeightKg(), Patient_.weightKg));
            }
            if (criteria.getPassportNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassportNumber(), Patient_.passportNumber));
            }
            if (criteria.getContactPersonName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPersonName(), Patient_.contactPersonName));
            }
            if (criteria.getContactPersonPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactPersonPhone(), Patient_.contactPersonPhone));
            }
            if (criteria.getAntecedents() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAntecedents(), Patient_.antecedents));
            }
            if (criteria.getAllergies() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAllergies(), Patient_.allergies));
            }
            if (criteria.getClinicalNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClinicalNotes(), Patient_.clinicalNotes));
            }
            if (criteria.getSmokingStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getSmokingStatus(), Patient_.smokingStatus));
            }
            if (criteria.getGdprConsentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGdprConsentDate(), Patient_.gdprConsentDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Patient_.status));
            }
            if (criteria.getDeceasedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeceasedDate(), Patient_.deceasedDate));
            }
            if (criteria.getInsuranceCompanyName() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getInsuranceCompanyName(), Patient_.insuranceCompanyName)
                );
            }
            if (criteria.getPatientInsuranceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPatientInsuranceId(), Patient_.patientInsuranceId));
            }
            if (criteria.getInsurancePolicyNumber() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getInsurancePolicyNumber(), Patient_.insurancePolicyNumber)
                );
            }
            if (criteria.getInsuranceCoverageType() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getInsuranceCoverageType(), Patient_.insuranceCoverageType)
                );
            }
            if (criteria.getInsuranceValidFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInsuranceValidFrom(), Patient_.insuranceValidFrom));
            }
            if (criteria.getInsuranceValidTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInsuranceValidTo(), Patient_.insuranceValidTo));
            }
            // if (criteria.getCreatedBy() != null) {
            //     specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Patient_.createdBy));
            // }
            // if (criteria.getCreatedDate() != null) {
            //     specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Patient_.createdDate));
            // }
            // if (criteria.getLastModifiedBy() != null) {
            //     specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Patient_.lastModifiedBy));
            // }
            // if (criteria.getLastModifiedDate() != null) {
            //     specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Patient_.lastModifiedDate));
            // }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(Patient_.user, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getUserEmail() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserEmail(), root -> root.join(Patient_.user, JoinType.LEFT).get(User_.email))
                );
            }
            if (criteria.getUserLogin() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserLogin(), root -> root.join(Patient_.user, JoinType.LEFT).get(User_.login))
                );
            }

            // Full Text Search - recherche OR sur plusieurs champs
            if (criteria.getFullTextSearch() != null && criteria.getFullTextSearch().getContains() != null) {
                String searchTerm = criteria.getFullTextSearch().getContains();
                specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(Patient_.firstName)), "%" + searchTerm.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(Patient_.lastName)), "%" + searchTerm.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(Patient_.nif)), "%" + searchTerm.toLowerCase() + "%"),
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(Patient_.medicalRecordNumber)),
                            "%" + searchTerm.toLowerCase() + "%"
                        ),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(Patient_.phone1)), "%" + searchTerm.toLowerCase() + "%")
                    )
                );
            }
        }
        return specification;
    }
}
