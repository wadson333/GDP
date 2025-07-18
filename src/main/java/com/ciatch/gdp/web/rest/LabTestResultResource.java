package com.ciatch.gdp.web.rest;

import com.ciatch.gdp.repository.LabTestResultRepository;
import com.ciatch.gdp.service.LabTestResultService;
import com.ciatch.gdp.service.dto.LabTestResultDTO;
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
 * REST controller for managing {@link com.ciatch.gdp.domain.LabTestResult}.
 */
@RestController
@RequestMapping("/api/lab-test-results")
public class LabTestResultResource {

    private static final Logger LOG = LoggerFactory.getLogger(LabTestResultResource.class);

    private static final String ENTITY_NAME = "labTestResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LabTestResultService labTestResultService;

    private final LabTestResultRepository labTestResultRepository;

    public LabTestResultResource(LabTestResultService labTestResultService, LabTestResultRepository labTestResultRepository) {
        this.labTestResultService = labTestResultService;
        this.labTestResultRepository = labTestResultRepository;
    }

    /**
     * {@code POST  /lab-test-results} : Create a new labTestResult.
     *
     * @param labTestResultDTO the labTestResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new labTestResultDTO, or with status {@code 400 (Bad Request)} if the labTestResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LabTestResultDTO> createLabTestResult(@Valid @RequestBody LabTestResultDTO labTestResultDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LabTestResult : {}", labTestResultDTO);
        if (labTestResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new labTestResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        labTestResultDTO = labTestResultService.save(labTestResultDTO);
        return ResponseEntity.created(new URI("/api/lab-test-results/" + labTestResultDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, labTestResultDTO.getId().toString()))
            .body(labTestResultDTO);
    }

    /**
     * {@code PUT  /lab-test-results/:id} : Updates an existing labTestResult.
     *
     * @param id the id of the labTestResultDTO to save.
     * @param labTestResultDTO the labTestResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated labTestResultDTO,
     * or with status {@code 400 (Bad Request)} if the labTestResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the labTestResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LabTestResultDTO> updateLabTestResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LabTestResultDTO labTestResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LabTestResult : {}, {}", id, labTestResultDTO);
        if (labTestResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, labTestResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!labTestResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        labTestResultDTO = labTestResultService.update(labTestResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, labTestResultDTO.getId().toString()))
            .body(labTestResultDTO);
    }

    /**
     * {@code PATCH  /lab-test-results/:id} : Partial updates given fields of an existing labTestResult, field will ignore if it is null
     *
     * @param id the id of the labTestResultDTO to save.
     * @param labTestResultDTO the labTestResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated labTestResultDTO,
     * or with status {@code 400 (Bad Request)} if the labTestResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the labTestResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the labTestResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LabTestResultDTO> partialUpdateLabTestResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LabTestResultDTO labTestResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LabTestResult partially : {}, {}", id, labTestResultDTO);
        if (labTestResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, labTestResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!labTestResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LabTestResultDTO> result = labTestResultService.partialUpdate(labTestResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, labTestResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lab-test-results} : get all the labTestResults.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of labTestResults in body.
     */
    @GetMapping("")
    public List<LabTestResultDTO> getAllLabTestResults(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all LabTestResults");
        return labTestResultService.findAll();
    }

    /**
     * {@code GET  /lab-test-results/:id} : get the "id" labTestResult.
     *
     * @param id the id of the labTestResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the labTestResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LabTestResultDTO> getLabTestResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LabTestResult : {}", id);
        Optional<LabTestResultDTO> labTestResultDTO = labTestResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(labTestResultDTO);
    }

    /**
     * {@code DELETE  /lab-test-results/:id} : delete the "id" labTestResult.
     *
     * @param id the id of the labTestResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabTestResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LabTestResult : {}", id);
        labTestResultService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
