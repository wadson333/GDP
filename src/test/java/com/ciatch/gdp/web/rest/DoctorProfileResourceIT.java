package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.DoctorProfileAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.repository.DoctorProfileRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.service.DoctorProfileService;
import com.ciatch.gdp.service.dto.DoctorProfileDTO;
import com.ciatch.gdp.service.mapper.DoctorProfileMapper;
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
 * Integration tests for the {@link DoctorProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DoctorProfileResourceIT {

    private static final String DEFAULT_SPECIALTY = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALTY = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_UNIVERSITY = "AAAAAAAAAA";
    private static final String UPDATED_UNIVERSITY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE_OF_PRACTICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE_OF_PRACTICE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/doctor-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DoctorProfileRepository doctorProfileRepositoryMock;

    @Autowired
    private DoctorProfileMapper doctorProfileMapper;

    @Mock
    private DoctorProfileService doctorProfileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorProfileMockMvc;

    private DoctorProfile doctorProfile;

    private DoctorProfile insertedDoctorProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorProfile createEntity(EntityManager em) {
        DoctorProfile doctorProfile = new DoctorProfile()
            .specialty(DEFAULT_SPECIALTY)
            .licenseNumber(DEFAULT_LICENSE_NUMBER)
            .university(DEFAULT_UNIVERSITY)
            .startDateOfPractice(DEFAULT_START_DATE_OF_PRACTICE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        doctorProfile.setUser(user);
        return doctorProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorProfile createUpdatedEntity(EntityManager em) {
        DoctorProfile updatedDoctorProfile = new DoctorProfile()
            .specialty(UPDATED_SPECIALTY)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .university(UPDATED_UNIVERSITY)
            .startDateOfPractice(UPDATED_START_DATE_OF_PRACTICE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedDoctorProfile.setUser(user);
        return updatedDoctorProfile;
    }

    @BeforeEach
    public void initTest() {
        doctorProfile = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDoctorProfile != null) {
            doctorProfileRepository.delete(insertedDoctorProfile);
            insertedDoctorProfile = null;
        }
    }

    @Test
    @Transactional
    void createDoctorProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DoctorProfile
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);
        var returnedDoctorProfileDTO = om.readValue(
            restDoctorProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DoctorProfileDTO.class
        );

        // Validate the DoctorProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDoctorProfile = doctorProfileMapper.toEntity(returnedDoctorProfileDTO);
        assertDoctorProfileUpdatableFieldsEquals(returnedDoctorProfile, getPersistedDoctorProfile(returnedDoctorProfile));

        insertedDoctorProfile = returnedDoctorProfile;
    }

    @Test
    @Transactional
    void createDoctorProfileWithExistingId() throws Exception {
        // Create the DoctorProfile with an existing ID
        doctorProfile.setId(1L);
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSpecialtyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setSpecialty(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicenseNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setLicenseNumber(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateOfPracticeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setStartDateOfPractice(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoctorProfiles() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList
        restDoctorProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialty").value(hasItem(DEFAULT_SPECIALTY)))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].university").value(hasItem(DEFAULT_UNIVERSITY)))
            .andExpect(jsonPath("$.[*].startDateOfPractice").value(hasItem(DEFAULT_START_DATE_OF_PRACTICE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(doctorProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(doctorProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoctorProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(doctorProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoctorProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(doctorProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDoctorProfile() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get the doctorProfile
        restDoctorProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, doctorProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctorProfile.getId().intValue()))
            .andExpect(jsonPath("$.specialty").value(DEFAULT_SPECIALTY))
            .andExpect(jsonPath("$.licenseNumber").value(DEFAULT_LICENSE_NUMBER))
            .andExpect(jsonPath("$.university").value(DEFAULT_UNIVERSITY))
            .andExpect(jsonPath("$.startDateOfPractice").value(DEFAULT_START_DATE_OF_PRACTICE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDoctorProfile() throws Exception {
        // Get the doctorProfile
        restDoctorProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoctorProfile() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctorProfile
        DoctorProfile updatedDoctorProfile = doctorProfileRepository.findById(doctorProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDoctorProfile are not directly saved in db
        em.detach(updatedDoctorProfile);
        updatedDoctorProfile
            .specialty(UPDATED_SPECIALTY)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .university(UPDATED_UNIVERSITY)
            .startDateOfPractice(UPDATED_START_DATE_OF_PRACTICE);
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(updatedDoctorProfile);

        restDoctorProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(doctorProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDoctorProfileToMatchAllProperties(updatedDoctorProfile);
    }

    @Test
    @Transactional
    void putNonExistingDoctorProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctorProfile.setId(longCount.incrementAndGet());

        // Create the DoctorProfile
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctorProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(doctorProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoctorProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctorProfile.setId(longCount.incrementAndGet());

        // Create the DoctorProfile
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(doctorProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoctorProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctorProfile.setId(longCount.incrementAndGet());

        // Create the DoctorProfile
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoctorProfileWithPatch() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctorProfile using partial update
        DoctorProfile partialUpdatedDoctorProfile = new DoctorProfile();
        partialUpdatedDoctorProfile.setId(doctorProfile.getId());

        partialUpdatedDoctorProfile
            .specialty(UPDATED_SPECIALTY)
            .university(UPDATED_UNIVERSITY)
            .startDateOfPractice(UPDATED_START_DATE_OF_PRACTICE);

        restDoctorProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctorProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoctorProfile))
            )
            .andExpect(status().isOk());

        // Validate the DoctorProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoctorProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDoctorProfile, doctorProfile),
            getPersistedDoctorProfile(doctorProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateDoctorProfileWithPatch() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the doctorProfile using partial update
        DoctorProfile partialUpdatedDoctorProfile = new DoctorProfile();
        partialUpdatedDoctorProfile.setId(doctorProfile.getId());

        partialUpdatedDoctorProfile
            .specialty(UPDATED_SPECIALTY)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .university(UPDATED_UNIVERSITY)
            .startDateOfPractice(UPDATED_START_DATE_OF_PRACTICE);

        restDoctorProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctorProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDoctorProfile))
            )
            .andExpect(status().isOk());

        // Validate the DoctorProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDoctorProfileUpdatableFieldsEquals(partialUpdatedDoctorProfile, getPersistedDoctorProfile(partialUpdatedDoctorProfile));
    }

    @Test
    @Transactional
    void patchNonExistingDoctorProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctorProfile.setId(longCount.incrementAndGet());

        // Create the DoctorProfile
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doctorProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doctorProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoctorProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctorProfile.setId(longCount.incrementAndGet());

        // Create the DoctorProfile
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(doctorProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoctorProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        doctorProfile.setId(longCount.incrementAndGet());

        // Create the DoctorProfile
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctorProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DoctorProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoctorProfile() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the doctorProfile
        restDoctorProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctorProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return doctorProfileRepository.count();
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

    protected DoctorProfile getPersistedDoctorProfile(DoctorProfile doctorProfile) {
        return doctorProfileRepository.findById(doctorProfile.getId()).orElseThrow();
    }

    protected void assertPersistedDoctorProfileToMatchAllProperties(DoctorProfile expectedDoctorProfile) {
        assertDoctorProfileAllPropertiesEquals(expectedDoctorProfile, getPersistedDoctorProfile(expectedDoctorProfile));
    }

    protected void assertPersistedDoctorProfileToMatchUpdatableProperties(DoctorProfile expectedDoctorProfile) {
        assertDoctorProfileAllUpdatablePropertiesEquals(expectedDoctorProfile, getPersistedDoctorProfile(expectedDoctorProfile));
    }
}
