package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.Medication;
import com.ciatch.gdp.domain.Medication_;
import com.ciatch.gdp.repository.MedicationRepository;
import com.ciatch.gdp.service.criteria.MedicationCriteria;
import com.ciatch.gdp.service.dto.MedicationDTO;
import com.ciatch.gdp.service.mapper.MedicationMapper;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class MedicationQueryService extends QueryService<Medication> {

    private final Logger log = LoggerFactory.getLogger(MedicationQueryService.class);

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    public MedicationQueryService(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    /**
     * Finds a page of MedicationDTOs matching the given criteria.
     *
     * @param criteria the filtering criteria
     * @param page the pagination information
     * @return a page of matching MedicationDTOs
     */
    @Transactional(readOnly = true)
    public Page<MedicationDTO> findByCriteria(MedicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationRepository.findAll(specification, page).map(medicationMapper::toDto);
    }

    /**
     * Creates a JPA Specification based on the provided MedicationCriteria.
     * This specification is used to filter Medication entities according to the criteria fields.
     *
     * @param criteria the filtering criteria
     * @return the constructed Specification for Medication
     */
    protected Specification<Medication> createSpecification(MedicationCriteria criteria) {
        Specification<Medication> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Medication_.name));
            }
            if (criteria.getCodeAtc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeAtc(), Medication_.codeAtc));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Medication_.active));
            }
            if (criteria.getManufacturer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getManufacturer(), Medication_.manufacturer));
            }
            if (criteria.getRouteOfAdministration() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRouteOfAdministration(), Medication_.routeOfAdministration)
                );
            }
            if (criteria.getPrescriptionStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPrescriptionStatus(), Medication_.prescriptionStatus));
            }
            if (criteria.getRiskLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getRiskLevel(), Medication_.riskLevel));
            }
            // Add price range conditions
            if (criteria.getUnitPriceMin() != null) {
                specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get(Medication_.unitPrice), criteria.getUnitPriceMin().getEquals())
                );
            }
            if (criteria.getUnitPriceMax() != null) {
                specification = specification.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get(Medication_.unitPrice), criteria.getUnitPriceMax().getEquals())
                );
            }
        }
        return specification;
    }
}
