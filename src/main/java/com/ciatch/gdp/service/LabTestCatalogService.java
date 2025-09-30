package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.domain.enumeration.LabTestMethod;
import com.ciatch.gdp.domain.enumeration.LabTestType;
import com.ciatch.gdp.repository.LabTestCatalogRepository;
import com.ciatch.gdp.service.dto.LabTestCatalogDTO;
import com.ciatch.gdp.service.mapper.LabTestCatalogMapper;
import com.ciatch.gdp.web.rest.errors.LabTestNameAlreadyUsedException;
import java.util.Comparator;
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
        // Check if name already exists
        if (labTestCatalogDTO.getId() == null && labTestCatalogRepository.findLatestVersionByName(labTestCatalogDTO.getName()) != null) {
            throw new LabTestNameAlreadyUsedException();
        }

        // Set default values for new entries
        if (labTestCatalogDTO.getId() == null) {
            labTestCatalogDTO.setActive(true);
            labTestCatalogDTO.setVersion(1);
        }

        if (labTestCatalogDTO.getValidFrom() == null) {
            labTestCatalogDTO.setValidFrom(java.time.Instant.now());
        }

        if (labTestCatalogDTO.getVersion() == null) {
            labTestCatalogDTO.setVersion(1);
        }

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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LabTestCatalogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LabTestCatalogs");
        return labTestCatalogRepository.findAll(pageable).map(labTestCatalogMapper::toDto);
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

    /**
     * Searches for LabTestCatalog entities based on the provided criteria.
     *
     * @param name the name of the lab test to search for (can be partial or full name)
     * @param type the type of the lab test to filter by (can be null to ignore)
     * @param method the method of the lab test to filter by (can be null to ignore)
     * @param active the active status to filter by (can be null to ignore)
     * @param pageable the pagination information
     * @return a page of {@link LabTestCatalogDTO} matching the search criteria
     */
    public Page<LabTestCatalogDTO> search(String name, LabTestType type, LabTestMethod method, Boolean active, Pageable pageable) {
        LOG.debug("Request to search LabTestCatalogs");
        // Normalise le paramètre name
        if (name == null) {
            name = "";
        }
        return labTestCatalogRepository.search(name, type, method, active, pageable).map(labTestCatalogMapper::toDto);
    }

    /**
     * Retrieves the latest version of each lab test catalog entry, grouped by name, and returns them as a list of DTOs.
     * <p>
     * This method fetches all lab test catalog entries using the provided {@link Pageable} object for pagination,
     * groups them by their name, and selects the entry with the highest version for each group.
     * The resulting latest versions are then mapped to {@link LabTestCatalogDTO} objects.
     * </p>
     *
     * @param pageable the pagination information to control the number of results returned
     * @return a list of {@link LabTestCatalogDTO} objects representing the latest version of each lab test catalog entry
     */
    public List<LabTestCatalogDTO> findLatestVersions(Pageable pageable) {
        return labTestCatalogRepository
            .findAll(pageable)
            .getContent()
            .stream()
            .collect(Collectors.groupingBy(LabTestCatalog::getName, Collectors.maxBy(Comparator.comparing(LabTestCatalog::getVersion))))
            .values()
            .stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(labTestCatalogMapper::toDto)
            .collect(Collectors.toList());
    }
}
