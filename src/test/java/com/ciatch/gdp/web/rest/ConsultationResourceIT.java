package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.ConsultationAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ciatch.gdp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Consultation;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.repository.ConsultationRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.service.ConsultationService;
import com.ciatch.gdp.service.dto.ConsultationDTO;
import com.ciatch.gdp.service.mapper.ConsultationMapper;
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
 * Integration tests for the {@link ConsultationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConsultationResourceIT {

    private static final ZonedDateTime DEFAULT_CONSULTATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONSULTATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SYMPTOMS = "AAAAAAAAAA";
    private static final String UPDATED_SYMPTOMS = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSIS = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consultations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ConsultationRepository consultationRepositoryMock;

    @Autowired
    private ConsultationMapper consultationMapper;

    @Mock
    private ConsultationService consultationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultationMockMvc;

    private Consultation consultation;

    private Consultation insertedConsultation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultation createEntity(EntityManager em) {
        Consultation consultation = new Consultation()
            .consultationDate(DEFAULT_CONSULTATION_DATE)
            .symptoms(DEFAULT_SYMPTOMS)
            .diagnosis(DEFAULT_DIAGNOSIS);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        consultation.setDoctor(user);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        consultation.setPatient(patient);
        return consultation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultation createUpdatedEntity(EntityManager em) {
        Consultation updatedConsultation = new Consultation()
            .consultationDate(UPDATED_CONSULTATION_DATE)
            .symptoms(UPDATED_SYMPTOMS)
            .diagnosis(UPDATED_DIAGNOSIS);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedConsultation.setDoctor(user);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedConsultation.setPatient(patient);
        return updatedConsultation;
    }

    @BeforeEach
    public void initTest() {
        consultation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedConsultation != null) {
            consultationRepository.delete(insertedConsultation);
            insertedConsultation = null;
        }
    }

    @Test
    @Transactional
    void createConsultation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);
        var returnedConsultationDTO = om.readValue(
            restConsultationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConsultationDTO.class
        );

        // Validate the Consultation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConsultation = consultationMapper.toEntity(returnedConsultationDTO);
        assertConsultationUpdatableFieldsEquals(returnedConsultation, getPersistedConsultation(returnedConsultation));

        insertedConsultation = returnedConsultation;
    }

    @Test
    @Transactional
    void createConsultationWithExistingId() throws Exception {
        // Create the Consultation with an existing ID
        consultation.setId(1L);
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConsultationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consultation.setConsultationDate(null);

        // Create the Consultation, which fails.
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        restConsultationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsultations() throws Exception {
        // Initialize the database
        insertedConsultation = consultationRepository.saveAndFlush(consultation);

        // Get all the consultationList
        restConsultationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultation.getId().intValue())))
            .andExpect(jsonPath("$.[*].consultationDate").value(hasItem(sameInstant(DEFAULT_CONSULTATION_DATE))))
            .andExpect(jsonPath("$.[*].symptoms").value(hasItem(DEFAULT_SYMPTOMS.toString())))
            .andExpect(jsonPath("$.[*].diagnosis").value(hasItem(DEFAULT_DIAGNOSIS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsultationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(consultationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsultationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(consultationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsultationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(consultationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsultationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(consultationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConsultation() throws Exception {
        // Initialize the database
        insertedConsultation = consultationRepository.saveAndFlush(consultation);

        // Get the consultation
        restConsultationMockMvc
            .perform(get(ENTITY_API_URL_ID, consultation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultation.getId().intValue()))
            .andExpect(jsonPath("$.consultationDate").value(sameInstant(DEFAULT_CONSULTATION_DATE)))
            .andExpect(jsonPath("$.symptoms").value(DEFAULT_SYMPTOMS.toString()))
            .andExpect(jsonPath("$.diagnosis").value(DEFAULT_DIAGNOSIS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingConsultation() throws Exception {
        // Get the consultation
        restConsultationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsultation() throws Exception {
        // Initialize the database
        insertedConsultation = consultationRepository.saveAndFlush(consultation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consultation
        Consultation updatedConsultation = consultationRepository.findById(consultation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsultation are not directly saved in db
        em.detach(updatedConsultation);
        updatedConsultation.consultationDate(UPDATED_CONSULTATION_DATE).symptoms(UPDATED_SYMPTOMS).diagnosis(UPDATED_DIAGNOSIS);
        ConsultationDTO consultationDTO = consultationMapper.toDto(updatedConsultation);

        restConsultationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsultationToMatchAllProperties(updatedConsultation);
    }

    @Test
    @Transactional
    void putNonExistingConsultation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultation.setId(longCount.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultation.setId(longCount.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultation.setId(longCount.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultationWithPatch() throws Exception {
        // Initialize the database
        insertedConsultation = consultationRepository.saveAndFlush(consultation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consultation using partial update
        Consultation partialUpdatedConsultation = new Consultation();
        partialUpdatedConsultation.setId(consultation.getId());

        partialUpdatedConsultation.symptoms(UPDATED_SYMPTOMS).diagnosis(UPDATED_DIAGNOSIS);

        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsultation))
            )
            .andExpect(status().isOk());

        // Validate the Consultation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsultationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConsultation, consultation),
            getPersistedConsultation(consultation)
        );
    }

    @Test
    @Transactional
    void fullUpdateConsultationWithPatch() throws Exception {
        // Initialize the database
        insertedConsultation = consultationRepository.saveAndFlush(consultation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consultation using partial update
        Consultation partialUpdatedConsultation = new Consultation();
        partialUpdatedConsultation.setId(consultation.getId());

        partialUpdatedConsultation.consultationDate(UPDATED_CONSULTATION_DATE).symptoms(UPDATED_SYMPTOMS).diagnosis(UPDATED_DIAGNOSIS);

        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsultation))
            )
            .andExpect(status().isOk());

        // Validate the Consultation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsultationUpdatableFieldsEquals(partialUpdatedConsultation, getPersistedConsultation(partialUpdatedConsultation));
    }

    @Test
    @Transactional
    void patchNonExistingConsultation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultation.setId(longCount.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultation.setId(longCount.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consultationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultation.setId(longCount.incrementAndGet());

        // Create the Consultation
        ConsultationDTO consultationDTO = consultationMapper.toDto(consultation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consultationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultation() throws Exception {
        // Initialize the database
        insertedConsultation = consultationRepository.saveAndFlush(consultation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consultation
        restConsultationMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consultationRepository.count();
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

    protected Consultation getPersistedConsultation(Consultation consultation) {
        return consultationRepository.findById(consultation.getId()).orElseThrow();
    }

    protected void assertPersistedConsultationToMatchAllProperties(Consultation expectedConsultation) {
        assertConsultationAllPropertiesEquals(expectedConsultation, getPersistedConsultation(expectedConsultation));
    }

    protected void assertPersistedConsultationToMatchUpdatableProperties(Consultation expectedConsultation) {
        assertConsultationAllUpdatablePropertiesEquals(expectedConsultation, getPersistedConsultation(expectedConsultation));
    }
}
