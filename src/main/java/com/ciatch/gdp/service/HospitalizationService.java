package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.Hospitalization;
import com.ciatch.gdp.repository.HospitalizationRepository;
import com.ciatch.gdp.service.dto.HospitalizationDTO;
import com.ciatch.gdp.service.mapper.HospitalizationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ciatch.gdp.domain.Hospitalization}.
 */
@Service
@Transactional
public class HospitalizationService {

    private static final Logger LOG = LoggerFactory.getLogger(HospitalizationService.class);

    private final HospitalizationRepository hospitalizationRepository;

    private final HospitalizationMapper hospitalizationMapper;

    public HospitalizationService(HospitalizationRepository hospitalizationRepository, HospitalizationMapper hospitalizationMapper) {
        this.hospitalizationRepository = hospitalizationRepository;
        this.hospitalizationMapper = hospitalizationMapper;
    }

    /**
     * Save a hospitalization.
     *
     * @param hospitalizationDTO the entity to save.
     * @return the persisted entity.
     */
    public HospitalizationDTO save(HospitalizationDTO hospitalizationDTO) {
        LOG.debug("Request to save Hospitalization : {}", hospitalizationDTO);
        Hospitalization hospitalization = hospitalizationMapper.toEntity(hospitalizationDTO);
        hospitalization = hospitalizationRepository.save(hospitalization);
        return hospitalizationMapper.toDto(hospitalization);
    }

    /**
     * Update a hospitalization.
     *
     * @param hospitalizationDTO the entity to save.
     * @return the persisted entity.
     */
    public HospitalizationDTO update(HospitalizationDTO hospitalizationDTO) {
        LOG.debug("Request to update Hospitalization : {}", hospitalizationDTO);
        Hospitalization hospitalization = hospitalizationMapper.toEntity(hospitalizationDTO);
        hospitalization = hospitalizationRepository.save(hospitalization);
        return hospitalizationMapper.toDto(hospitalization);
    }

    /**
     * Partially update a hospitalization.
     *
     * @param hospitalizationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HospitalizationDTO> partialUpdate(HospitalizationDTO hospitalizationDTO) {
        LOG.debug("Request to partially update Hospitalization : {}", hospitalizationDTO);

        return hospitalizationRepository
            .findById(hospitalizationDTO.getId())
            .map(existingHospitalization -> {
                hospitalizationMapper.partialUpdate(existingHospitalization, hospitalizationDTO);

                return existingHospitalization;
            })
            .map(hospitalizationRepository::save)
            .map(hospitalizationMapper::toDto);
    }

    /**
     * Get all the hospitalizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HospitalizationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Hospitalizations");
        return hospitalizationRepository.findAll(pageable).map(hospitalizationMapper::toDto);
    }

    /**
     * Get all the hospitalizations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HospitalizationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return hospitalizationRepository.findAllWithEagerRelationships(pageable).map(hospitalizationMapper::toDto);
    }

    /**
     * Get one hospitalization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HospitalizationDTO> findOne(Long id) {
        LOG.debug("Request to get Hospitalization : {}", id);
        return hospitalizationRepository.findOneWithEagerRelationships(id).map(hospitalizationMapper::toDto);
    }

    /**
     * Delete the hospitalization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Hospitalization : {}", id);
        hospitalizationRepository.deleteById(id);
    }
}
