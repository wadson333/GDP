package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.domain.enumeration.LabTestMethod;
import com.ciatch.gdp.domain.enumeration.LabTestType;
import com.ciatch.gdp.repository.LabTestCatalogRepository;
import com.ciatch.gdp.service.dto.LabTestCatalogDTO;
import com.ciatch.gdp.service.mapper.LabTestCatalogMapper;
import com.ciatch.gdp.web.rest.errors.BadRequestAlertException;
import com.ciatch.gdp.web.rest.errors.LabTestAlreadyInactiveException;
import com.ciatch.gdp.web.rest.errors.LabTestNameAlreadyUsedException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private static final String ENTITY_NAME = "labTestCatalog";

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
        // if (labTestCatalogDTO.getId() == null &&
        //     labTestCatalogRepository.findLatestVersionByName(labTestCatalogDTO.getName()) != null) {
        //     throw new LabTestNameAlreadyUsedException();
        // }

        // // Set default values for new entries
        // if (labTestCatalogDTO.getId() == null) {
        //     labTestCatalogDTO.setActive(true);
        //     labTestCatalogDTO.setVersion(1);
        // }

        // if (labTestCatalogDTO.getValidFrom() == null) {
        //     labTestCatalogDTO.setValidFrom(java.time.Instant.now());
        // }

        // if (labTestCatalogDTO.getVersion() == null) {
        //     labTestCatalogDTO.setVersion(1);
        // }

        this.verificationBeforeSaving(labTestCatalogDTO);

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

        this.verificationBeforeSaving(labTestCatalogDTO);

        if (
            labTestCatalogDTO.getId() != null
            // &&  labTestCatalogRepository.findAllVersionsByNameAndVersion(labTestCatalogDTO.getName(), labTestCatalogDTO.getVersion()) != null
        ) {
            LOG.debug("Potential duplicate lab test detected : {}", labTestCatalogDTO);
            // If name is being changed, check for uniqueness
            List<LabTestCatalog> allVersions = labTestCatalogRepository.findAllVersionsByName(labTestCatalogDTO.getName());
            LOG.debug("All versions for name '{}': {}", labTestCatalogDTO.getName(), allVersions);
            if (!allVersions.isEmpty()) {
                for (LabTestCatalog existing : allVersions) {
                    LOG.debug("All versions for name '{}': {}", labTestCatalogDTO, existing);
                    if (existing.getVersion() == labTestCatalogDTO.getVersion() && !existing.getId().equals(labTestCatalogDTO.getId())) { // Use .equals()
                        throw new LabTestNameAlreadyUsedException();
                    }
                }
            }
            // if the lab test is inactive, prevent editing
            LabTestCatalogDTO existingLabTest = labTestCatalogMapper.toDto(
                labTestCatalogRepository
                    .findById(labTestCatalogDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Lab test not found with id: " + labTestCatalogDTO.getId()))
            );

            if (existingLabTest.getActive() == false) {
                throw new IllegalStateException("Can't edit an inactive lab test id: " + existingLabTest.getId());
            }
            // if (labTestCatalogRepository.findAllVersionsByName(labTestCatalogDTO.getName()).stream()
            //     .anyMatch(existing ->
            //         existing.getVersion() == labTestCatalogDTO.getVersion()
            //         && existing.getName().equalsIgnoreCase(labTestCatalogDTO.getName())
            //         && existing.getId() != labTestCatalogDTO.getId()) ) {
            //     throw new LabTestNameAlreadyUsedException();
            // }
            // throw new LabTestNameAlreadyUsedException();
        }

        if (labTestCatalogDTO.getVersion() == null) {
            throw new IllegalArgumentException("ID can't be null when updading a lab test");
        }

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
    public Page<LabTestCatalogDTO> search(
        String name,
        LabTestType type,
        LabTestMethod method,
        Boolean active,
        Pageable pageable,
        boolean isLatestOnly
    ) {
        LOG.debug("Request to search LabTestCatalogs");
        // Normalise le paramètre name
        if (name == null) {
            name = "";
        }
        return isLatestOnly == true
            ? labTestCatalogRepository.searchWithLastVersion(name, type, method, active, pageable).map(labTestCatalogMapper::toDto)
            : labTestCatalogRepository.search(name, type, method, active, pageable).map(labTestCatalogMapper::toDto);
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
     * @return a Page of {@link LabTestCatalogDTO} objects representing the latest version of each lab test catalog entry
     */
    public Page<LabTestCatalogDTO> findLatestVersions(Pageable pageable) {
        // return labTestCatalogRepository
        //     .findAll(pageable)
        //     .getContent()
        //     .stream()
        //     .collect(Collectors.groupingBy(LabTestCatalog::getName, Collectors.maxBy(Comparator.comparing(LabTestCatalog::getVersion))))
        //     .values()
        //     .stream()
        //     .filter(Optional::isPresent)
        //     .map(Optional::get)
        //     .map(labTestCatalogMapper::toDto)
        //     .collect(Collectors.toList());
        // List<LabTestCatalog> allEntries = labTestCatalogRepository.findAll();

        // List<LabTestCatalog> latestVersions = allEntries.stream()
        //     .collect(Collectors.groupingBy(LabTestCatalog::getName))
        //     .values().stream()
        //     .map(versions -> versions.stream()
        //         .max(Comparator.comparing(LabTestCatalog::getVersion))
        //         .orElseThrow())
        //     .collect(Collectors.toList());

        // // Create a new PageImpl with the latest versions
        // return new PageImpl<>(
        //     latestVersions.stream()
        //         .map(labTestCatalogMapper::toDto)
        //         .collect(Collectors.toList()),
        //     pageable,
        //     labTestCatalogRepository.count() // Total count for pagination
        // );
        LOG.debug("Request to get latest versions of LabTestCatalogs");
        return labTestCatalogRepository.findLatestVersions(pageable).map(labTestCatalogMapper::toDto);
    }

    /**
     * Get all versions of a lab test catalog by its name.
     *
     * @param name the name of the lab test catalog
     * @return the list of all versions
     */
    @Transactional(readOnly = true)
    public List<LabTestCatalogDTO> findAllVersionsByName(String name) {
        LOG.debug("Request to get all versions of LabTestCatalog : {}", name);
        return labTestCatalogRepository.findAllVersionsByName(name).stream().map(labTestCatalogMapper::toDto).collect(Collectors.toList());
    }

    public void verificationBeforeSaving(LabTestCatalogDTO labTestCatalogDTO) {
        if (
            labTestCatalogDTO.getValidFrom() != null &&
            labTestCatalogDTO.getValidTo() != null &&
            labTestCatalogDTO.getValidFrom().isAfter(labTestCatalogDTO.getValidTo())
        ) {
            throw new IllegalArgumentException("Valid from date must be before valid to date");
        }

        if (
            labTestCatalogDTO.getReferenceRangeLow() != null &&
            labTestCatalogDTO.getReferenceRangeHigh() != null &&
            labTestCatalogDTO.getReferenceRangeLow().compareTo(labTestCatalogDTO.getReferenceRangeHigh()) >= 0
        ) {
            throw new IllegalArgumentException("Reference range low must be less than reference range high");
        }
    }

    /**
     * Deactivates a lab test catalog version.
     *
     * @param id the id of the lab test catalog to deactivate
     * @return the updated LabTestCatalogDTO
     * @throws LabTestAlreadyInactiveException if the test is already inactive
     */
    public LabTestCatalogDTO deactivate(LabTestCatalog labTestCatalog) {
        LOG.debug("Request to deactivate LabTestCatalog : {}", labTestCatalog);

        labTestCatalog.setActive(false);
        labTestCatalog.setValidTo(Instant.now());

        labTestCatalog = labTestCatalogRepository.save(labTestCatalog);
        return labTestCatalogMapper.toDto(labTestCatalog);
    }

    /**
     * Prepare a new version of an existing lab test catalog.
     *
     * @param id the id of the lab test catalog to create a new version from
     * @return the pre-filled DTO for the new version
     */
    public LabTestCatalogDTO prepareNewVersion(Long id) {
        LOG.debug("Request to prepare new version of LabTestCatalog : {}", id);

        LabTestCatalog currentVersion = labTestCatalogRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        // Check if this is the latest version
        LabTestCatalog latestVersion = labTestCatalogRepository.findLatestVersionByName(currentVersion.getName());
        if (latestVersion.getId() != currentVersion.getId()) {
            throw new BadRequestAlertException("Cannot create new version from old version", ENTITY_NAME, "oldversion");
        }

        return labTestCatalogMapper.toDto(currentVersion);
    }

    /**
     * Create a new version of a lab test catalog.
     *
     * @param newVersionDTO the new version to create
     * @param originalId the id of the original version
     * @return the created new version
     */
    public LabTestCatalogDTO createNewVersion(LabTestCatalogDTO newVersionDTO, Long originalId) {
        LOG.debug("Request to create new version of LabTestCatalog : {}", newVersionDTO);

        // Get the original version
        LabTestCatalog originalVersion = labTestCatalogRepository
            .findById(originalId)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        // Ensure name hasn't been changed
        if (!originalVersion.getName().equals(newVersionDTO.getName())) {
            throw new BadRequestAlertException("Name cannot be changed in new version", ENTITY_NAME, "namechange");
        }

        // Deactivate the current version
        originalVersion.setActive(false);
        originalVersion.setValidTo(Instant.now());
        labTestCatalogRepository.save(originalVersion);

        // Create new version
        newVersionDTO.setId(null);
        newVersionDTO.setValidFrom(Instant.now());
        newVersionDTO.setValidTo(null);
        newVersionDTO.setVersion(originalVersion.getVersion() + 1);
        newVersionDTO.setActive(true);

        verificationBeforeSaving(newVersionDTO);

        LabTestCatalog newVersion = labTestCatalogMapper.toEntity(newVersionDTO);
        newVersion = labTestCatalogRepository.save(newVersion);

        return labTestCatalogMapper.toDto(newVersion);
    }
}
