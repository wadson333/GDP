package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.repository.LabTestCatalogRepository;
import com.ciatch.gdp.service.dto.LabTestCatalogDTO;
import com.ciatch.gdp.service.mapper.LabTestCatalogMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ciatch.gdp.domain.LabTestCatalog}.
 */
@Service
@Transactional
public class LabTestCatalogService {

    private static final Logger LOG = LoggerFactory.getLogger(LabTestCatalogService.class);

    private final LabTestCatalogRepository labTestCatalogRepository;

    private final LabTestCatalogMapper labTestCatalogMapper;

    public LabTestCatalogService(LabTestCatalogRepository labTestCatalogRepository, LabTestCatalogMapper labTestCatalogMapper) {
        this.labTestCatalogRepository = labTestCatalogRepository;
        this.labTestCatalogMapper = labTestCatalogMapper;
    }

    /**
     * Save a labTestCatalog.
     *
     * @param labTestCatalogDTO the entity to save.
     * @return the persisted entity.
     */
    public LabTestCatalogDTO save(LabTestCatalogDTO labTestCatalogDTO) {
        LOG.debug("Request to save LabTestCatalog : {}", labTestCatalogDTO);
        LabTestCatalog labTestCatalog = labTestCatalogMapper.toEntity(labTestCatalogDTO);
        labTestCatalog = labTestCatalogRepository.save(labTestCatalog);
        return labTestCatalogMapper.toDto(labTestCatalog);
    }

    /**
     * Update a labTestCatalog.
     *
     * @param labTestCatalogDTO the entity to save.
     * @return the persisted entity.
     */
    public LabTestCatalogDTO update(LabTestCatalogDTO labTestCatalogDTO) {
        LOG.debug("Request to update LabTestCatalog : {}", labTestCatalogDTO);
        LabTestCatalog labTestCatalog = labTestCatalogMapper.toEntity(labTestCatalogDTO);
        labTestCatalog = labTestCatalogRepository.save(labTestCatalog);
        return labTestCatalogMapper.toDto(labTestCatalog);
    }

    /**
     * Partially update a labTestCatalog.
     *
     * @param labTestCatalogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LabTestCatalogDTO> partialUpdate(LabTestCatalogDTO labTestCatalogDTO) {
        LOG.debug("Request to partially update LabTestCatalog : {}", labTestCatalogDTO);

        return labTestCatalogRepository
            .findById(labTestCatalogDTO.getId())
            .map(existingLabTestCatalog -> {
                labTestCatalogMapper.partialUpdate(existingLabTestCatalog, labTestCatalogDTO);

                return existingLabTestCatalog;
            })
            .map(labTestCatalogRepository::save)
            .map(labTestCatalogMapper::toDto);
    }

    /**
     * Get all the labTestCatalogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LabTestCatalogDTO> findAll() {
        LOG.debug("Request to get all LabTestCatalogs");
        return labTestCatalogRepository
            .findAll()
            .stream()
            .map(labTestCatalogMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one labTestCatalog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LabTestCatalogDTO> findOne(Long id) {
        LOG.debug("Request to get LabTestCatalog : {}", id);
        return labTestCatalogRepository.findById(id).map(labTestCatalogMapper::toDto);
    }

    /**
     * Delete the labTestCatalog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LabTestCatalog : {}", id);
        labTestCatalogRepository.deleteById(id);
    }
}
