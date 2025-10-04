package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.MedicationAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ciatch.gdp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Medication;
import com.ciatch.gdp.domain.enumeration.PrescriptionStatus;
import com.ciatch.gdp.domain.enumeration.RiskLevel;
import com.ciatch.gdp.domain.enumeration.RouteAdmin;
import com.ciatch.gdp.repository.MedicationRepository;
import com.ciatch.gdp.service.dto.MedicationDTO;
import com.ciatch.gdp.service.mapper.MedicationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private static final String DEFAULT_INTERNATIONAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INTERNATIONAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_ATC = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ATC = "BBBBBBBBBB";

    private static final String DEFAULT_FORMULATION = "AAAAAAAAAA";
    private static final String UPDATED_FORMULATION = "BBBBBBBBBB";

    private static final String DEFAULT_STRENGTH = "AAAAAAAAAA";
    private static final String UPDATED_STRENGTH = "BBBBBBBBBB";

    private static final RouteAdmin DEFAULT_ROUTE_OF_ADMINISTRATION = RouteAdmin.ORAL;
    private static final RouteAdmin UPDATED_ROUTE_OF_ADMINISTRATION = RouteAdmin.INTRAVENOUS;

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final String DEFAULT_MARKETING_AUTHORIZATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MARKETING_AUTHORIZATION_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_MARKETING_AUTHORIZATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MARKETING_AUTHORIZATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PACKAGING = "AAAAAAAAAA";
    private static final String UPDATED_PACKAGING = "BBBBBBBBBB";

    private static final PrescriptionStatus DEFAULT_PRESCRIPTION_STATUS = PrescriptionStatus.PRESCRIPTION;
    private static final PrescriptionStatus UPDATED_PRESCRIPTION_STATUS = PrescriptionStatus.OTC;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_STORAGE_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_CONDITION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPOSITION = "AAAAAAAAAA";
    private static final String UPDATED_COMPOSITION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRAINDICATIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONTRAINDICATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_SIDE_EFFECTS = "AAAAAAAAAA";
    private static final String UPDATED_SIDE_EFFECTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_IS_GENERIC = false;
    private static final Boolean UPDATED_IS_GENERIC = true;

    private static final RiskLevel DEFAULT_RISK_LEVEL = RiskLevel.LOW;
    private static final RiskLevel UPDATED_RISK_LEVEL = RiskLevel.MODERATE;

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
        return new Medication()
            .name(DEFAULT_NAME)
            .internationalName(DEFAULT_INTERNATIONAL_NAME)
            .codeAtc(DEFAULT_CODE_ATC)
            .formulation(DEFAULT_FORMULATION)
            .strength(DEFAULT_STRENGTH)
            .routeOfAdministration(DEFAULT_ROUTE_OF_ADMINISTRATION)
            .manufacturer(DEFAULT_MANUFACTURER)
            .marketingAuthorizationNumber(DEFAULT_MARKETING_AUTHORIZATION_NUMBER)
            .marketingAuthorizationDate(DEFAULT_MARKETING_AUTHORIZATION_DATE)
            .packaging(DEFAULT_PACKAGING)
            .prescriptionStatus(DEFAULT_PRESCRIPTION_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .barcode(DEFAULT_BARCODE)
            .storageCondition(DEFAULT_STORAGE_CONDITION)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .image(DEFAULT_IMAGE)
            .composition(DEFAULT_COMPOSITION)
            .contraindications(DEFAULT_CONTRAINDICATIONS)
            .sideEffects(DEFAULT_SIDE_EFFECTS)
            .active(DEFAULT_ACTIVE)
            .isGeneric(DEFAULT_IS_GENERIC)
            .riskLevel(DEFAULT_RISK_LEVEL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medication createUpdatedEntity() {
        return new Medication()
            .name(UPDATED_NAME)
            .internationalName(UPDATED_INTERNATIONAL_NAME)
            .codeAtc(UPDATED_CODE_ATC)
            .formulation(UPDATED_FORMULATION)
            .strength(UPDATED_STRENGTH)
            .routeOfAdministration(UPDATED_ROUTE_OF_ADMINISTRATION)
            .manufacturer(UPDATED_MANUFACTURER)
            .marketingAuthorizationNumber(UPDATED_MARKETING_AUTHORIZATION_NUMBER)
            .marketingAuthorizationDate(UPDATED_MARKETING_AUTHORIZATION_DATE)
            .packaging(UPDATED_PACKAGING)
            .prescriptionStatus(UPDATED_PRESCRIPTION_STATUS)
            .description(UPDATED_DESCRIPTION)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .barcode(UPDATED_BARCODE)
            .storageCondition(UPDATED_STORAGE_CONDITION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .image(UPDATED_IMAGE)
            .composition(UPDATED_COMPOSITION)
            .contraindications(UPDATED_CONTRAINDICATIONS)
            .sideEffects(UPDATED_SIDE_EFFECTS)
            .active(UPDATED_ACTIVE)
            .isGeneric(UPDATED_IS_GENERIC)
            .riskLevel(UPDATED_RISK_LEVEL);
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
    void checkRouteOfAdministrationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medication.setRouteOfAdministration(null);

        // Create the Medication, which fails.
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        restMedicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrescriptionStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medication.setPrescriptionStatus(null);

        // Create the Medication, which fails.
        MedicationDTO medicationDTO = medicationMapper.toDto(medication);

        restMedicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medication.setActive(null);

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
            .andExpect(jsonPath("$.[*].internationalName").value(hasItem(DEFAULT_INTERNATIONAL_NAME)))
            .andExpect(jsonPath("$.[*].codeAtc").value(hasItem(DEFAULT_CODE_ATC)))
            .andExpect(jsonPath("$.[*].formulation").value(hasItem(DEFAULT_FORMULATION)))
            .andExpect(jsonPath("$.[*].strength").value(hasItem(DEFAULT_STRENGTH)))
            .andExpect(jsonPath("$.[*].routeOfAdministration").value(hasItem(DEFAULT_ROUTE_OF_ADMINISTRATION.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].marketingAuthorizationNumber").value(hasItem(DEFAULT_MARKETING_AUTHORIZATION_NUMBER)))
            .andExpect(jsonPath("$.[*].marketingAuthorizationDate").value(hasItem(DEFAULT_MARKETING_AUTHORIZATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].packaging").value(hasItem(DEFAULT_PACKAGING)))
            .andExpect(jsonPath("$.[*].prescriptionStatus").value(hasItem(DEFAULT_PRESCRIPTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].storageCondition").value(hasItem(DEFAULT_STORAGE_CONDITION)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].composition").value(hasItem(DEFAULT_COMPOSITION)))
            .andExpect(jsonPath("$.[*].contraindications").value(hasItem(DEFAULT_CONTRAINDICATIONS)))
            .andExpect(jsonPath("$.[*].sideEffects").value(hasItem(DEFAULT_SIDE_EFFECTS)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isGeneric").value(hasItem(DEFAULT_IS_GENERIC.booleanValue())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())));
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
            .andExpect(jsonPath("$.internationalName").value(DEFAULT_INTERNATIONAL_NAME))
            .andExpect(jsonPath("$.codeAtc").value(DEFAULT_CODE_ATC))
            .andExpect(jsonPath("$.formulation").value(DEFAULT_FORMULATION))
            .andExpect(jsonPath("$.strength").value(DEFAULT_STRENGTH))
            .andExpect(jsonPath("$.routeOfAdministration").value(DEFAULT_ROUTE_OF_ADMINISTRATION.toString()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER))
            .andExpect(jsonPath("$.marketingAuthorizationNumber").value(DEFAULT_MARKETING_AUTHORIZATION_NUMBER))
            .andExpect(jsonPath("$.marketingAuthorizationDate").value(DEFAULT_MARKETING_AUTHORIZATION_DATE.toString()))
            .andExpect(jsonPath("$.packaging").value(DEFAULT_PACKAGING))
            .andExpect(jsonPath("$.prescriptionStatus").value(DEFAULT_PRESCRIPTION_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.storageCondition").value(DEFAULT_STORAGE_CONDITION))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.composition").value(DEFAULT_COMPOSITION))
            .andExpect(jsonPath("$.contraindications").value(DEFAULT_CONTRAINDICATIONS))
            .andExpect(jsonPath("$.sideEffects").value(DEFAULT_SIDE_EFFECTS))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isGeneric").value(DEFAULT_IS_GENERIC.booleanValue()))
            .andExpect(jsonPath("$.riskLevel").value(DEFAULT_RISK_LEVEL.toString()));
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
        updatedMedication
            .name(UPDATED_NAME)
            .internationalName(UPDATED_INTERNATIONAL_NAME)
            .codeAtc(UPDATED_CODE_ATC)
            .formulation(UPDATED_FORMULATION)
            .strength(UPDATED_STRENGTH)
            .routeOfAdministration(UPDATED_ROUTE_OF_ADMINISTRATION)
            .manufacturer(UPDATED_MANUFACTURER)
            .marketingAuthorizationNumber(UPDATED_MARKETING_AUTHORIZATION_NUMBER)
            .marketingAuthorizationDate(UPDATED_MARKETING_AUTHORIZATION_DATE)
            .packaging(UPDATED_PACKAGING)
            .prescriptionStatus(UPDATED_PRESCRIPTION_STATUS)
            .description(UPDATED_DESCRIPTION)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .barcode(UPDATED_BARCODE)
            .storageCondition(UPDATED_STORAGE_CONDITION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .image(UPDATED_IMAGE)
            .composition(UPDATED_COMPOSITION)
            .contraindications(UPDATED_CONTRAINDICATIONS)
            .sideEffects(UPDATED_SIDE_EFFECTS)
            .active(UPDATED_ACTIVE)
            .isGeneric(UPDATED_IS_GENERIC)
            .riskLevel(UPDATED_RISK_LEVEL);
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

        partialUpdatedMedication
            .formulation(UPDATED_FORMULATION)
            .strength(UPDATED_STRENGTH)
            .manufacturer(UPDATED_MANUFACTURER)
            .marketingAuthorizationNumber(UPDATED_MARKETING_AUTHORIZATION_NUMBER)
            .marketingAuthorizationDate(UPDATED_MARKETING_AUTHORIZATION_DATE)
            .packaging(UPDATED_PACKAGING)
            .prescriptionStatus(UPDATED_PRESCRIPTION_STATUS)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .barcode(UPDATED_BARCODE)
            .unitPrice(UPDATED_UNIT_PRICE)
            .image(UPDATED_IMAGE)
            .composition(UPDATED_COMPOSITION)
            .active(UPDATED_ACTIVE)
            .riskLevel(UPDATED_RISK_LEVEL);

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

        partialUpdatedMedication
            .name(UPDATED_NAME)
            .internationalName(UPDATED_INTERNATIONAL_NAME)
            .codeAtc(UPDATED_CODE_ATC)
            .formulation(UPDATED_FORMULATION)
            .strength(UPDATED_STRENGTH)
            .routeOfAdministration(UPDATED_ROUTE_OF_ADMINISTRATION)
            .manufacturer(UPDATED_MANUFACTURER)
            .marketingAuthorizationNumber(UPDATED_MARKETING_AUTHORIZATION_NUMBER)
            .marketingAuthorizationDate(UPDATED_MARKETING_AUTHORIZATION_DATE)
            .packaging(UPDATED_PACKAGING)
            .prescriptionStatus(UPDATED_PRESCRIPTION_STATUS)
            .description(UPDATED_DESCRIPTION)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .barcode(UPDATED_BARCODE)
            .storageCondition(UPDATED_STORAGE_CONDITION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .image(UPDATED_IMAGE)
            .composition(UPDATED_COMPOSITION)
            .contraindications(UPDATED_CONTRAINDICATIONS)
            .sideEffects(UPDATED_SIDE_EFFECTS)
            .active(UPDATED_ACTIVE)
            .isGeneric(UPDATED_IS_GENERIC)
            .riskLevel(UPDATED_RISK_LEVEL);

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
