package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.MedicalDocument;
import com.ciatch.gdp.repository.MedicalDocumentRepository;
import com.ciatch.gdp.service.dto.MedicalDocumentDTO;
import com.ciatch.gdp.service.mapper.MedicalDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ciatch.gdp.domain.MedicalDocument}.
 */
@Service
@Transactional
public class MedicalDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalDocumentService.class);

    private final MedicalDocumentRepository medicalDocumentRepository;

    private final MedicalDocumentMapper medicalDocumentMapper;

    public MedicalDocumentService(MedicalDocumentRepository medicalDocumentRepository, MedicalDocumentMapper medicalDocumentMapper) {
        this.medicalDocumentRepository = medicalDocumentRepository;
        this.medicalDocumentMapper = medicalDocumentMapper;
    }

    /**
     * Save a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalDocumentDTO save(MedicalDocumentDTO medicalDocumentDTO) {
        LOG.debug("Request to save MedicalDocument : {}", medicalDocumentDTO);
        MedicalDocument medicalDocument = medicalDocumentMapper.toEntity(medicalDocumentDTO);
        medicalDocument = medicalDocumentRepository.save(medicalDocument);
        return medicalDocumentMapper.toDto(medicalDocument);
    }

    /**
     * Update a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalDocumentDTO update(MedicalDocumentDTO medicalDocumentDTO) {
        LOG.debug("Request to update MedicalDocument : {}", medicalDocumentDTO);
        MedicalDocument medicalDocument = medicalDocumentMapper.toEntity(medicalDocumentDTO);
        medicalDocument = medicalDocumentRepository.save(medicalDocument);
        return medicalDocumentMapper.toDto(medicalDocument);
    }

    /**
     * Partially update a medicalDocument.
     *
     * @param medicalDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicalDocumentDTO> partialUpdate(MedicalDocumentDTO medicalDocumentDTO) {
        LOG.debug("Request to partially update MedicalDocument : {}", medicalDocumentDTO);

        return medicalDocumentRepository
            .findById(medicalDocumentDTO.getId())
            .map(existingMedicalDocument -> {
                medicalDocumentMapper.partialUpdate(existingMedicalDocument, medicalDocumentDTO);

                return existingMedicalDocument;
            })
            .map(medicalDocumentRepository::save)
            .map(medicalDocumentMapper::toDto);
    }

    /**
     * Get all the medicalDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalDocumentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MedicalDocuments");
        return medicalDocumentRepository.findAll(pageable).map(medicalDocumentMapper::toDto);
    }

    /**
     * Get all the medicalDocuments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicalDocumentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicalDocumentRepository.findAllWithEagerRelationships(pageable).map(medicalDocumentMapper::toDto);
    }

    /**
     * Get one medicalDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicalDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get MedicalDocument : {}", id);
        return medicalDocumentRepository.findOneWithEagerRelationships(id).map(medicalDocumentMapper::toDto);
    }

    /**
     * Delete the medicalDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MedicalDocument : {}", id);
        medicalDocumentRepository.deleteById(id);
    }
}
