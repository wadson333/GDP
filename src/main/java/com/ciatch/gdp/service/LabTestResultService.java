package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.LabTestResult;
import com.ciatch.gdp.repository.LabTestResultRepository;
import com.ciatch.gdp.service.dto.LabTestResultDTO;
import com.ciatch.gdp.service.mapper.LabTestResultMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ciatch.gdp.domain.LabTestResult}.
 */
@Service
@Transactional
public class LabTestResultService {

    private static final Logger LOG = LoggerFactory.getLogger(LabTestResultService.class);

    private final LabTestResultRepository labTestResultRepository;

    private final LabTestResultMapper labTestResultMapper;

    public LabTestResultService(LabTestResultRepository labTestResultRepository, LabTestResultMapper labTestResultMapper) {
        this.labTestResultRepository = labTestResultRepository;
        this.labTestResultMapper = labTestResultMapper;
    }

    /**
     * Save a labTestResult.
     *
     * @param labTestResultDTO the entity to save.
     * @return the persisted entity.
     */
    public LabTestResultDTO save(LabTestResultDTO labTestResultDTO) {
        LOG.debug("Request to save LabTestResult : {}", labTestResultDTO);
        LabTestResult labTestResult = labTestResultMapper.toEntity(labTestResultDTO);
        labTestResult = labTestResultRepository.save(labTestResult);
        return labTestResultMapper.toDto(labTestResult);
    }

    /**
     * Update a labTestResult.
     *
     * @param labTestResultDTO the entity to save.
     * @return the persisted entity.
     */
    public LabTestResultDTO update(LabTestResultDTO labTestResultDTO) {
        LOG.debug("Request to update LabTestResult : {}", labTestResultDTO);
        LabTestResult labTestResult = labTestResultMapper.toEntity(labTestResultDTO);
        labTestResult = labTestResultRepository.save(labTestResult);
        return labTestResultMapper.toDto(labTestResult);
    }

    /**
     * Partially update a labTestResult.
     *
     * @param labTestResultDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LabTestResultDTO> partialUpdate(LabTestResultDTO labTestResultDTO) {
        LOG.debug("Request to partially update LabTestResult : {}", labTestResultDTO);

        return labTestResultRepository
            .findById(labTestResultDTO.getId())
            .map(existingLabTestResult -> {
                labTestResultMapper.partialUpdate(existingLabTestResult, labTestResultDTO);

                return existingLabTestResult;
            })
            .map(labTestResultRepository::save)
            .map(labTestResultMapper::toDto);
    }

    /**
     * Get all the labTestResults.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LabTestResultDTO> findAll() {
        LOG.debug("Request to get all LabTestResults");
        return labTestResultRepository.findAll().stream().map(labTestResultMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the labTestResults with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LabTestResultDTO> findAllWithEagerRelationships(Pageable pageable) {
        return labTestResultRepository.findAllWithEagerRelationships(pageable).map(labTestResultMapper::toDto);
    }

    /**
     * Get one labTestResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LabTestResultDTO> findOne(Long id) {
        LOG.debug("Request to get LabTestResult : {}", id);
        return labTestResultRepository.findOneWithEagerRelationships(id).map(labTestResultMapper::toDto);
    }

    /**
     * Delete the labTestResult by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LabTestResult : {}", id);
        labTestResultRepository.deleteById(id);
    }
}
