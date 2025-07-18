package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.PrescriptionItem;
import com.ciatch.gdp.repository.PrescriptionItemRepository;
import com.ciatch.gdp.service.dto.PrescriptionItemDTO;
import com.ciatch.gdp.service.mapper.PrescriptionItemMapper;
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
 * Service Implementation for managing {@link com.ciatch.gdp.domain.PrescriptionItem}.
 */
@Service
@Transactional
public class PrescriptionItemService {

    private static final Logger LOG = LoggerFactory.getLogger(PrescriptionItemService.class);

    private final PrescriptionItemRepository prescriptionItemRepository;

    private final PrescriptionItemMapper prescriptionItemMapper;

    public PrescriptionItemService(PrescriptionItemRepository prescriptionItemRepository, PrescriptionItemMapper prescriptionItemMapper) {
        this.prescriptionItemRepository = prescriptionItemRepository;
        this.prescriptionItemMapper = prescriptionItemMapper;
    }

    /**
     * Save a prescriptionItem.
     *
     * @param prescriptionItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionItemDTO save(PrescriptionItemDTO prescriptionItemDTO) {
        LOG.debug("Request to save PrescriptionItem : {}", prescriptionItemDTO);
        PrescriptionItem prescriptionItem = prescriptionItemMapper.toEntity(prescriptionItemDTO);
        prescriptionItem = prescriptionItemRepository.save(prescriptionItem);
        return prescriptionItemMapper.toDto(prescriptionItem);
    }

    /**
     * Update a prescriptionItem.
     *
     * @param prescriptionItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PrescriptionItemDTO update(PrescriptionItemDTO prescriptionItemDTO) {
        LOG.debug("Request to update PrescriptionItem : {}", prescriptionItemDTO);
        PrescriptionItem prescriptionItem = prescriptionItemMapper.toEntity(prescriptionItemDTO);
        prescriptionItem = prescriptionItemRepository.save(prescriptionItem);
        return prescriptionItemMapper.toDto(prescriptionItem);
    }

    /**
     * Partially update a prescriptionItem.
     *
     * @param prescriptionItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrescriptionItemDTO> partialUpdate(PrescriptionItemDTO prescriptionItemDTO) {
        LOG.debug("Request to partially update PrescriptionItem : {}", prescriptionItemDTO);

        return prescriptionItemRepository
            .findById(prescriptionItemDTO.getId())
            .map(existingPrescriptionItem -> {
                prescriptionItemMapper.partialUpdate(existingPrescriptionItem, prescriptionItemDTO);

                return existingPrescriptionItem;
            })
            .map(prescriptionItemRepository::save)
            .map(prescriptionItemMapper::toDto);
    }

    /**
     * Get all the prescriptionItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PrescriptionItemDTO> findAll() {
        LOG.debug("Request to get all PrescriptionItems");
        return prescriptionItemRepository
            .findAll()
            .stream()
            .map(prescriptionItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the prescriptionItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PrescriptionItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prescriptionItemRepository.findAllWithEagerRelationships(pageable).map(prescriptionItemMapper::toDto);
    }

    /**
     * Get one prescriptionItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrescriptionItemDTO> findOne(Long id) {
        LOG.debug("Request to get PrescriptionItem : {}", id);
        return prescriptionItemRepository.findOneWithEagerRelationships(id).map(prescriptionItemMapper::toDto);
    }

    /**
     * Delete the prescriptionItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PrescriptionItem : {}", id);
        prescriptionItemRepository.deleteById(id);
    }
}
