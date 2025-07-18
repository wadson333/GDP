package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.LabTestResultAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ciatch.gdp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.domain.LabTestResult;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.repository.LabTestResultRepository;
import com.ciatch.gdp.service.LabTestResultService;
import com.ciatch.gdp.service.dto.LabTestResultDTO;
import com.ciatch.gdp.service.mapper.LabTestResultMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link LabTestResultResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LabTestResultResourceIT {

    private static final BigDecimal DEFAULT_RESULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RESULT_VALUE = new BigDecimal(2);

    private static final LocalDate DEFAULT_RESULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESULT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ABNORMAL = false;
    private static final Boolean UPDATED_IS_ABNORMAL = true;

    private static final String ENTITY_API_URL = "/api/lab-test-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabTestResultRepository labTestResultRepository;

    @Mock
    private LabTestResultRepository labTestResultRepositoryMock;

    @Autowired
    private LabTestResultMapper labTestResultMapper;

    @Mock
    private LabTestResultService labTestResultServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLabTestResultMockMvc;

    private LabTestResult labTestResult;

    private LabTestResult insertedLabTestResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LabTestResult createEntity(EntityManager em) {
        LabTestResult labTestResult = new LabTestResult()
            .resultValue(DEFAULT_RESULT_VALUE)
            .resultDate(DEFAULT_RESULT_DATE)
            .isAbnormal(DEFAULT_IS_ABNORMAL);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        labTestResult.setPatient(patient);
        // Add required entity
        LabTestCatalog labTestCatalog;
        if (TestUtil.findAll(em, LabTestCatalog.class).isEmpty()) {
            labTestCatalog = LabTestCatalogResourceIT.createEntity();
            em.persist(labTestCatalog);
            em.flush();
        } else {
            labTestCatalog = TestUtil.findAll(em, LabTestCatalog.class).get(0);
        }
        labTestResult.setLabTest(labTestCatalog);
        return labTestResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LabTestResult createUpdatedEntity(EntityManager em) {
        LabTestResult updatedLabTestResult = new LabTestResult()
            .resultValue(UPDATED_RESULT_VALUE)
            .resultDate(UPDATED_RESULT_DATE)
            .isAbnormal(UPDATED_IS_ABNORMAL);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        updatedLabTestResult.setPatient(patient);
        // Add required entity
        LabTestCatalog labTestCatalog;
        if (TestUtil.findAll(em, LabTestCatalog.class).isEmpty()) {
            labTestCatalog = LabTestCatalogResourceIT.createUpdatedEntity();
            em.persist(labTestCatalog);
            em.flush();
        } else {
            labTestCatalog = TestUtil.findAll(em, LabTestCatalog.class).get(0);
        }
        updatedLabTestResult.setLabTest(labTestCatalog);
        return updatedLabTestResult;
    }

    @BeforeEach
    public void initTest() {
        labTestResult = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLabTestResult != null) {
            labTestResultRepository.delete(insertedLabTestResult);
            insertedLabTestResult = null;
        }
    }

    @Test
    @Transactional
    void createLabTestResult() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LabTestResult
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);
        var returnedLabTestResultDTO = om.readValue(
            restLabTestResultMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestResultDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LabTestResultDTO.class
        );

        // Validate the LabTestResult in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLabTestResult = labTestResultMapper.toEntity(returnedLabTestResultDTO);
        assertLabTestResultUpdatableFieldsEquals(returnedLabTestResult, getPersistedLabTestResult(returnedLabTestResult));

        insertedLabTestResult = returnedLabTestResult;
    }

    @Test
    @Transactional
    void createLabTestResultWithExistingId() throws Exception {
        // Create the LabTestResult with an existing ID
        labTestResult.setId(1L);
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLabTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkResultValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        labTestResult.setResultValue(null);

        // Create the LabTestResult, which fails.
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        restLabTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResultDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        labTestResult.setResultDate(null);

        // Create the LabTestResult, which fails.
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        restLabTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsAbnormalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        labTestResult.setIsAbnormal(null);

        // Create the LabTestResult, which fails.
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        restLabTestResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLabTestResults() throws Exception {
        // Initialize the database
        insertedLabTestResult = labTestResultRepository.saveAndFlush(labTestResult);

        // Get all the labTestResultList
        restLabTestResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(labTestResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].resultValue").value(hasItem(sameNumber(DEFAULT_RESULT_VALUE))))
            .andExpect(jsonPath("$.[*].resultDate").value(hasItem(DEFAULT_RESULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isAbnormal").value(hasItem(DEFAULT_IS_ABNORMAL.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLabTestResultsWithEagerRelationshipsIsEnabled() throws Exception {
        when(labTestResultServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLabTestResultMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(labTestResultServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLabTestResultsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(labTestResultServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLabTestResultMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(labTestResultRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLabTestResult() throws Exception {
        // Initialize the database
        insertedLabTestResult = labTestResultRepository.saveAndFlush(labTestResult);

        // Get the labTestResult
        restLabTestResultMockMvc
            .perform(get(ENTITY_API_URL_ID, labTestResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(labTestResult.getId().intValue()))
            .andExpect(jsonPath("$.resultValue").value(sameNumber(DEFAULT_RESULT_VALUE)))
            .andExpect(jsonPath("$.resultDate").value(DEFAULT_RESULT_DATE.toString()))
            .andExpect(jsonPath("$.isAbnormal").value(DEFAULT_IS_ABNORMAL.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLabTestResult() throws Exception {
        // Get the labTestResult
        restLabTestResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLabTestResult() throws Exception {
        // Initialize the database
        insertedLabTestResult = labTestResultRepository.saveAndFlush(labTestResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the labTestResult
        LabTestResult updatedLabTestResult = labTestResultRepository.findById(labTestResult.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLabTestResult are not directly saved in db
        em.detach(updatedLabTestResult);
        updatedLabTestResult.resultValue(UPDATED_RESULT_VALUE).resultDate(UPDATED_RESULT_DATE).isAbnormal(UPDATED_IS_ABNORMAL);
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(updatedLabTestResult);

        restLabTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, labTestResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(labTestResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLabTestResultToMatchAllProperties(updatedLabTestResult);
    }

    @Test
    @Transactional
    void putNonExistingLabTestResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestResult.setId(longCount.incrementAndGet());

        // Create the LabTestResult
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, labTestResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(labTestResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLabTestResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestResult.setId(longCount.incrementAndGet());

        // Create the LabTestResult
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(labTestResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLabTestResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestResult.setId(longCount.incrementAndGet());

        // Create the LabTestResult
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLabTestResultWithPatch() throws Exception {
        // Initialize the database
        insertedLabTestResult = labTestResultRepository.saveAndFlush(labTestResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the labTestResult using partial update
        LabTestResult partialUpdatedLabTestResult = new LabTestResult();
        partialUpdatedLabTestResult.setId(labTestResult.getId());

        partialUpdatedLabTestResult.isAbnormal(UPDATED_IS_ABNORMAL);

        restLabTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabTestResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLabTestResult))
            )
            .andExpect(status().isOk());

        // Validate the LabTestResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLabTestResultUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLabTestResult, labTestResult),
            getPersistedLabTestResult(labTestResult)
        );
    }

    @Test
    @Transactional
    void fullUpdateLabTestResultWithPatch() throws Exception {
        // Initialize the database
        insertedLabTestResult = labTestResultRepository.saveAndFlush(labTestResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the labTestResult using partial update
        LabTestResult partialUpdatedLabTestResult = new LabTestResult();
        partialUpdatedLabTestResult.setId(labTestResult.getId());

        partialUpdatedLabTestResult.resultValue(UPDATED_RESULT_VALUE).resultDate(UPDATED_RESULT_DATE).isAbnormal(UPDATED_IS_ABNORMAL);

        restLabTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabTestResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLabTestResult))
            )
            .andExpect(status().isOk());

        // Validate the LabTestResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLabTestResultUpdatableFieldsEquals(partialUpdatedLabTestResult, getPersistedLabTestResult(partialUpdatedLabTestResult));
    }

    @Test
    @Transactional
    void patchNonExistingLabTestResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestResult.setId(longCount.incrementAndGet());

        // Create the LabTestResult
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, labTestResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(labTestResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLabTestResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestResult.setId(longCount.incrementAndGet());

        // Create the LabTestResult
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(labTestResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLabTestResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestResult.setId(longCount.incrementAndGet());

        // Create the LabTestResult
        LabTestResultDTO labTestResultDTO = labTestResultMapper.toDto(labTestResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestResultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(labTestResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LabTestResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLabTestResult() throws Exception {
        // Initialize the database
        insertedLabTestResult = labTestResultRepository.saveAndFlush(labTestResult);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the labTestResult
        restLabTestResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, labTestResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return labTestResultRepository.count();
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

    protected LabTestResult getPersistedLabTestResult(LabTestResult labTestResult) {
        return labTestResultRepository.findById(labTestResult.getId()).orElseThrow();
    }

    protected void assertPersistedLabTestResultToMatchAllProperties(LabTestResult expectedLabTestResult) {
        assertLabTestResultAllPropertiesEquals(expectedLabTestResult, getPersistedLabTestResult(expectedLabTestResult));
    }

    protected void assertPersistedLabTestResultToMatchUpdatableProperties(LabTestResult expectedLabTestResult) {
        assertLabTestResultAllUpdatablePropertiesEquals(expectedLabTestResult, getPersistedLabTestResult(expectedLabTestResult));
    }
}
