package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.UserConfiguration;
import com.ciatch.gdp.repository.UserConfigurationRepository;
import com.ciatch.gdp.service.dto.UserConfigurationDTO;
import com.ciatch.gdp.service.mapper.UserConfigurationMapper;
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
 * Service Implementation for managing {@link com.ciatch.gdp.domain.UserConfiguration}.
 */
@Service
@Transactional
public class UserConfigurationService {

    private static final Logger LOG = LoggerFactory.getLogger(UserConfigurationService.class);

    private final UserConfigurationRepository userConfigurationRepository;

    private final UserConfigurationMapper userConfigurationMapper;

    public UserConfigurationService(
        UserConfigurationRepository userConfigurationRepository,
        UserConfigurationMapper userConfigurationMapper
    ) {
        this.userConfigurationRepository = userConfigurationRepository;
        this.userConfigurationMapper = userConfigurationMapper;
    }

    /**
     * Save a userConfiguration.
     *
     * @param userConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    public UserConfigurationDTO save(UserConfigurationDTO userConfigurationDTO) {
        LOG.debug("Request to save UserConfiguration : {}", userConfigurationDTO);
        UserConfiguration userConfiguration = userConfigurationMapper.toEntity(userConfigurationDTO);
        userConfiguration = userConfigurationRepository.save(userConfiguration);
        return userConfigurationMapper.toDto(userConfiguration);
    }

    /**
     * Update a userConfiguration.
     *
     * @param userConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    public UserConfigurationDTO update(UserConfigurationDTO userConfigurationDTO) {
        LOG.debug("Request to update UserConfiguration : {}", userConfigurationDTO);
        UserConfiguration userConfiguration = userConfigurationMapper.toEntity(userConfigurationDTO);
        userConfiguration = userConfigurationRepository.save(userConfiguration);
        return userConfigurationMapper.toDto(userConfiguration);
    }

    /**
     * Partially update a userConfiguration.
     *
     * @param userConfigurationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserConfigurationDTO> partialUpdate(UserConfigurationDTO userConfigurationDTO) {
        LOG.debug("Request to partially update UserConfiguration : {}", userConfigurationDTO);

        return userConfigurationRepository
            .findById(userConfigurationDTO.getId())
            .map(existingUserConfiguration -> {
                userConfigurationMapper.partialUpdate(existingUserConfiguration, userConfigurationDTO);

                return existingUserConfiguration;
            })
            .map(userConfigurationRepository::save)
            .map(userConfigurationMapper::toDto);
    }

    /**
     * Get all the userConfigurations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserConfigurationDTO> findAll() {
        LOG.debug("Request to get all UserConfigurations");
        return userConfigurationRepository
            .findAll()
            .stream()
            .map(userConfigurationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the userConfigurations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserConfigurationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userConfigurationRepository.findAllWithEagerRelationships(pageable).map(userConfigurationMapper::toDto);
    }

    /**
     * Get one userConfiguration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserConfigurationDTO> findOne(Long id) {
        LOG.debug("Request to get UserConfiguration : {}", id);
        return userConfigurationRepository.findOneWithEagerRelationships(id).map(userConfigurationMapper::toDto);
    }

    /**
     * Delete the userConfiguration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserConfiguration : {}", id);
        userConfigurationRepository.deleteById(id);
    }
}
