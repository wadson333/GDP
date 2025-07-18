package com.ciatch.gdp.web.rest;

import com.ciatch.gdp.repository.LabTestCatalogRepository;
import com.ciatch.gdp.service.LabTestCatalogService;
import com.ciatch.gdp.service.dto.LabTestCatalogDTO;
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
 * REST controller for managing {@link com.ciatch.gdp.domain.LabTestCatalog}.
 */
@RestController
@RequestMapping("/api/lab-test-catalogs")
public class LabTestCatalogResource {

    private static final Logger LOG = LoggerFactory.getLogger(LabTestCatalogResource.class);

    private static final String ENTITY_NAME = "labTestCatalog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LabTestCatalogService labTestCatalogService;

    private final LabTestCatalogRepository labTestCatalogRepository;

    public LabTestCatalogResource(LabTestCatalogService labTestCatalogService, LabTestCatalogRepository labTestCatalogRepository) {
        this.labTestCatalogService = labTestCatalogService;
        this.labTestCatalogRepository = labTestCatalogRepository;
    }

    /**
     * {@code POST  /lab-test-catalogs} : Create a new labTestCatalog.
     *
     * @param labTestCatalogDTO the labTestCatalogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new labTestCatalogDTO, or with status {@code 400 (Bad Request)} if the labTestCatalog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LabTestCatalogDTO> createLabTestCatalog(@Valid @RequestBody LabTestCatalogDTO labTestCatalogDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LabTestCatalog : {}", labTestCatalogDTO);
        if (labTestCatalogDTO.getId() != null) {
            throw new BadRequestAlertException("A new labTestCatalog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        labTestCatalogDTO = labTestCatalogService.save(labTestCatalogDTO);
        return ResponseEntity.created(new URI("/api/lab-test-catalogs/" + labTestCatalogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, labTestCatalogDTO.getId().toString()))
            .body(labTestCatalogDTO);
    }

    /**
     * {@code PUT  /lab-test-catalogs/:id} : Updates an existing labTestCatalog.
     *
     * @param id the id of the labTestCatalogDTO to save.
     * @param labTestCatalogDTO the labTestCatalogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated labTestCatalogDTO,
     * or with status {@code 400 (Bad Request)} if the labTestCatalogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the labTestCatalogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LabTestCatalogDTO> updateLabTestCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LabTestCatalogDTO labTestCatalogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LabTestCatalog : {}, {}", id, labTestCatalogDTO);
        if (labTestCatalogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, labTestCatalogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!labTestCatalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        labTestCatalogDTO = labTestCatalogService.update(labTestCatalogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, labTestCatalogDTO.getId().toString()))
            .body(labTestCatalogDTO);
    }

    /**
     * {@code PATCH  /lab-test-catalogs/:id} : Partial updates given fields of an existing labTestCatalog, field will ignore if it is null
     *
     * @param id the id of the labTestCatalogDTO to save.
     * @param labTestCatalogDTO the labTestCatalogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated labTestCatalogDTO,
     * or with status {@code 400 (Bad Request)} if the labTestCatalogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the labTestCatalogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the labTestCatalogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LabTestCatalogDTO> partialUpdateLabTestCatalog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LabTestCatalogDTO labTestCatalogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LabTestCatalog partially : {}, {}", id, labTestCatalogDTO);
        if (labTestCatalogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, labTestCatalogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!labTestCatalogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LabTestCatalogDTO> result = labTestCatalogService.partialUpdate(labTestCatalogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, labTestCatalogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lab-test-catalogs} : get all the labTestCatalogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of labTestCatalogs in body.
     */
    @GetMapping("")
    public List<LabTestCatalogDTO> getAllLabTestCatalogs() {
        LOG.debug("REST request to get all LabTestCatalogs");
        return labTestCatalogService.findAll();
    }

    /**
     * {@code GET  /lab-test-catalogs/:id} : get the "id" labTestCatalog.
     *
     * @param id the id of the labTestCatalogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the labTestCatalogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LabTestCatalogDTO> getLabTestCatalog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LabTestCatalog : {}", id);
        Optional<LabTestCatalogDTO> labTestCatalogDTO = labTestCatalogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(labTestCatalogDTO);
    }

    /**
     * {@code DELETE  /lab-test-catalogs/:id} : delete the "id" labTestCatalog.
     *
     * @param id the id of the labTestCatalogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabTestCatalog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LabTestCatalog : {}", id);
        labTestCatalogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
