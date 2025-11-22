package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.PatientAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ciatch.gdp.web.rest.TestUtil.sameInstant;
import static com.ciatch.gdp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.PatientStatus;
import com.ciatch.gdp.domain.enumeration.SmokingStatus;
import com.ciatch.gdp.repository.PatientRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.mapper.PatientMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final BloodType DEFAULT_BLOOD_TYPE = BloodType.A_POS;
    private static final BloodType UPDATED_BLOOD_TYPE = BloodType.A_NEG;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_1 = "+987207264500343";
    private static final String UPDATED_PHONE_1 = "+886730";

    private static final String DEFAULT_PHONE_2 = "258864665720134";
    private static final String UPDATED_PHONE_2 = "2065558";

    private static final String DEFAULT_NIF = "AAAAAAAAAA";
    private static final String UPDATED_NIF = "BBBBBBBBBB";

    private static final String DEFAULT_NINU = "AAAAAAAAAA";
    private static final String UPDATED_NINU = "BBBBBBBBBB";

    private static final String DEFAULT_MEDICAL_RECORD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MEDICAL_RECORD_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_HEIGHT_CM = 0;
    private static final Integer UPDATED_HEIGHT_CM = 1;

    private static final BigDecimal DEFAULT_WEIGHT_KG = new BigDecimal(0);
    private static final BigDecimal UPDATED_WEIGHT_KG = new BigDecimal(1);

    private static final String DEFAULT_PASSPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON_PHONE = "12621650753";
    private static final String UPDATED_CONTACT_PERSON_PHONE = "+273572959";

    private static final String DEFAULT_ANTECEDENTS = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDENTS = "BBBBBBBBBB";

    private static final String DEFAULT_ALLERGIES = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGIES = "BBBBBBBBBB";

    private static final String DEFAULT_CLINICAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_CLINICAL_NOTES = "BBBBBBBBBB";

    private static final SmokingStatus DEFAULT_SMOKING_STATUS = SmokingStatus.NEVER;
    private static final SmokingStatus UPDATED_SMOKING_STATUS = SmokingStatus.FORMER;

    private static final ZonedDateTime DEFAULT_GDPR_CONSENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_GDPR_CONSENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final PatientStatus DEFAULT_STATUS = PatientStatus.ACTIVE;
    private static final PatientStatus UPDATED_STATUS = PatientStatus.INACTIVE;

    private static final ZonedDateTime DEFAULT_DECEASED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DECEASED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INSURANCE_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATIENT_INSURANCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PATIENT_INSURANCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INSURANCE_POLICY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_POLICY_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_INSURANCE_COVERAGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_COVERAGE_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INSURANCE_VALID_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSURANCE_VALID_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_INSURANCE_VALID_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSURANCE_VALID_TO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    private Patient insertedPatient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .uid(DEFAULT_UID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .bloodType(DEFAULT_BLOOD_TYPE)
            .address(DEFAULT_ADDRESS)
            .phone1(DEFAULT_PHONE_1)
            .phone2(DEFAULT_PHONE_2)
            .nif(DEFAULT_NIF)
            .ninu(DEFAULT_NINU)
            .medicalRecordNumber(DEFAULT_MEDICAL_RECORD_NUMBER)
            .heightCm(DEFAULT_HEIGHT_CM)
            .weightKg(DEFAULT_WEIGHT_KG)
            .passportNumber(DEFAULT_PASSPORT_NUMBER)
            .contactPersonName(DEFAULT_CONTACT_PERSON_NAME)
            .contactPersonPhone(DEFAULT_CONTACT_PERSON_PHONE)
            .antecedents(DEFAULT_ANTECEDENTS)
            .allergies(DEFAULT_ALLERGIES)
            .clinicalNotes(DEFAULT_CLINICAL_NOTES)
            .smokingStatus(DEFAULT_SMOKING_STATUS)
            .gdprConsentDate(DEFAULT_GDPR_CONSENT_DATE)
            .status(DEFAULT_STATUS)
            .deceasedDate(DEFAULT_DECEASED_DATE)
            .insuranceCompanyName(DEFAULT_INSURANCE_COMPANY_NAME)
            .patientInsuranceId(DEFAULT_PATIENT_INSURANCE_ID)
            .insurancePolicyNumber(DEFAULT_INSURANCE_POLICY_NUMBER)
            .insuranceCoverageType(DEFAULT_INSURANCE_COVERAGE_TYPE)
            .insuranceValidFrom(DEFAULT_INSURANCE_VALID_FROM)
            .insuranceValidTo(DEFAULT_INSURANCE_VALID_TO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        patient.setUser(user);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient updatedPatient = new Patient()
            .uid(UPDATED_UID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .bloodType(UPDATED_BLOOD_TYPE)
            .address(UPDATED_ADDRESS)
            .phone1(UPDATED_PHONE_1)
            .phone2(UPDATED_PHONE_2)
            .nif(UPDATED_NIF)
            .ninu(UPDATED_NINU)
            .medicalRecordNumber(UPDATED_MEDICAL_RECORD_NUMBER)
            .heightCm(UPDATED_HEIGHT_CM)
            .weightKg(UPDATED_WEIGHT_KG)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .contactPersonName(UPDATED_CONTACT_PERSON_NAME)
            .contactPersonPhone(UPDATED_CONTACT_PERSON_PHONE)
            .antecedents(UPDATED_ANTECEDENTS)
            .allergies(UPDATED_ALLERGIES)
            .clinicalNotes(UPDATED_CLINICAL_NOTES)
            .smokingStatus(UPDATED_SMOKING_STATUS)
            .gdprConsentDate(UPDATED_GDPR_CONSENT_DATE)
            .status(UPDATED_STATUS)
            .deceasedDate(UPDATED_DECEASED_DATE)
            .insuranceCompanyName(UPDATED_INSURANCE_COMPANY_NAME)
            .patientInsuranceId(UPDATED_PATIENT_INSURANCE_ID)
            .insurancePolicyNumber(UPDATED_INSURANCE_POLICY_NUMBER)
            .insuranceCoverageType(UPDATED_INSURANCE_COVERAGE_TYPE)
            .insuranceValidFrom(UPDATED_INSURANCE_VALID_FROM)
            .insuranceValidTo(UPDATED_INSURANCE_VALID_TO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedPatient.setUser(user);
        return updatedPatient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPatient != null) {
            patientRepository.delete(insertedPatient);
            insertedPatient = null;
        }
    }

    @Test
    @Transactional
    void createPatient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);
        var returnedPatientDTO = om.readValue(
            restPatientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PatientDTO.class
        );

        // Validate the Patient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPatient = patientMapper.toEntity(returnedPatientDTO);
        assertPatientUpdatableFieldsEquals(returnedPatient, getPersistedPatient(returnedPatient));

        insertedPatient = returnedPatient;
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);
        PatientDTO patientDTO = patientMapper.toDto(patient);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setFirstName(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setLastName(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setBirthDate(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhone1IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setPhone1(null);

        // Create the Patient, which fails.
        PatientDTO patientDTO = patientMapper.toDto(patient);

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].bloodType").value(hasItem(DEFAULT_BLOOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone1").value(hasItem(DEFAULT_PHONE_1)))
            .andExpect(jsonPath("$.[*].phone2").value(hasItem(DEFAULT_PHONE_2)))
            .andExpect(jsonPath("$.[*].nif").value(hasItem(DEFAULT_NIF)))
            .andExpect(jsonPath("$.[*].ninu").value(hasItem(DEFAULT_NINU)))
            .andExpect(jsonPath("$.[*].medicalRecordNumber").value(hasItem(DEFAULT_MEDICAL_RECORD_NUMBER)))
            .andExpect(jsonPath("$.[*].heightCm").value(hasItem(DEFAULT_HEIGHT_CM)))
            .andExpect(jsonPath("$.[*].weightKg").value(hasItem(sameNumber(DEFAULT_WEIGHT_KG))))
            .andExpect(jsonPath("$.[*].passportNumber").value(hasItem(DEFAULT_PASSPORT_NUMBER)))
            .andExpect(jsonPath("$.[*].contactPersonName").value(hasItem(DEFAULT_CONTACT_PERSON_NAME)))
            .andExpect(jsonPath("$.[*].contactPersonPhone").value(hasItem(DEFAULT_CONTACT_PERSON_PHONE)))
            .andExpect(jsonPath("$.[*].antecedents").value(hasItem(DEFAULT_ANTECEDENTS.toString())))
            .andExpect(jsonPath("$.[*].allergies").value(hasItem(DEFAULT_ALLERGIES.toString())))
            .andExpect(jsonPath("$.[*].clinicalNotes").value(hasItem(DEFAULT_CLINICAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].smokingStatus").value(hasItem(DEFAULT_SMOKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].gdprConsentDate").value(hasItem(sameInstant(DEFAULT_GDPR_CONSENT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].deceasedDate").value(hasItem(sameInstant(DEFAULT_DECEASED_DATE))))
            .andExpect(jsonPath("$.[*].insuranceCompanyName").value(hasItem(DEFAULT_INSURANCE_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].patientInsuranceId").value(hasItem(DEFAULT_PATIENT_INSURANCE_ID)))
            .andExpect(jsonPath("$.[*].insurancePolicyNumber").value(hasItem(DEFAULT_INSURANCE_POLICY_NUMBER)))
            .andExpect(jsonPath("$.[*].insuranceCoverageType").value(hasItem(DEFAULT_INSURANCE_COVERAGE_TYPE)))
            .andExpect(jsonPath("$.[*].insuranceValidFrom").value(hasItem(DEFAULT_INSURANCE_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].insuranceValidTo").value(hasItem(DEFAULT_INSURANCE_VALID_TO.toString())));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.bloodType").value(DEFAULT_BLOOD_TYPE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone1").value(DEFAULT_PHONE_1))
            .andExpect(jsonPath("$.phone2").value(DEFAULT_PHONE_2))
            .andExpect(jsonPath("$.nif").value(DEFAULT_NIF))
            .andExpect(jsonPath("$.ninu").value(DEFAULT_NINU))
            .andExpect(jsonPath("$.medicalRecordNumber").value(DEFAULT_MEDICAL_RECORD_NUMBER))
            .andExpect(jsonPath("$.heightCm").value(DEFAULT_HEIGHT_CM))
            .andExpect(jsonPath("$.weightKg").value(sameNumber(DEFAULT_WEIGHT_KG)))
            .andExpect(jsonPath("$.passportNumber").value(DEFAULT_PASSPORT_NUMBER))
            .andExpect(jsonPath("$.contactPersonName").value(DEFAULT_CONTACT_PERSON_NAME))
            .andExpect(jsonPath("$.contactPersonPhone").value(DEFAULT_CONTACT_PERSON_PHONE))
            .andExpect(jsonPath("$.antecedents").value(DEFAULT_ANTECEDENTS.toString()))
            .andExpect(jsonPath("$.allergies").value(DEFAULT_ALLERGIES.toString()))
            .andExpect(jsonPath("$.clinicalNotes").value(DEFAULT_CLINICAL_NOTES.toString()))
            .andExpect(jsonPath("$.smokingStatus").value(DEFAULT_SMOKING_STATUS.toString()))
            .andExpect(jsonPath("$.gdprConsentDate").value(sameInstant(DEFAULT_GDPR_CONSENT_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.deceasedDate").value(sameInstant(DEFAULT_DECEASED_DATE)))
            .andExpect(jsonPath("$.insuranceCompanyName").value(DEFAULT_INSURANCE_COMPANY_NAME))
            .andExpect(jsonPath("$.patientInsuranceId").value(DEFAULT_PATIENT_INSURANCE_ID))
            .andExpect(jsonPath("$.insurancePolicyNumber").value(DEFAULT_INSURANCE_POLICY_NUMBER))
            .andExpect(jsonPath("$.insuranceCoverageType").value(DEFAULT_INSURANCE_COVERAGE_TYPE))
            .andExpect(jsonPath("$.insuranceValidFrom").value(DEFAULT_INSURANCE_VALID_FROM.toString()))
            .andExpect(jsonPath("$.insuranceValidTo").value(DEFAULT_INSURANCE_VALID_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .uid(UPDATED_UID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .bloodType(UPDATED_BLOOD_TYPE)
            .address(UPDATED_ADDRESS)
            .phone1(UPDATED_PHONE_1)
            .phone2(UPDATED_PHONE_2)
            .nif(UPDATED_NIF)
            .ninu(UPDATED_NINU)
            .medicalRecordNumber(UPDATED_MEDICAL_RECORD_NUMBER)
            .heightCm(UPDATED_HEIGHT_CM)
            .weightKg(UPDATED_WEIGHT_KG)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .contactPersonName(UPDATED_CONTACT_PERSON_NAME)
            .contactPersonPhone(UPDATED_CONTACT_PERSON_PHONE)
            .antecedents(UPDATED_ANTECEDENTS)
            .allergies(UPDATED_ALLERGIES)
            .clinicalNotes(UPDATED_CLINICAL_NOTES)
            .smokingStatus(UPDATED_SMOKING_STATUS)
            .gdprConsentDate(UPDATED_GDPR_CONSENT_DATE)
            .status(UPDATED_STATUS)
            .deceasedDate(UPDATED_DECEASED_DATE)
            .insuranceCompanyName(UPDATED_INSURANCE_COMPANY_NAME)
            .patientInsuranceId(UPDATED_PATIENT_INSURANCE_ID)
            .insurancePolicyNumber(UPDATED_INSURANCE_POLICY_NUMBER)
            .insuranceCoverageType(UPDATED_INSURANCE_COVERAGE_TYPE)
            .insuranceValidFrom(UPDATED_INSURANCE_VALID_FROM)
            .insuranceValidTo(UPDATED_INSURANCE_VALID_TO);
        PatientDTO patientDTO = patientMapper.toDto(updatedPatient);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPatientToMatchAllProperties(updatedPatient);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .address(UPDATED_ADDRESS)
            .phone2(UPDATED_PHONE_2)
            .nif(UPDATED_NIF)
            .medicalRecordNumber(UPDATED_MEDICAL_RECORD_NUMBER)
            .weightKg(UPDATED_WEIGHT_KG)
            .contactPersonPhone(UPDATED_CONTACT_PERSON_PHONE)
            .antecedents(UPDATED_ANTECEDENTS)
            .clinicalNotes(UPDATED_CLINICAL_NOTES)
            .deceasedDate(UPDATED_DECEASED_DATE)
            .insuranceCompanyName(UPDATED_INSURANCE_COMPANY_NAME)
            .insuranceValidTo(UPDATED_INSURANCE_VALID_TO);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPatient, patient), getPersistedPatient(patient));
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .uid(UPDATED_UID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .bloodType(UPDATED_BLOOD_TYPE)
            .address(UPDATED_ADDRESS)
            .phone1(UPDATED_PHONE_1)
            .phone2(UPDATED_PHONE_2)
            .nif(UPDATED_NIF)
            .ninu(UPDATED_NINU)
            .medicalRecordNumber(UPDATED_MEDICAL_RECORD_NUMBER)
            .heightCm(UPDATED_HEIGHT_CM)
            .weightKg(UPDATED_WEIGHT_KG)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .contactPersonName(UPDATED_CONTACT_PERSON_NAME)
            .contactPersonPhone(UPDATED_CONTACT_PERSON_PHONE)
            .antecedents(UPDATED_ANTECEDENTS)
            .allergies(UPDATED_ALLERGIES)
            .clinicalNotes(UPDATED_CLINICAL_NOTES)
            .smokingStatus(UPDATED_SMOKING_STATUS)
            .gdprConsentDate(UPDATED_GDPR_CONSENT_DATE)
            .status(UPDATED_STATUS)
            .deceasedDate(UPDATED_DECEASED_DATE)
            .insuranceCompanyName(UPDATED_INSURANCE_COMPANY_NAME)
            .patientInsuranceId(UPDATED_PATIENT_INSURANCE_ID)
            .insurancePolicyNumber(UPDATED_INSURANCE_POLICY_NUMBER)
            .insuranceCoverageType(UPDATED_INSURANCE_COVERAGE_TYPE)
            .insuranceValidFrom(UPDATED_INSURANCE_VALID_FROM)
            .insuranceValidTo(UPDATED_INSURANCE_VALID_TO);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientUpdatableFieldsEquals(partialUpdatedPatient, getPersistedPatient(partialUpdatedPatient));
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // Create the Patient
        PatientDTO patientDTO = patientMapper.toDto(patient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(patientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return patientRepository.count();
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

    protected Patient getPersistedPatient(Patient patient) {
        return patientRepository.findById(patient.getId()).orElseThrow();
    }

    protected void assertPersistedPatientToMatchAllProperties(Patient expectedPatient) {
        assertPatientAllPropertiesEquals(expectedPatient, getPersistedPatient(expectedPatient));
    }

    protected void assertPersistedPatientToMatchUpdatableProperties(Patient expectedPatient) {
        assertPatientAllUpdatablePropertiesEquals(expectedPatient, getPersistedPatient(expectedPatient));
    }
}
