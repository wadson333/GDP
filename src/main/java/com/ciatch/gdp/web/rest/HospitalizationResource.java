package com.ciatch.gdp.web.rest;

import com.ciatch.gdp.repository.HospitalizationRepository;
import com.ciatch.gdp.service.HospitalizationService;
import com.ciatch.gdp.service.dto.HospitalizationDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ciatch.gdp.domain.Hospitalization}.
 */
@RestController
@RequestMapping("/api/hospitalizations")
public class HospitalizationResource {

    private static final Logger LOG = LoggerFactory.getLogger(HospitalizationResource.class);

    private static final String ENTITY_NAME = "hospitalization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HospitalizationService hospitalizationService;

    private final HospitalizationRepository hospitalizationRepository;

    public HospitalizationResource(HospitalizationService hospitalizationService, HospitalizationRepository hospitalizationRepository) {
        this.hospitalizationService = hospitalizationService;
        this.hospitalizationRepository = hospitalizationRepository;
    }

    /**
     * {@code POST  /hospitalizations} : Create a new hospitalization.
     *
     * @param hospitalizationDTO the hospitalizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hospitalizationDTO, or with status {@code 400 (Bad Request)} if the hospitalization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HospitalizationDTO> createHospitalization(@Valid @RequestBody HospitalizationDTO hospitalizationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Hospitalization : {}", hospitalizationDTO);
        if (hospitalizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new hospitalization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hospitalizationDTO = hospitalizationService.save(hospitalizationDTO);
        return ResponseEntity.created(new URI("/api/hospitalizations/" + hospitalizationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hospitalizationDTO.getId().toString()))
            .body(hospitalizationDTO);
    }

    /**
     * {@code PUT  /hospitalizations/:id} : Updates an existing hospitalization.
     *
     * @param id the id of the hospitalizationDTO to save.
     * @param hospitalizationDTO the hospitalizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospitalizationDTO,
     * or with status {@code 400 (Bad Request)} if the hospitalizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hospitalizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HospitalizationDTO> updateHospitalization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HospitalizationDTO hospitalizationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Hospitalization : {}, {}", id, hospitalizationDTO);
        if (hospitalizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospitalizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hospitalizationDTO = hospitalizationService.update(hospitalizationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hospitalizationDTO.getId().toString()))
            .body(hospitalizationDTO);
    }

    /**
     * {@code PATCH  /hospitalizations/:id} : Partial updates given fields of an existing hospitalization, field will ignore if it is null
     *
     * @param id the id of the hospitalizationDTO to save.
     * @param hospitalizationDTO the hospitalizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospitalizationDTO,
     * or with status {@code 400 (Bad Request)} if the hospitalizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hospitalizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hospitalizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HospitalizationDTO> partialUpdateHospitalization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HospitalizationDTO hospitalizationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Hospitalization partially : {}, {}", id, hospitalizationDTO);
        if (hospitalizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospitalizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HospitalizationDTO> result = hospitalizationService.partialUpdate(hospitalizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hospitalizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hospitalizations} : get all the hospitalizations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitalizations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HospitalizationDTO>> getAllHospitalizations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Hospitalizations");
        Page<HospitalizationDTO> page;
        if (eagerload) {
            page = hospitalizationService.findAllWithEagerRelationships(pageable);
        } else {
            page = hospitalizationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hospitalizations/:id} : get the "id" hospitalization.
     *
     * @param id the id of the hospitalizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospitalizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HospitalizationDTO> getHospitalization(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Hospitalization : {}", id);
        Optional<HospitalizationDTO> hospitalizationDTO = hospitalizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hospitalizationDTO);
    }

    /**
     * {@code DELETE  /hospitalizations/:id} : delete the "id" hospitalization.
     *
     * @param id the id of the hospitalizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospitalization(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Hospitalization : {}", id);
        hospitalizationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
