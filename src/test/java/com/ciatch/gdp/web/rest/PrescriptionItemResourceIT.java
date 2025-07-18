package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.PrescriptionItemAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Medication;
import com.ciatch.gdp.domain.Prescription;
import com.ciatch.gdp.domain.PrescriptionItem;
import com.ciatch.gdp.repository.PrescriptionItemRepository;
import com.ciatch.gdp.service.PrescriptionItemService;
import com.ciatch.gdp.service.dto.PrescriptionItemDTO;
import com.ciatch.gdp.service.mapper.PrescriptionItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PrescriptionItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrescriptionItemResourceIT {

    private static final String DEFAULT_DOSAGE = "AAAAAAAAAA";
    private static final String UPDATED_DOSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prescription-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrescriptionItemRepository prescriptionItemRepository;

    @Mock
    private PrescriptionItemRepository prescriptionItemRepositoryMock;

    @Autowired
    private PrescriptionItemMapper prescriptionItemMapper;

    @Mock
    private PrescriptionItemService prescriptionItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrescriptionItemMockMvc;

    private PrescriptionItem prescriptionItem;

    private PrescriptionItem insertedPrescriptionItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrescriptionItem createEntity(EntityManager em) {
        PrescriptionItem prescriptionItem = new PrescriptionItem()
            .dosage(DEFAULT_DOSAGE)
            .frequency(DEFAULT_FREQUENCY)
            .duration(DEFAULT_DURATION);
        // Add required entity
        Medication medication;
        if (TestUtil.findAll(em, Medication.class).isEmpty()) {
            medication = MedicationResourceIT.createEntity();
            em.persist(medication);
            em.flush();
        } else {
            medication = TestUtil.findAll(em, Medication.class).get(0);
        }
        prescriptionItem.setMedication(medication);
        // Add required entity
        Prescription prescription;
        if (TestUtil.findAll(em, Prescription.class).isEmpty()) {
            prescription = PrescriptionResourceIT.createEntity();
            em.persist(prescription);
            em.flush();
        } else {
            prescription = TestUtil.findAll(em, Prescription.class).get(0);
        }
        prescriptionItem.setPrescription(prescription);
        return prescriptionItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrescriptionItem createUpdatedEntity(EntityManager em) {
        PrescriptionItem updatedPrescriptionItem = new PrescriptionItem()
            .dosage(UPDATED_DOSAGE)
            .frequency(UPDATED_FREQUENCY)
            .duration(UPDATED_DURATION);
        // Add required entity
        Medication medication;
        if (TestUtil.findAll(em, Medication.class).isEmpty()) {
            medication = MedicationResourceIT.createUpdatedEntity();
            em.persist(medication);
            em.flush();
        } else {
            medication = TestUtil.findAll(em, Medication.class).get(0);
        }
        updatedPrescriptionItem.setMedication(medication);
        // Add required entity
        Prescription prescription;
        if (TestUtil.findAll(em, Prescription.class).isEmpty()) {
            prescription = PrescriptionResourceIT.createUpdatedEntity();
            em.persist(prescription);
            em.flush();
        } else {
            prescription = TestUtil.findAll(em, Prescription.class).get(0);
        }
        updatedPrescriptionItem.setPrescription(prescription);
        return updatedPrescriptionItem;
    }

    @BeforeEach
    public void initTest() {
        prescriptionItem = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPrescriptionItem != null) {
            prescriptionItemRepository.delete(insertedPrescriptionItem);
            insertedPrescriptionItem = null;
        }
    }

    @Test
    @Transactional
    void createPrescriptionItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PrescriptionItem
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);
        var returnedPrescriptionItemDTO = om.readValue(
            restPrescriptionItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrescriptionItemDTO.class
        );

        // Validate the PrescriptionItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrescriptionItem = prescriptionItemMapper.toEntity(returnedPrescriptionItemDTO);
        assertPrescriptionItemUpdatableFieldsEquals(returnedPrescriptionItem, getPersistedPrescriptionItem(returnedPrescriptionItem));

        insertedPrescriptionItem = returnedPrescriptionItem;
    }

    @Test
    @Transactional
    void createPrescriptionItemWithExistingId() throws Exception {
        // Create the PrescriptionItem with an existing ID
        prescriptionItem.setId(1L);
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescriptionItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prescriptionItem.setFrequency(null);

        // Create the PrescriptionItem, which fails.
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        restPrescriptionItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrescriptionItems() throws Exception {
        // Initialize the database
        insertedPrescriptionItem = prescriptionItemRepository.saveAndFlush(prescriptionItem);

        // Get all the prescriptionItemList
        restPrescriptionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescriptionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].dosage").value(hasItem(DEFAULT_DOSAGE)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrescriptionItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prescriptionItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrescriptionItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prescriptionItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrescriptionItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prescriptionItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrescriptionItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prescriptionItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrescriptionItem() throws Exception {
        // Initialize the database
        insertedPrescriptionItem = prescriptionItemRepository.saveAndFlush(prescriptionItem);

        // Get the prescriptionItem
        restPrescriptionItemMockMvc
            .perform(get(ENTITY_API_URL_ID, prescriptionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prescriptionItem.getId().intValue()))
            .andExpect(jsonPath("$.dosage").value(DEFAULT_DOSAGE))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    void getNonExistingPrescriptionItem() throws Exception {
        // Get the prescriptionItem
        restPrescriptionItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrescriptionItem() throws Exception {
        // Initialize the database
        insertedPrescriptionItem = prescriptionItemRepository.saveAndFlush(prescriptionItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prescriptionItem
        PrescriptionItem updatedPrescriptionItem = prescriptionItemRepository.findById(prescriptionItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrescriptionItem are not directly saved in db
        em.detach(updatedPrescriptionItem);
        updatedPrescriptionItem.dosage(UPDATED_DOSAGE).frequency(UPDATED_FREQUENCY).duration(UPDATED_DURATION);
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(updatedPrescriptionItem);

        restPrescriptionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prescriptionItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrescriptionItemToMatchAllProperties(updatedPrescriptionItem);
    }

    @Test
    @Transactional
    void putNonExistingPrescriptionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescriptionItem.setId(longCount.incrementAndGet());

        // Create the PrescriptionItem
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prescriptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrescriptionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescriptionItem.setId(longCount.incrementAndGet());

        // Create the PrescriptionItem
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prescriptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrescriptionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescriptionItem.setId(longCount.incrementAndGet());

        // Create the PrescriptionItem
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prescriptionItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrescriptionItemWithPatch() throws Exception {
        // Initialize the database
        insertedPrescriptionItem = prescriptionItemRepository.saveAndFlush(prescriptionItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prescriptionItem using partial update
        PrescriptionItem partialUpdatedPrescriptionItem = new PrescriptionItem();
        partialUpdatedPrescriptionItem.setId(prescriptionItem.getId());

        partialUpdatedPrescriptionItem.frequency(UPDATED_FREQUENCY).duration(UPDATED_DURATION);

        restPrescriptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescriptionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrescriptionItem))
            )
            .andExpect(status().isOk());

        // Validate the PrescriptionItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrescriptionItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrescriptionItem, prescriptionItem),
            getPersistedPrescriptionItem(prescriptionItem)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrescriptionItemWithPatch() throws Exception {
        // Initialize the database
        insertedPrescriptionItem = prescriptionItemRepository.saveAndFlush(prescriptionItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prescriptionItem using partial update
        PrescriptionItem partialUpdatedPrescriptionItem = new PrescriptionItem();
        partialUpdatedPrescriptionItem.setId(prescriptionItem.getId());

        partialUpdatedPrescriptionItem.dosage(UPDATED_DOSAGE).frequency(UPDATED_FREQUENCY).duration(UPDATED_DURATION);

        restPrescriptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescriptionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrescriptionItem))
            )
            .andExpect(status().isOk());

        // Validate the PrescriptionItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrescriptionItemUpdatableFieldsEquals(
            partialUpdatedPrescriptionItem,
            getPersistedPrescriptionItem(partialUpdatedPrescriptionItem)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPrescriptionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescriptionItem.setId(longCount.incrementAndGet());

        // Create the PrescriptionItem
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prescriptionItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prescriptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrescriptionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescriptionItem.setId(longCount.incrementAndGet());

        // Create the PrescriptionItem
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prescriptionItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrescriptionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prescriptionItem.setId(longCount.incrementAndGet());

        // Create the PrescriptionItem
        PrescriptionItemDTO prescriptionItemDTO = prescriptionItemMapper.toDto(prescriptionItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prescriptionItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrescriptionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrescriptionItem() throws Exception {
        // Initialize the database
        insertedPrescriptionItem = prescriptionItemRepository.saveAndFlush(prescriptionItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prescriptionItem
        restPrescriptionItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, prescriptionItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prescriptionItemRepository.count();
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

    protected PrescriptionItem getPersistedPrescriptionItem(PrescriptionItem prescriptionItem) {
        return prescriptionItemRepository.findById(prescriptionItem.getId()).orElseThrow();
    }

    protected void assertPersistedPrescriptionItemToMatchAllProperties(PrescriptionItem expectedPrescriptionItem) {
        assertPrescriptionItemAllPropertiesEquals(expectedPrescriptionItem, getPersistedPrescriptionItem(expectedPrescriptionItem));
    }

    protected void assertPersistedPrescriptionItemToMatchUpdatableProperties(PrescriptionItem expectedPrescriptionItem) {
        assertPrescriptionItemAllUpdatablePropertiesEquals(
            expectedPrescriptionItem,
            getPersistedPrescriptionItem(expectedPrescriptionItem)
        );
    }
}
