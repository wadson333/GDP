package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.LabTestCatalogAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ciatch.gdp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.repository.LabTestCatalogRepository;
import com.ciatch.gdp.service.dto.LabTestCatalogDTO;
import com.ciatch.gdp.service.mapper.LabTestCatalogMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link LabTestCatalogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LabTestCatalogResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REFERENCE_RANGE_LOW = new BigDecimal(1);
    private static final BigDecimal UPDATED_REFERENCE_RANGE_LOW = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REFERENCE_RANGE_HIGH = new BigDecimal(1);
    private static final BigDecimal UPDATED_REFERENCE_RANGE_HIGH = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/lab-test-catalogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabTestCatalogRepository labTestCatalogRepository;

    @Autowired
    private LabTestCatalogMapper labTestCatalogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLabTestCatalogMockMvc;

    private LabTestCatalog labTestCatalog;

    private LabTestCatalog insertedLabTestCatalog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LabTestCatalog createEntity() {
        return new LabTestCatalog()
            .name(DEFAULT_NAME)
            .unit(DEFAULT_UNIT)
            .referenceRangeLow(DEFAULT_REFERENCE_RANGE_LOW)
            .referenceRangeHigh(DEFAULT_REFERENCE_RANGE_HIGH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LabTestCatalog createUpdatedEntity() {
        return new LabTestCatalog()
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .referenceRangeLow(UPDATED_REFERENCE_RANGE_LOW)
            .referenceRangeHigh(UPDATED_REFERENCE_RANGE_HIGH);
    }

    @BeforeEach
    public void initTest() {
        labTestCatalog = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLabTestCatalog != null) {
            labTestCatalogRepository.delete(insertedLabTestCatalog);
            insertedLabTestCatalog = null;
        }
    }

    @Test
    @Transactional
    void createLabTestCatalog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LabTestCatalog
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);
        var returnedLabTestCatalogDTO = om.readValue(
            restLabTestCatalogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestCatalogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LabTestCatalogDTO.class
        );

        // Validate the LabTestCatalog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLabTestCatalog = labTestCatalogMapper.toEntity(returnedLabTestCatalogDTO);
        assertLabTestCatalogUpdatableFieldsEquals(returnedLabTestCatalog, getPersistedLabTestCatalog(returnedLabTestCatalog));

        insertedLabTestCatalog = returnedLabTestCatalog;
    }

    @Test
    @Transactional
    void createLabTestCatalogWithExistingId() throws Exception {
        // Create the LabTestCatalog with an existing ID
        labTestCatalog.setId(1L);
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLabTestCatalogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestCatalogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        labTestCatalog.setName(null);

        // Create the LabTestCatalog, which fails.
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        restLabTestCatalogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestCatalogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        labTestCatalog.setUnit(null);

        // Create the LabTestCatalog, which fails.
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        restLabTestCatalogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestCatalogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLabTestCatalogs() throws Exception {
        // Initialize the database
        insertedLabTestCatalog = labTestCatalogRepository.saveAndFlush(labTestCatalog);

        // Get all the labTestCatalogList
        restLabTestCatalogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(labTestCatalog.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].referenceRangeLow").value(hasItem(sameNumber(DEFAULT_REFERENCE_RANGE_LOW))))
            .andExpect(jsonPath("$.[*].referenceRangeHigh").value(hasItem(sameNumber(DEFAULT_REFERENCE_RANGE_HIGH))));
    }

    @Test
    @Transactional
    void getLabTestCatalog() throws Exception {
        // Initialize the database
        insertedLabTestCatalog = labTestCatalogRepository.saveAndFlush(labTestCatalog);

        // Get the labTestCatalog
        restLabTestCatalogMockMvc
            .perform(get(ENTITY_API_URL_ID, labTestCatalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(labTestCatalog.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.referenceRangeLow").value(sameNumber(DEFAULT_REFERENCE_RANGE_LOW)))
            .andExpect(jsonPath("$.referenceRangeHigh").value(sameNumber(DEFAULT_REFERENCE_RANGE_HIGH)));
    }

    @Test
    @Transactional
    void getNonExistingLabTestCatalog() throws Exception {
        // Get the labTestCatalog
        restLabTestCatalogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLabTestCatalog() throws Exception {
        // Initialize the database
        insertedLabTestCatalog = labTestCatalogRepository.saveAndFlush(labTestCatalog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the labTestCatalog
        LabTestCatalog updatedLabTestCatalog = labTestCatalogRepository.findById(labTestCatalog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLabTestCatalog are not directly saved in db
        em.detach(updatedLabTestCatalog);
        updatedLabTestCatalog
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .referenceRangeLow(UPDATED_REFERENCE_RANGE_LOW)
            .referenceRangeHigh(UPDATED_REFERENCE_RANGE_HIGH);
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(updatedLabTestCatalog);

        restLabTestCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, labTestCatalogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(labTestCatalogDTO))
            )
            .andExpect(status().isOk());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLabTestCatalogToMatchAllProperties(updatedLabTestCatalog);
    }

    @Test
    @Transactional
    void putNonExistingLabTestCatalog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestCatalog.setId(longCount.incrementAndGet());

        // Create the LabTestCatalog
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabTestCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, labTestCatalogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(labTestCatalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLabTestCatalog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestCatalog.setId(longCount.incrementAndGet());

        // Create the LabTestCatalog
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestCatalogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(labTestCatalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLabTestCatalog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestCatalog.setId(longCount.incrementAndGet());

        // Create the LabTestCatalog
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestCatalogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(labTestCatalogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLabTestCatalogWithPatch() throws Exception {
        // Initialize the database
        insertedLabTestCatalog = labTestCatalogRepository.saveAndFlush(labTestCatalog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the labTestCatalog using partial update
        LabTestCatalog partialUpdatedLabTestCatalog = new LabTestCatalog();
        partialUpdatedLabTestCatalog.setId(labTestCatalog.getId());

        partialUpdatedLabTestCatalog.unit(UPDATED_UNIT).referenceRangeHigh(UPDATED_REFERENCE_RANGE_HIGH);

        restLabTestCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabTestCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLabTestCatalog))
            )
            .andExpect(status().isOk());

        // Validate the LabTestCatalog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLabTestCatalogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLabTestCatalog, labTestCatalog),
            getPersistedLabTestCatalog(labTestCatalog)
        );
    }

    @Test
    @Transactional
    void fullUpdateLabTestCatalogWithPatch() throws Exception {
        // Initialize the database
        insertedLabTestCatalog = labTestCatalogRepository.saveAndFlush(labTestCatalog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the labTestCatalog using partial update
        LabTestCatalog partialUpdatedLabTestCatalog = new LabTestCatalog();
        partialUpdatedLabTestCatalog.setId(labTestCatalog.getId());

        partialUpdatedLabTestCatalog
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .referenceRangeLow(UPDATED_REFERENCE_RANGE_LOW)
            .referenceRangeHigh(UPDATED_REFERENCE_RANGE_HIGH);

        restLabTestCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLabTestCatalog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLabTestCatalog))
            )
            .andExpect(status().isOk());

        // Validate the LabTestCatalog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLabTestCatalogUpdatableFieldsEquals(partialUpdatedLabTestCatalog, getPersistedLabTestCatalog(partialUpdatedLabTestCatalog));
    }

    @Test
    @Transactional
    void patchNonExistingLabTestCatalog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestCatalog.setId(longCount.incrementAndGet());

        // Create the LabTestCatalog
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLabTestCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, labTestCatalogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(labTestCatalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLabTestCatalog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestCatalog.setId(longCount.incrementAndGet());

        // Create the LabTestCatalog
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestCatalogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(labTestCatalogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLabTestCatalog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        labTestCatalog.setId(longCount.incrementAndGet());

        // Create the LabTestCatalog
        LabTestCatalogDTO labTestCatalogDTO = labTestCatalogMapper.toDto(labTestCatalog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLabTestCatalogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(labTestCatalogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LabTestCatalog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLabTestCatalog() throws Exception {
        // Initialize the database
        insertedLabTestCatalog = labTestCatalogRepository.saveAndFlush(labTestCatalog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the labTestCatalog
        restLabTestCatalogMockMvc
            .perform(delete(ENTITY_API_URL_ID, labTestCatalog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return labTestCatalogRepository.count();
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

    protected LabTestCatalog getPersistedLabTestCatalog(LabTestCatalog labTestCatalog) {
        return labTestCatalogRepository.findById(labTestCatalog.getId()).orElseThrow();
    }

    protected void assertPersistedLabTestCatalogToMatchAllProperties(LabTestCatalog expectedLabTestCatalog) {
        assertLabTestCatalogAllPropertiesEquals(expectedLabTestCatalog, getPersistedLabTestCatalog(expectedLabTestCatalog));
    }

    protected void assertPersistedLabTestCatalogToMatchUpdatableProperties(LabTestCatalog expectedLabTestCatalog) {
        assertLabTestCatalogAllUpdatablePropertiesEquals(expectedLabTestCatalog, getPersistedLabTestCatalog(expectedLabTestCatalog));
    }
}
