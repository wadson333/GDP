package com.ciatch.gdp.web.rest;

import com.ciatch.gdp.repository.PatientRepository;
import com.ciatch.gdp.security.AuthoritiesConstants;
import com.ciatch.gdp.service.PatientLifecycleService;
import com.ciatch.gdp.service.PatientQueryService;
import com.ciatch.gdp.service.PatientService;
import com.ciatch.gdp.service.criteria.PatientCriteria;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.dto.PatientUserDTO;
import com.ciatch.gdp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ciatch.gdp.domain.Patient}.
 */
@RestController
@RequestMapping("/api/patients")
public class PatientResource {

    private static final Logger LOG = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "patient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientService patientService;

    private final PatientRepository patientRepository;

    private final PatientQueryService patientQueryService;

    private final PatientLifecycleService patientLifecycleService;

    public PatientResource(
        PatientService patientService,
        PatientLifecycleService patientLifecycleService,
        PatientRepository patientRepository,
        PatientQueryService patientQueryService
    ) {
        this.patientService = patientService;
        this.patientRepository = patientRepository;
        this.patientQueryService = patientQueryService;
        this.patientLifecycleService = patientLifecycleService;
    }

    /**
     * {@code POST  /patients} : Create a new patient.
     *
     * @param patientDTO the patientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientDTO, or with status {@code 400 (Bad Request)} if the patient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) throws URISyntaxException {
        LOG.debug("REST request to save Patient : {}", patientDTO);
        if (patientDTO.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        patientDTO = patientService.save(patientDTO);
        return ResponseEntity.created(new URI("/api/patients/" + patientDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, patientDTO.getId().toString()))
            .body(patientDTO);
    }

    /**
     * {@code POST  /patient-with-user} : Create a new patient and associated user.
     * <p>
     * This operation is atomic: if either user or patient creation fails, the entire transaction is rolled back.
     *
     * @param patientUserDTO the DTO containing user and patient information.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientDTO.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-with-user")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<PatientDTO> createPatientWithUser(@Valid @RequestBody PatientUserDTO patientUserDTO) throws URISyntaxException {
        LOG.debug("REST request to create Patient with User : {}", patientUserDTO);

        // Note: Exceptions like NifAlreadyUsedException are thrown by the service
        // and automatically handled by JHipster's global exception translator
        // to return 400 Bad Request with the correct error headers.
        PatientDTO result = patientLifecycleService.createPatientWithUser(patientUserDTO);

        return ResponseEntity.created(new URI("/api/patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /patient-with-user/:uid} : Get patient with associated user by UID.
     *
     * @param uid the UUID of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-with-user/{uid}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<PatientUserDTO> getPatientWithUser(@PathVariable("uid") UUID uid) {
        LOG.debug("REST request to get Patient with User by UID : {}", uid);
        Optional<PatientUserDTO> patientUserDTO = patientLifecycleService.findPatientWithUserByUid(uid);
        return ResponseUtil.wrapOrNotFound(patientUserDTO);
    }

    /**
     * {@code PUT  /patient-with-user} : Update an existing patient and associated user.
     * <p>
     * This operation is atomic: if either user or patient update fails, the entire transaction is rolled back.
     *
     * @param patientUserDTO the DTO containing updated user and patient information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientDTO.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-with-user")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<PatientDTO> updatePatientWithUser(@Valid @RequestBody PatientUserDTO patientUserDTO) throws URISyntaxException {
        LOG.debug("REST request to update Patient with User : {}", patientUserDTO);

        if (patientUserDTO.getUid() == null) {
            throw new BadRequestAlertException("Invalid UID", ENTITY_NAME, "uidnull");
        }

        PatientDTO result = patientLifecycleService.updatePatientWithUser(patientUserDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patients/:id} : Updates an existing patient.
     *
     * @param id the id of the patientDTO to save.
     * @param patientDTO the patientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientDTO,
     * or with status {@code 400 (Bad Request)} if the patientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PatientDTO patientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Patient : {}, {}", id, patientDTO);
        if (patientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        patientDTO = patientService.update(patientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientDTO.getId().toString()))
            .body(patientDTO);
    }

    /**
     * {@code PATCH  /patients/:id} : Partial updates given fields of an existing patient, field will ignore if it is null
     *
     * @param id the id of the patientDTO to save.
     * @param patientDTO the patientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientDTO,
     * or with status {@code 400 (Bad Request)} if the patientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the patientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientDTO> partialUpdatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PatientDTO patientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Patient partially : {}, {}", id, patientDTO);
        if (patientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientDTO> result = patientService.partialUpdate(patientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /patients} : get all the patients.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patients in body.
     */
    // @GetMapping("")
    // public ResponseEntity<List<PatientDTO>> getAllPatients(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    //     LOG.debug("REST request to get a page of Patients");
    //     Page<PatientDTO> page = patientService.findAll(pageable);
    //     HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    //     return ResponseEntity.ok().headers(headers).body(page.getContent());
    // }

    /**
     * {@code GET  /patients} : get all the patients with advanced filtering.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patients in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PatientDTO>> getAllPatients(
        PatientCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Patients by criteria: {}", criteria);
        Page<PatientDTO> page = patientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patients/count} : count all the patients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPatients(PatientCriteria criteria) {
        LOG.debug("REST request to count Patients by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patients/:id} : get the "id" patient.
     *
     * @param id the id of the patientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Patient : {}", id);
        Optional<PatientDTO> patientDTO = patientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientDTO);
    }

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patient.
     *
     * @param id the id of the patientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Patient : {}", id);
        patientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
