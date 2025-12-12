package com.ciatch.gdp.web.rest;

import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.service.DoctorPublicService;
import com.ciatch.gdp.service.criteria.DoctorProfileCriteria;
import com.ciatch.gdp.service.dto.DoctorPublicDTO;
import com.ciatch.gdp.service.mapper.DoctorPublicMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * Public REST controller for viewing {@link com.ciatch.gdp.domain.DoctorProfile}.
 * Provides read-only access to active doctors without authentication.
 */
@RestController
@RequestMapping("/api/public")
public class DoctorPublicResource {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorPublicResource.class);

    private final DoctorPublicService doctorPublicService;
    private final DoctorPublicMapper doctorPublicMapper;

    public DoctorPublicResource(DoctorPublicService doctorPublicService, DoctorPublicMapper doctorPublicMapper) {
        this.doctorPublicService = doctorPublicService;
        this.doctorPublicMapper = doctorPublicMapper;
    }

    /**
     * {@code GET  /public/doctors} : Get all active doctors with filtering and pagination.
     * This endpoint is publicly accessible without authentication.
     * Only doctors with status 'ACTIVE' are returned.
     *
     * @param criteria the criteria which the requested entities should match.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctors in body.
     */
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorPublicDTO>> getAllActiveDoctors(
        DoctorProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get active Doctors by criteria: {}", criteria);

        Page<DoctorProfile> doctorProfiles = doctorPublicService.findActiveDoctors(criteria, pageable);
        Page<DoctorPublicDTO> page = doctorProfiles.map(doctorPublicMapper::toDto);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /public/doctors/:uid} : Get a specific active doctor by UID.
     * This endpoint is publicly accessible without authentication.
     * Only returns the doctor if status is 'ACTIVE'.
     *
     * @param uid the UID of the doctor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctor,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctors/{uid}")
    public ResponseEntity<DoctorPublicDTO> getActiveDoctorByUid(@PathVariable("uid") UUID uid) {
        LOG.debug("REST request to get active Doctor by UID: {}", uid);

        Optional<DoctorPublicDTO> doctorPublicDTO = doctorPublicService.findActiveDoctorByUid(uid).map(doctorPublicMapper::toDto);

        return ResponseUtil.wrapOrNotFound(doctorPublicDTO);
    }

    /**
     * {@code GET  /public/doctors/count} : Count active doctors matching the criteria.
     * This endpoint is publicly accessible without authentication.
     * Only counts doctors with status 'ACTIVE'.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/doctors/count")
    public ResponseEntity<Long> countActiveDoctors(DoctorProfileCriteria criteria) {
        LOG.debug("REST request to count active Doctors by criteria: {}", criteria);
        return ResponseEntity.ok().body(doctorPublicService.countActiveDoctors(criteria));
    }
}
