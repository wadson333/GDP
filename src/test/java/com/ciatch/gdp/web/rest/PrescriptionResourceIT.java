package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.PrescriptionAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Prescription;
import com.ciatch.gdp.repository.PrescriptionRepository;
import com.ciatch.gdp.service.dto.PrescriptionDTO;
import com.ciatch.gdp.service.mapper.PrescriptionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PrescriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrescriptionResourceIT {

    private static final LocalDate DEFAULT_PRESCRIPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRESCRIPTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/prescriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrescriptionMockMvc;

    private Prescription prescription;

    private Prescription insertedPrescription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescription createEntity() {
        return new Prescription().prescriptionDate(DEFAULT_PRESCRIPTION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescription createUpdatedEntity() {
        return new Prescription().prescriptionDate(UPDATED_PRESCRIPTION_DATE);
    }

    @BeforeEach
    public void initTest() {
        prescription = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPrescription != null) {
            prescriptionRepository.delete(insertedPrescription);
            insertedPrescription = null;
        }
    }

    @Test
    @Transactional
    void createPrescription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);
        var returnedPrescriptionDTO = om.readValue(
            restPrescriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrescriptionDTO.class
        );

        // Validate the Prescription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrescription = prescriptionMapper.toEntity(returnedPrescriptionDTO);
        assertPrescriptionUpdatableFieldsEquals(returnedPrescription, getPersistedPrescription(returnedPrescription));

        insertedPrescription = returnedPrescription;
    }

    @Test
    @Transactional
    void createPrescriptionWithExistingId() throws Exception {
        // Create the Prescription with an existing ID
        prescription.setId(1L);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrescriptionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prescription.setPrescriptionDate(null);

        // Create the Prescription, which fails.
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        restPrescriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrescriptions() throws Exception {
        // Initialize the database
        insertedPrescription = prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescriptionDate").value(hasItem(DEFAULT_PRESCRIPTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getPrescription() throws Exception {
        // Initialize the database
        insertedPrescription = prescriptionRepository.saveAndFlush(prescription);

        // Get the prescription
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prescription.getId().intValue()))
            .andExpect(jsonPath("$.prescriptionDate").value(DEFAULT_PRESCRIPTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPrescription() throws Exception {
        // Get the prescription
        restPrescriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrescription() throws Exception {
        // Initialize the database
        insertedPrescription = prescriptionRepository.saveAndFlush(prescription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prescription
        Prescription updatedPrescription = prescriptionRepository.findById(prescription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrescription are not directly saved in db
        em.detach(updatedPrescription);
        updatedPrescription.prescriptionDate(UPDATED_PRESCRIPTION_DATE);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(updatedPrescription);

        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prescriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrescriptionToMatchAllProperties(updatedPrescription);
    }

    @Test
    @Transactional
    void putNonExistingPrescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescription.setId(longCount.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescription.setId(longCount.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescription.setId(longCount.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedPrescription = prescriptionRepository.saveAndFlush(prescription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prescription using partial update
        Prescription partialUpdatedPrescription = new Prescription();
        partialUpdatedPrescription.setId(prescription.getId());

        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrescription))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrescriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrescription, prescription),
            getPersistedPrescription(prescription)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrescriptionWithPatch() throws Exception {
        // Initialize the database
        insertedPrescription = prescriptionRepository.saveAndFlush(prescription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prescription using partial update
        Prescription partialUpdatedPrescription = new Prescription();
        partialUpdatedPrescription.setId(prescription.getId());

        partialUpdatedPrescription.prescriptionDate(UPDATED_PRESCRIPTION_DATE);

        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrescription))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrescriptionUpdatableFieldsEquals(partialUpdatedPrescription, getPersistedPrescription(partialUpdatedPrescription));
    }

    @Test
    @Transactional
    void patchNonExistingPrescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescription.setId(longCount.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescription.setId(longCount.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrescription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescription.setId(longCount.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prescriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrescription() throws Exception {
        // Initialize the database
        insertedPrescription = prescriptionRepository.saveAndFlush(prescription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prescription
        restPrescriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prescription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prescriptionRepository.count();
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

    protected Prescription getPersistedPrescription(Prescription prescription) {
        return prescriptionRepository.findById(prescription.getId()).orElseThrow();
    }

    protected void assertPersistedPrescriptionToMatchAllProperties(Prescription expectedPrescription) {
        assertPrescriptionAllPropertiesEquals(expectedPrescription, getPersistedPrescription(expectedPrescription));
    }

    protected void assertPersistedPrescriptionToMatchUpdatableProperties(Prescription expectedPrescription) {
        assertPrescriptionAllUpdatablePropertiesEquals(expectedPrescription, getPersistedPrescription(expectedPrescription));
    }
}
