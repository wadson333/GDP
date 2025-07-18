package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.MedicationAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Medication;
import com.ciatch.gdp.repository.MedicationRepository;
import com.ciatch.gdp.service.dto.MedicationDTO;
import com.ciatch.gdp.service.mapper.MedicationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MedicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private MedicationMapper medicationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicationMockMvc;

    private Medication medication;

    private Medication insertedMedication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medication createEntity() {
        return new Medication().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medication createUpdatedEntity() {
        return new Medication().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        medication = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedication != null) {
            medicationRepository.delete(insertedMedication);
            insertedMedication = null;
        }
    }

    @Test
    @Transactional
    void createMedication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);
        var returnedMedicationDTO = om.readValue(
            restMedicationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicationDTO.class
        );

        // Validate the Medication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedication = medicationMapper.toEntity(returnedMedicationDTO);
        assertMedicationUpdatableFieldsEquals(returnedMedication, getPersistedMedication(returnedMedication));

        insertedMedication = returnedMedication;
    }

    @Test
    @Transactional
    void createMedicationWithExistingId() throws Exception {
        // Create the Medication with an existing ID
        medication.setId(1L);
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medication.setName(null);

        // Create the Medication, which fails.
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        restMedicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedications() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get all the medicationList
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getMedication() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        // Get the medication
        restMedicationMockMvc
            .perform(get(ENTITY_API_URL_ID, medication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medication.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedication() throws Exception {
        // Get the medication
        restMedicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedication() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medication
        Medication updatedMedication = medicationRepository.findById(medication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedication are not directly saved in db
        em.detach(updatedMedication);
        updatedMedication.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        MedicationDTO medicationDTO = medicationMapper.toDto(updatedMedication);

        restMedicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicationToMatchAllProperties(updatedMedication);
    }

    @Test
    @Transactional
    void putNonExistingMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicationWithPatch() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medication using partial update
        Medication partialUpdatedMedication = new Medication();
        partialUpdatedMedication.setId(medication.getId());

        partialUpdatedMedication.name(UPDATED_NAME);

        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedication))
            )
            .andExpect(status().isOk());

        // Validate the Medication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedication, medication),
            getPersistedMedication(medication)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicationWithPatch() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medication using partial update
        Medication partialUpdatedMedication = new Medication();
        partialUpdatedMedication.setId(medication.getId());

        partialUpdatedMedication.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedication))
            )
            .andExpect(status().isOk());

        // Validate the Medication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationUpdatableFieldsEquals(partialUpdatedMedication, getPersistedMedication(partialUpdatedMedication));
    }

    @Test
    @Transactional
    void patchNonExistingMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medication.setId(longCount.incrementAndGet());

        // Create the Medication
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedication() throws Exception {
        // Initialize the database
        insertedMedication = medicationRepository.saveAndFlush(medication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medication
        restMedicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, medication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicationRepository.count();
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

    protected Medication getPersistedMedication(Medication medication) {
        return medicationRepository.findById(medication.getId()).orElseThrow();
    }

    protected void assertPersistedMedicationToMatchAllProperties(Medication expectedMedication) {
        assertMedicationAllPropertiesEquals(expectedMedication, getPersistedMedication(expectedMedication));
    }

    protected void assertPersistedMedicationToMatchUpdatableProperties(Medication expectedMedication) {
        assertMedicationAllUpdatablePropertiesEquals(expectedMedication, getPersistedMedication(expectedMedication));
    }
}
