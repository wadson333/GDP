package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.MedicalDocumentAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.MedicalDocument;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.repository.MedicalDocumentRepository;
import com.ciatch.gdp.service.MedicalDocumentService;
import com.ciatch.gdp.service.dto.MedicalDocumentDTO;
import com.ciatch.gdp.service.mapper.MedicalDocumentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MedicalDocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicalDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOCUMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOCUMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medical-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalDocumentRepository medicalDocumentRepository;

    @Mock
    private MedicalDocumentRepository medicalDocumentRepositoryMock;

    @Autowired
    private MedicalDocumentMapper medicalDocumentMapper;

    @Mock
    private MedicalDocumentService medicalDocumentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalDocumentMockMvc;

    private MedicalDocument medicalDocument;

    private MedicalDocument insertedMedicalDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalDocument createEntity(EntityManager em) {
        MedicalDocument medicalDocument = new MedicalDocument()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .documentDate(DEFAULT_DOCUMENT_DATE)
            .filePath(DEFAULT_FILE_PATH)
            .fileType(DEFAULT_FILE_TYPE)
            .desc(DEFAULT_DESC);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        medicalDocument.setPatient(patient);
        return medicalDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalDocument createUpdatedEntity(EntityManager em) {
        MedicalDocument updatedMedicalDocument = new MedicalDocument()
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDate(UPDATED_DOCUMENT_DATE)
            .filePath(UPDATED_FILE_PATH)
            .fileType(UPDATED_FILE_TYPE)
            .desc(UPDATED_DESC);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedMedicalDocument.setPatient(patient);
        return updatedMedicalDocument;
    }

    @BeforeEach
    public void initTest() {
        medicalDocument = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicalDocument != null) {
            medicalDocumentRepository.delete(insertedMedicalDocument);
            insertedMedicalDocument = null;
        }
    }

    @Test
    @Transactional
    void createMedicalDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);
        var returnedMedicalDocumentDTO = om.readValue(
            restMedicalDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalDocumentDTO.class
        );

        // Validate the MedicalDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalDocument = medicalDocumentMapper.toEntity(returnedMedicalDocumentDTO);
        assertMedicalDocumentUpdatableFieldsEquals(returnedMedicalDocument, getPersistedMedicalDocument(returnedMedicalDocument));

        insertedMedicalDocument = returnedMedicalDocument;
    }

    @Test
    @Transactional
    void createMedicalDocumentWithExistingId() throws Exception {
        // Create the MedicalDocument with an existing ID
        medicalDocument.setId(1L);
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setDocumentName(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilePathIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setFilePath(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalDocument.setFileType(null);

        // Create the MedicalDocument, which fails.
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        restMedicalDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalDocuments() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        // Get all the medicalDocumentList
        restMedicalDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].documentDate").value(hasItem(DEFAULT_DOCUMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicalDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicalDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicalDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicalDocumentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicalDocument() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        // Get the medicalDocument
        restMedicalDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.documentDate").value(DEFAULT_DOCUMENT_DATE.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC));
    }

    @Test
    @Transactional
    void getNonExistingMedicalDocument() throws Exception {
        // Get the medicalDocument
        restMedicalDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalDocument() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument
        MedicalDocument updatedMedicalDocument = medicalDocumentRepository.findById(medicalDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalDocument are not directly saved in db
        em.detach(updatedMedicalDocument);
        updatedMedicalDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDate(UPDATED_DOCUMENT_DATE)
            .filePath(UPDATED_FILE_PATH)
            .fileType(UPDATED_FILE_TYPE)
            .desc(UPDATED_DESC);
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(updatedMedicalDocument);

        restMedicalDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalDocumentToMatchAllProperties(updatedMedicalDocument);
    }

    @Test
    @Transactional
    void putNonExistingMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument using partial update
        MedicalDocument partialUpdatedMedicalDocument = new MedicalDocument();
        partialUpdatedMedicalDocument.setId(medicalDocument.getId());

        partialUpdatedMedicalDocument.documentDate(UPDATED_DOCUMENT_DATE).filePath(UPDATED_FILE_PATH).fileType(UPDATED_FILE_TYPE);

        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalDocument))
            )
            .andExpect(status().isOk());

        // Validate the MedicalDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalDocument, medicalDocument),
            getPersistedMedicalDocument(medicalDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalDocument using partial update
        MedicalDocument partialUpdatedMedicalDocument = new MedicalDocument();
        partialUpdatedMedicalDocument.setId(medicalDocument.getId());

        partialUpdatedMedicalDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDate(UPDATED_DOCUMENT_DATE)
            .filePath(UPDATED_FILE_PATH)
            .fileType(UPDATED_FILE_TYPE)
            .desc(UPDATED_DESC);

        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalDocument))
            )
            .andExpect(status().isOk());

        // Validate the MedicalDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalDocumentUpdatableFieldsEquals(
            partialUpdatedMedicalDocument,
            getPersistedMedicalDocument(partialUpdatedMedicalDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalDocument.setId(longCount.incrementAndGet());

        // Create the MedicalDocument
        MedicalDocumentDTO medicalDocumentDTO = medicalDocumentMapper.toDto(medicalDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalDocument() throws Exception {
        // Initialize the database
        insertedMedicalDocument = medicalDocumentRepository.saveAndFlush(medicalDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalDocument
        restMedicalDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalDocumentRepository.count();
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

    protected MedicalDocument getPersistedMedicalDocument(MedicalDocument medicalDocument) {
        return medicalDocumentRepository.findById(medicalDocument.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalDocumentToMatchAllProperties(MedicalDocument expectedMedicalDocument) {
        assertMedicalDocumentAllPropertiesEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
    }

    protected void assertPersistedMedicalDocumentToMatchUpdatableProperties(MedicalDocument expectedMedicalDocument) {
        assertMedicalDocumentAllUpdatablePropertiesEquals(expectedMedicalDocument, getPersistedMedicalDocument(expectedMedicalDocument));
    }
}
