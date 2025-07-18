package com.ciatch.gdp.web.rest;

import com.ciatch.gdp.repository.PrescriptionItemRepository;
import com.ciatch.gdp.service.PrescriptionItemService;
import com.ciatch.gdp.service.dto.PrescriptionItemDTO;
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
 * REST controller for managing {@link com.ciatch.gdp.domain.PrescriptionItem}.
 */
@RestController
@RequestMapping("/api/prescription-items")
public class PrescriptionItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(PrescriptionItemResource.class);

    private static final String ENTITY_NAME = "prescriptionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrescriptionItemService prescriptionItemService;

    private final PrescriptionItemRepository prescriptionItemRepository;

    public PrescriptionItemResource(
        PrescriptionItemService prescriptionItemService,
        PrescriptionItemRepository prescriptionItemRepository
    ) {
        this.prescriptionItemService = prescriptionItemService;
        this.prescriptionItemRepository = prescriptionItemRepository;
    }

    /**
     * {@code POST  /prescription-items} : Create a new prescriptionItem.
     *
     * @param prescriptionItemDTO the prescriptionItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prescriptionItemDTO, or with status {@code 400 (Bad Request)} if the prescriptionItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PrescriptionItemDTO> createPrescriptionItem(@Valid @RequestBody PrescriptionItemDTO prescriptionItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PrescriptionItem : {}", prescriptionItemDTO);
        if (prescriptionItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new prescriptionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prescriptionItemDTO = prescriptionItemService.save(prescriptionItemDTO);
        return ResponseEntity.created(new URI("/api/prescription-items/" + prescriptionItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prescriptionItemDTO.getId().toString()))
            .body(prescriptionItemDTO);
    }

    /**
     * {@code PUT  /prescription-items/:id} : Updates an existing prescriptionItem.
     *
     * @param id the id of the prescriptionItemDTO to save.
     * @param prescriptionItemDTO the prescriptionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescriptionItemDTO,
     * or with status {@code 400 (Bad Request)} if the prescriptionItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prescriptionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionItemDTO> updatePrescriptionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrescriptionItemDTO prescriptionItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PrescriptionItem : {}, {}", id, prescriptionItemDTO);
        if (prescriptionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescriptionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescriptionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prescriptionItemDTO = prescriptionItemService.update(prescriptionItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prescriptionItemDTO.getId().toString()))
            .body(prescriptionItemDTO);
    }

    /**
     * {@code PATCH  /prescription-items/:id} : Partial updates given fields of an existing prescriptionItem, field will ignore if it is null
     *
     * @param id the id of the prescriptionItemDTO to save.
     * @param prescriptionItemDTO the prescriptionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescriptionItemDTO,
     * or with status {@code 400 (Bad Request)} if the prescriptionItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prescriptionItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prescriptionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrescriptionItemDTO> partialUpdatePrescriptionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrescriptionItemDTO prescriptionItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PrescriptionItem partially : {}, {}", id, prescriptionItemDTO);
        if (prescriptionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescriptionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescriptionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrescriptionItemDTO> result = prescriptionItemService.partialUpdate(prescriptionItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prescriptionItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prescription-items} : get all the prescriptionItems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prescriptionItems in body.
     */
    @GetMapping("")
    public List<PrescriptionItemDTO> getAllPrescriptionItems(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PrescriptionItems");
        return prescriptionItemService.findAll();
    }

    /**
     * {@code GET  /prescription-items/:id} : get the "id" prescriptionItem.
     *
     * @param id the id of the prescriptionItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prescriptionItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionItemDTO> getPrescriptionItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PrescriptionItem : {}", id);
        Optional<PrescriptionItemDTO> prescriptionItemDTO = prescriptionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prescriptionItemDTO);
    }

    /**
     * {@code DELETE  /prescription-items/:id} : delete the "id" prescriptionItem.
     *
     * @param id the id of the prescriptionItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescriptionItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PrescriptionItem : {}", id);
        prescriptionItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
