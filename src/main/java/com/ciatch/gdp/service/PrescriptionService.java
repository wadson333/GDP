package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.Prescription;
import com.ciatch.gdp.repository.PrescriptionRepository;
import com.ciatch.gdp.service.dto.PrescriptionDTO;
import com.ciatch.gdp.service.mapper.PrescriptionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ciatch.gdp.domain.Prescription}.
 */
@Service
@Transactional
public class PrescriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(PrescriptionService.class);

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    /**
     * Save a prescription.
     *
     * @param prescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionDTO save(PrescriptionDTO prescriptionDTO) {
        LOG.debug("Request to save Prescription : {}", prescriptionDTO);
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    /**
     * Update a prescription.
     *
     * @param prescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionDTO update(PrescriptionDTO prescriptionDTO) {
        LOG.debug("Request to update Prescription : {}", prescriptionDTO);
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    /**
     * Partially update a prescription.
     *
     * @param prescriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrescriptionDTO> partialUpdate(PrescriptionDTO prescriptionDTO) {
        LOG.debug("Request to partially update Prescription : {}", prescriptionDTO);

        return prescriptionRepository
            .findById(prescriptionDTO.getId())
            .map(existingPrescription -> {
                prescriptionMapper.partialUpdate(existingPrescription, prescriptionDTO);

                return existingPrescription;
            })
            .map(prescriptionRepository::save)
            .map(prescriptionMapper::toDto);
    }

    /**
     * Get all the prescriptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> findAll() {
        LOG.debug("Request to get all Prescriptions");
        return prescriptionRepository.findAll().stream().map(prescriptionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the prescriptions where Consultation is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> findAllWhereConsultationIsNull() {
        LOG.debug("Request to get all prescriptions where Consultation is null");
        return StreamSupport.stream(prescriptionRepository.findAll().spliterator(), false)
            .filter(prescription -> prescription.getConsultation() == null)
            .map(prescriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one prescription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrescriptionDTO> findOne(Long id) {
        LOG.debug("Request to get Prescription : {}", id);
        return prescriptionRepository.findById(id).map(prescriptionMapper::toDto);
    }

    /**
     * Delete the prescription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Prescription : {}", id);
        prescriptionRepository.deleteById(id);
    }
}
