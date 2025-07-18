package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.HospitalizationAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ciatch.gdp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Hospitalization;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.repository.HospitalizationRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.service.HospitalizationService;
import com.ciatch.gdp.service.dto.HospitalizationDTO;
import com.ciatch.gdp.service.mapper.HospitalizationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HospitalizationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HospitalizationResourceIT {

    private static final ZonedDateTime DEFAULT_ADMISSION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ADMISSION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DISCHARGE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DISCHARGE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hospitalizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HospitalizationRepository hospitalizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private HospitalizationRepository hospitalizationRepositoryMock;

    @Autowired
    private HospitalizationMapper hospitalizationMapper;

    @Mock
    private HospitalizationService hospitalizationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHospitalizationMockMvc;

    private Hospitalization hospitalization;

    private Hospitalization insertedHospitalization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospitalization createEntity(EntityManager em) {
        Hospitalization hospitalization = new Hospitalization()
            .admissionDate(DEFAULT_ADMISSION_DATE)
            .dischargeDate(DEFAULT_DISCHARGE_DATE)
            .reason(DEFAULT_REASON);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        hospitalization.setPatient(patient);
        return hospitalization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hospitalization createUpdatedEntity(EntityManager em) {
        Hospitalization updatedHospitalization = new Hospitalization()
            .admissionDate(UPDATED_ADMISSION_DATE)
            .dischargeDate(UPDATED_DISCHARGE_DATE)
            .reason(UPDATED_REASON);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedHospitalization.setPatient(patient);
        return updatedHospitalization;
    }

    @BeforeEach
    public void initTest() {
        hospitalization = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHospitalization != null) {
            hospitalizationRepository.delete(insertedHospitalization);
            insertedHospitalization = null;
        }
    }

    @Test
    @Transactional
    void createHospitalization() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Hospitalization
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);
        var returnedHospitalizationDTO = om.readValue(
            restHospitalizationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hospitalizationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HospitalizationDTO.class
        );

        // Validate the Hospitalization in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHospitalization = hospitalizationMapper.toEntity(returnedHospitalizationDTO);
        assertHospitalizationUpdatableFieldsEquals(returnedHospitalization, getPersistedHospitalization(returnedHospitalization));

        insertedHospitalization = returnedHospitalization;
    }

    @Test
    @Transactional
    void createHospitalizationWithExistingId() throws Exception {
        // Create the Hospitalization with an existing ID
        hospitalization.setId(1L);
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHospitalizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hospitalizationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAdmissionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hospitalization.setAdmissionDate(null);

        // Create the Hospitalization, which fails.
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        restHospitalizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hospitalizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHospitalizations() throws Exception {
        // Initialize the database
        insertedHospitalization = hospitalizationRepository.saveAndFlush(hospitalization);

        // Get all the hospitalizationList
        restHospitalizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hospitalization.getId().intValue())))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(sameInstant(DEFAULT_ADMISSION_DATE))))
            .andExpect(jsonPath("$.[*].dischargeDate").value(hasItem(sameInstant(DEFAULT_DISCHARGE_DATE))))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHospitalizationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(hospitalizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHospitalizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(hospitalizationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHospitalizationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(hospitalizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHospitalizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(hospitalizationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHospitalization() throws Exception {
        // Initialize the database
        insertedHospitalization = hospitalizationRepository.saveAndFlush(hospitalization);

        // Get the hospitalization
        restHospitalizationMockMvc
            .perform(get(ENTITY_API_URL_ID, hospitalization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hospitalization.getId().intValue()))
            .andExpect(jsonPath("$.admissionDate").value(sameInstant(DEFAULT_ADMISSION_DATE)))
            .andExpect(jsonPath("$.dischargeDate").value(sameInstant(DEFAULT_DISCHARGE_DATE)))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHospitalization() throws Exception {
        // Get the hospitalization
        restHospitalizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHospitalization() throws Exception {
        // Initialize the database
        insertedHospitalization = hospitalizationRepository.saveAndFlush(hospitalization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hospitalization
        Hospitalization updatedHospitalization = hospitalizationRepository.findById(hospitalization.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHospitalization are not directly saved in db
        em.detach(updatedHospitalization);
        updatedHospitalization.admissionDate(UPDATED_ADMISSION_DATE).dischargeDate(UPDATED_DISCHARGE_DATE).reason(UPDATED_REASON);
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(updatedHospitalization);

        restHospitalizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospitalizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hospitalizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHospitalizationToMatchAllProperties(updatedHospitalization);
    }

    @Test
    @Transactional
    void putNonExistingHospitalization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hospitalization.setId(longCount.incrementAndGet());

        // Create the Hospitalization
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hospitalizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hospitalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHospitalization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hospitalization.setId(longCount.incrementAndGet());

        // Create the Hospitalization
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hospitalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHospitalization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hospitalization.setId(longCount.incrementAndGet());

        // Create the Hospitalization
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalizationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hospitalizationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHospitalizationWithPatch() throws Exception {
        // Initialize the database
        insertedHospitalization = hospitalizationRepository.saveAndFlush(hospitalization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hospitalization using partial update
        Hospitalization partialUpdatedHospitalization = new Hospitalization();
        partialUpdatedHospitalization.setId(hospitalization.getId());

        partialUpdatedHospitalization.dischargeDate(UPDATED_DISCHARGE_DATE);

        restHospitalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospitalization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHospitalization))
            )
            .andExpect(status().isOk());

        // Validate the Hospitalization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHospitalizationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHospitalization, hospitalization),
            getPersistedHospitalization(hospitalization)
        );
    }

    @Test
    @Transactional
    void fullUpdateHospitalizationWithPatch() throws Exception {
        // Initialize the database
        insertedHospitalization = hospitalizationRepository.saveAndFlush(hospitalization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hospitalization using partial update
        Hospitalization partialUpdatedHospitalization = new Hospitalization();
        partialUpdatedHospitalization.setId(hospitalization.getId());

        partialUpdatedHospitalization.admissionDate(UPDATED_ADMISSION_DATE).dischargeDate(UPDATED_DISCHARGE_DATE).reason(UPDATED_REASON);

        restHospitalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHospitalization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHospitalization))
            )
            .andExpect(status().isOk());

        // Validate the Hospitalization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHospitalizationUpdatableFieldsEquals(
            partialUpdatedHospitalization,
            getPersistedHospitalization(partialUpdatedHospitalization)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHospitalization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hospitalization.setId(longCount.incrementAndGet());

        // Create the Hospitalization
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHospitalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hospitalizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hospitalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHospitalization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hospitalization.setId(longCount.incrementAndGet());

        // Create the Hospitalization
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hospitalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHospitalization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hospitalization.setId(longCount.incrementAndGet());

        // Create the Hospitalization
        HospitalizationDTO hospitalizationDTO = hospitalizationMapper.toDto(hospitalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHospitalizationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hospitalizationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hospitalization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHospitalization() throws Exception {
        // Initialize the database
        insertedHospitalization = hospitalizationRepository.saveAndFlush(hospitalization);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hospitalization
        restHospitalizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, hospitalization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hospitalizationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Hospitalization getPersistedHospitalization(Hospitalization hospitalization) {
        return hospitalizationRepository.findById(hospitalization.getId()).orElseThrow();
    }

    protected void assertPersistedHospitalizationToMatchAllProperties(Hospitalization expectedHospitalization) {
        assertHospitalizationAllPropertiesEquals(expectedHospitalization, getPersistedHospitalization(expectedHospitalization));
    }

    protected void assertPersistedHospitalizationToMatchUpdatableProperties(Hospitalization expectedHospitalization) {
        assertHospitalizationAllUpdatablePropertiesEquals(expectedHospitalization, getPersistedHospitalization(expectedHospitalization));
    }
}
