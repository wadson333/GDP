package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.Medication;
import com.ciatch.gdp.repository.MedicationRepository;
import com.ciatch.gdp.service.dto.MedicationDTO;
import com.ciatch.gdp.service.mapper.MedicationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ciatch.gdp.domain.Medication}.
 */
@Service
@Transactional
public class MedicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationService.class);

    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    public MedicationService(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    /**
     * Save a medication.
     *
     * @param medicationDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationDTO save(MedicationDTO medicationDTO) {
        LOG.debug("Request to save Medication : {}", medicationDTO);
        Medication medication = medicationMapper.toEntity(medicationDTO);
        medication = medicationRepository.save(medication);
        return medicationMapper.toDto(medication);
    }

    /**
     * Update a medication.
     *
     * @param medicationDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationDTO update(MedicationDTO medicationDTO) {
        LOG.debug("Request to update Medication : {}", medicationDTO);
        Medication medication = medicationMapper.toEntity(medicationDTO);
        medication = medicationRepository.save(medication);
        return medicationMapper.toDto(medication);
    }

    /**
     * Partially update a medication.
     *
     * @param medicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicationDTO> partialUpdate(MedicationDTO medicationDTO) {
        LOG.debug("Request to partially update Medication : {}", medicationDTO);

        return medicationRepository
            .findById(medicationDTO.getId())
            .map(existingMedication -> {
                medicationMapper.partialUpdate(existingMedication, medicationDTO);

                return existingMedication;
            })
            .map(medicationRepository::save)
            .map(medicationMapper::toDto);
    }

    /**
     * Get all the medications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Medications");
        return medicationRepository.findAll(pageable).map(medicationMapper::toDto);
    }

    /**
     * Get one medication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicationDTO> findOne(Long id) {
        LOG.debug("Request to get Medication : {}", id);
        return medicationRepository.findById(id).map(medicationMapper::toDto);
    }

    /**
     * Delete the medication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Medication : {}", id);
        medicationRepository.deleteById(id);
    }
}
