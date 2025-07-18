package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.UserConfigurationAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.UserConfiguration;
import com.ciatch.gdp.repository.UserConfigurationRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.service.UserConfigurationService;
import com.ciatch.gdp.service.dto.UserConfigurationDTO;
import com.ciatch.gdp.service.mapper.UserConfigurationMapper;
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
 * Integration tests for the {@link UserConfigurationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserConfigurationResourceIT {

    private static final Boolean DEFAULT_TWO_FACTOR_ENABLED = false;
    private static final Boolean UPDATED_TWO_FACTOR_ENABLED = true;

    private static final String DEFAULT_TWO_FACTOR_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_TWO_FACTOR_SECRET = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECEIVE_EMAIL_NOTIFS = false;
    private static final Boolean UPDATED_RECEIVE_EMAIL_NOTIFS = true;

    private static final String ENTITY_API_URL = "/api/user-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserConfigurationRepository userConfigurationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserConfigurationRepository userConfigurationRepositoryMock;

    @Autowired
    private UserConfigurationMapper userConfigurationMapper;

    @Mock
    private UserConfigurationService userConfigurationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserConfigurationMockMvc;

    private UserConfiguration userConfiguration;

    private UserConfiguration insertedUserConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConfiguration createEntity(EntityManager em) {
        UserConfiguration userConfiguration = new UserConfiguration()
            .twoFactorEnabled(DEFAULT_TWO_FACTOR_ENABLED)
            .twoFactorSecret(DEFAULT_TWO_FACTOR_SECRET)
            .receiveEmailNotifs(DEFAULT_RECEIVE_EMAIL_NOTIFS);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        userConfiguration.setUser(user);
        return userConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConfiguration createUpdatedEntity(EntityManager em) {
        UserConfiguration updatedUserConfiguration = new UserConfiguration()
            .twoFactorEnabled(UPDATED_TWO_FACTOR_ENABLED)
            .twoFactorSecret(UPDATED_TWO_FACTOR_SECRET)
            .receiveEmailNotifs(UPDATED_RECEIVE_EMAIL_NOTIFS);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedUserConfiguration.setUser(user);
        return updatedUserConfiguration;
    }

    @BeforeEach
    public void initTest() {
        userConfiguration = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserConfiguration != null) {
            userConfigurationRepository.delete(insertedUserConfiguration);
            insertedUserConfiguration = null;
        }
    }

    @Test
    @Transactional
    void createUserConfiguration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserConfiguration
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);
        var returnedUserConfigurationDTO = om.readValue(
            restUserConfigurationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigurationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserConfigurationDTO.class
        );

        // Validate the UserConfiguration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserConfiguration = userConfigurationMapper.toEntity(returnedUserConfigurationDTO);
        assertUserConfigurationUpdatableFieldsEquals(returnedUserConfiguration, getPersistedUserConfiguration(returnedUserConfiguration));

        insertedUserConfiguration = returnedUserConfiguration;
    }

    @Test
    @Transactional
    void createUserConfigurationWithExistingId() throws Exception {
        // Create the UserConfiguration with an existing ID
        userConfiguration.setId(1L);
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTwoFactorEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userConfiguration.setTwoFactorEnabled(null);

        // Create the UserConfiguration, which fails.
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        restUserConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceiveEmailNotifsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userConfiguration.setReceiveEmailNotifs(null);

        // Create the UserConfiguration, which fails.
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        restUserConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserConfigurations() throws Exception {
        // Initialize the database
        insertedUserConfiguration = userConfigurationRepository.saveAndFlush(userConfiguration);

        // Get all the userConfigurationList
        restUserConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].twoFactorEnabled").value(hasItem(DEFAULT_TWO_FACTOR_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].twoFactorSecret").value(hasItem(DEFAULT_TWO_FACTOR_SECRET)))
            .andExpect(jsonPath("$.[*].receiveEmailNotifs").value(hasItem(DEFAULT_RECEIVE_EMAIL_NOTIFS.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserConfigurationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userConfigurationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserConfigurationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userConfigurationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserConfiguration() throws Exception {
        // Initialize the database
        insertedUserConfiguration = userConfigurationRepository.saveAndFlush(userConfiguration);

        // Get the userConfiguration
        restUserConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, userConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.twoFactorEnabled").value(DEFAULT_TWO_FACTOR_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.twoFactorSecret").value(DEFAULT_TWO_FACTOR_SECRET))
            .andExpect(jsonPath("$.receiveEmailNotifs").value(DEFAULT_RECEIVE_EMAIL_NOTIFS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserConfiguration() throws Exception {
        // Get the userConfiguration
        restUserConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserConfiguration() throws Exception {
        // Initialize the database
        insertedUserConfiguration = userConfigurationRepository.saveAndFlush(userConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConfiguration
        UserConfiguration updatedUserConfiguration = userConfigurationRepository.findById(userConfiguration.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserConfiguration are not directly saved in db
        em.detach(updatedUserConfiguration);
        updatedUserConfiguration
            .twoFactorEnabled(UPDATED_TWO_FACTOR_ENABLED)
            .twoFactorSecret(UPDATED_TWO_FACTOR_SECRET)
            .receiveEmailNotifs(UPDATED_RECEIVE_EMAIL_NOTIFS);
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(updatedUserConfiguration);

        restUserConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConfigurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserConfigurationToMatchAllProperties(updatedUserConfiguration);
    }

    @Test
    @Transactional
    void putNonExistingUserConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfiguration.setId(longCount.incrementAndGet());

        // Create the UserConfiguration
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfiguration.setId(longCount.incrementAndGet());

        // Create the UserConfiguration
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfiguration.setId(longCount.incrementAndGet());

        // Create the UserConfiguration
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigurationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigurationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedUserConfiguration = userConfigurationRepository.saveAndFlush(userConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConfiguration using partial update
        UserConfiguration partialUpdatedUserConfiguration = new UserConfiguration();
        partialUpdatedUserConfiguration.setId(userConfiguration.getId());

        partialUpdatedUserConfiguration.receiveEmailNotifs(UPDATED_RECEIVE_EMAIL_NOTIFS);

        restUserConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the UserConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserConfigurationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserConfiguration, userConfiguration),
            getPersistedUserConfiguration(userConfiguration)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedUserConfiguration = userConfigurationRepository.saveAndFlush(userConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConfiguration using partial update
        UserConfiguration partialUpdatedUserConfiguration = new UserConfiguration();
        partialUpdatedUserConfiguration.setId(userConfiguration.getId());

        partialUpdatedUserConfiguration
            .twoFactorEnabled(UPDATED_TWO_FACTOR_ENABLED)
            .twoFactorSecret(UPDATED_TWO_FACTOR_SECRET)
            .receiveEmailNotifs(UPDATED_RECEIVE_EMAIL_NOTIFS);

        restUserConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the UserConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserConfigurationUpdatableFieldsEquals(
            partialUpdatedUserConfiguration,
            getPersistedUserConfiguration(partialUpdatedUserConfiguration)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfiguration.setId(longCount.incrementAndGet());

        // Create the UserConfiguration
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userConfigurationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfiguration.setId(longCount.incrementAndGet());

        // Create the UserConfiguration
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfiguration.setId(longCount.incrementAndGet());

        // Create the UserConfiguration
        UserConfigurationDTO userConfigurationDTO = userConfigurationMapper.toDto(userConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigurationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userConfigurationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserConfiguration() throws Exception {
        // Initialize the database
        insertedUserConfiguration = userConfigurationRepository.saveAndFlush(userConfiguration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userConfiguration
        restUserConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userConfigurationRepository.count();
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

    protected UserConfiguration getPersistedUserConfiguration(UserConfiguration userConfiguration) {
        return userConfigurationRepository.findById(userConfiguration.getId()).orElseThrow();
    }

    protected void assertPersistedUserConfigurationToMatchAllProperties(UserConfiguration expectedUserConfiguration) {
        assertUserConfigurationAllPropertiesEquals(expectedUserConfiguration, getPersistedUserConfiguration(expectedUserConfiguration));
    }

    protected void assertPersistedUserConfigurationToMatchUpdatableProperties(UserConfiguration expectedUserConfiguration) {
        assertUserConfigurationAllUpdatablePropertiesEquals(
            expectedUserConfiguration,
            getPersistedUserConfiguration(expectedUserConfiguration)
        );
    }
}
