package com.ciatch.gdp.web.rest;

import com.ciatch.gdp.repository.UserConfigurationRepository;
import com.ciatch.gdp.service.UserConfigurationService;
import com.ciatch.gdp.service.dto.UserConfigurationDTO;
import com.ciatch.gdp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ciatch.gdp.domain.UserConfiguration}.
 */
@RestController
@RequestMapping("/api/user-configurations")
public class UserConfigurationResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserConfigurationResource.class);

    private static final String ENTITY_NAME = "userConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserConfigurationService userConfigurationService;

    private final UserConfigurationRepository userConfigurationRepository;

    public UserConfigurationResource(
        UserConfigurationService userConfigurationService,
        UserConfigurationRepository userConfigurationRepository
    ) {
        this.userConfigurationService = userConfigurationService;
        this.userConfigurationRepository = userConfigurationRepository;
    }

    /**
     * {@code POST  /user-configurations} : Create a new userConfiguration.
     *
     * @param userConfigurationDTO the userConfigurationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userConfigurationDTO, or with status {@code 400 (Bad Request)} if the userConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserConfigurationDTO> createUserConfiguration(@Valid @RequestBody UserConfigurationDTO userConfigurationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserConfiguration : {}", userConfigurationDTO);
        if (userConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new userConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userConfigurationDTO = userConfigurationService.save(userConfigurationDTO);
        return ResponseEntity.created(new URI("/api/user-configurations/" + userConfigurationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userConfigurationDTO.getId().toString()))
            .body(userConfigurationDTO);
    }

    /**
     * {@code PUT  /user-configurations/:id} : Updates an existing userConfiguration.
     *
     * @param id the id of the userConfigurationDTO to save.
     * @param userConfigurationDTO the userConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the userConfigurationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserConfigurationDTO> updateUserConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserConfigurationDTO userConfigurationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserConfiguration : {}, {}", id, userConfigurationDTO);
        if (userConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userConfigurationDTO = userConfigurationService.update(userConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userConfigurationDTO.getId().toString()))
            .body(userConfigurationDTO);
    }

    /**
     * {@code PATCH  /user-configurations/:id} : Partial updates given fields of an existing userConfiguration, field will ignore if it is null
     *
     * @param id the id of the userConfigurationDTO to save.
     * @param userConfigurationDTO the userConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the userConfigurationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userConfigurationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserConfigurationDTO> partialUpdateUserConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserConfigurationDTO userConfigurationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserConfiguration partially : {}, {}", id, userConfigurationDTO);
        if (userConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserConfigurationDTO> result = userConfigurationService.partialUpdate(userConfigurationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userConfigurationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-configurations} : get all the userConfigurations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userConfigurations in body.
     */
    @GetMapping("")
    public List<UserConfigurationDTO> getAllUserConfigurations(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all UserConfigurations");
        return userConfigurationService.findAll();
    }

    /**
     * {@code GET  /user-configurations/:id} : get the "id" userConfiguration.
     *
     * @param id the id of the userConfigurationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userConfigurationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserConfigurationDTO> getUserConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserConfiguration : {}", id);
        Optional<UserConfigurationDTO> userConfigurationDTO = userConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userConfigurationDTO);
    }

    /**
     * {@code DELETE  /user-configurations/:id} : delete the "id" userConfiguration.
     *
     * @param id the id of the userConfigurationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserConfiguration : {}", id);
        userConfigurationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
