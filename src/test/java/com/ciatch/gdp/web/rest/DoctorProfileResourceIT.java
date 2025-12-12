package com.ciatch.gdp.web.rest;

import static com.ciatch.gdp.domain.DoctorProfileAsserts.*;
import static com.ciatch.gdp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.ciatch.gdp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.enumeration.BloodType;
import com.ciatch.gdp.domain.enumeration.DoctorStatus;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.MedicalSpecialty;
import com.ciatch.gdp.repository.DoctorProfileRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.service.DoctorProfileService;
import com.ciatch.gdp.service.dto.DoctorProfileDTO;
import com.ciatch.gdp.service.mapper.DoctorProfileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
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

    private static final String DEFAULT_CODE_CLINIC = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CLINIC = "BBBBBBBBBB";

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_MEDICAL_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MEDICAL_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final BloodType DEFAULT_BLOOD_TYPE = BloodType.A_POS;
    private static final BloodType UPDATED_BLOOD_TYPE = BloodType.A_NEG;

    private static final MedicalSpecialty DEFAULT_PRIMARY_SPECIALTY = MedicalSpecialty.GENERAL_PRACTITIONER;
    private static final MedicalSpecialty UPDATED_PRIMARY_SPECIALTY = MedicalSpecialty.CARDIOLOGIST;

    private static final String DEFAULT_OTHER_SPECIALTIES = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_SPECIALTIES = "BBBBBBBBBB";

    private static final String DEFAULT_UNIVERSITY = "AAAAAAAAAA";
    private static final String UPDATED_UNIVERSITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_GRADUATION_YEAR = 1950;
    private static final Integer UPDATED_GRADUATION_YEAR = 1951;
    private static final Integer SMALLER_GRADUATION_YEAR = 1950 - 1;

    private static final LocalDate DEFAULT_START_DATE_OF_PRACTICE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE_OF_PRACTICE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE_OF_PRACTICE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_CONSULTATION_DURATION_MINUTES = 5;
    private static final Integer UPDATED_CONSULTATION_DURATION_MINUTES = 6;
    private static final Integer SMALLER_CONSULTATION_DURATION_MINUTES = 5 - 1;

    private static final Boolean DEFAULT_ACCEPTING_NEW_PATIENTS = false;
    private static final Boolean UPDATED_ACCEPTING_NEW_PATIENTS = true;

    private static final Boolean DEFAULT_ALLOWS_TELECONSULTATION = false;
    private static final Boolean UPDATED_ALLOWS_TELECONSULTATION = true;

    private static final BigDecimal DEFAULT_CONSULTATION_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_CONSULTATION_FEE = new BigDecimal(1);
    private static final BigDecimal SMALLER_CONSULTATION_FEE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TELECONSULTATION_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TELECONSULTATION_FEE = new BigDecimal(1);
    private static final BigDecimal SMALLER_TELECONSULTATION_FEE = new BigDecimal(0 - 1);

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_SPOKEN_LANGUAGES = "AAAAAAAAAA";
    private static final String UPDATED_SPOKEN_LANGUAGES = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE_URL = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_PHONE = "27357";
    private static final String UPDATED_OFFICE_PHONE = "+280583508";

    private static final String DEFAULT_OFFICE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_ADDRESS = "BBBBBBBBBB";

    private static final DoctorStatus DEFAULT_STATUS = DoctorStatus.PENDING_APPROVAL;
    private static final DoctorStatus UPDATED_STATUS = DoctorStatus.ACTIVE;

    private static final Boolean DEFAULT_IS_VERIFIED = false;
    private static final Boolean UPDATED_IS_VERIFIED = true;

    private static final Instant DEFAULT_VERIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERIFIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NIF = "AAAAAAAAAA";
    private static final String UPDATED_NIF = "BBBBBBBBBB";

    private static final String DEFAULT_NINU = "AAAAAAAAAA";
    private static final String UPDATED_NINU = "BBBBBBBBBB";

    private static final Double DEFAULT_AVERAGE_RATING = 0D;
    private static final Double UPDATED_AVERAGE_RATING = 1D;
    private static final Double SMALLER_AVERAGE_RATING = 0D - 1D;

    private static final Integer DEFAULT_REVIEW_COUNT = 0;
    private static final Integer UPDATED_REVIEW_COUNT = 1;
    private static final Integer SMALLER_REVIEW_COUNT = 0 - 1;

    private static final Long DEFAULT_VERSION = 1L;
    private static final Long UPDATED_VERSION = 2L;
    private static final Long SMALLER_VERSION = 1L - 1L;

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
            .codeClinic(DEFAULT_CODE_CLINIC)
            .uid(DEFAULT_UID)
            .medicalLicenseNumber(DEFAULT_MEDICAL_LICENSE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .bloodType(DEFAULT_BLOOD_TYPE)
            .primarySpecialty(DEFAULT_PRIMARY_SPECIALTY)
            .otherSpecialties(DEFAULT_OTHER_SPECIALTIES)
            .university(DEFAULT_UNIVERSITY)
            .graduationYear(DEFAULT_GRADUATION_YEAR)
            .startDateOfPractice(DEFAULT_START_DATE_OF_PRACTICE)
            .consultationDurationMinutes(DEFAULT_CONSULTATION_DURATION_MINUTES)
            .acceptingNewPatients(DEFAULT_ACCEPTING_NEW_PATIENTS)
            .allowsTeleconsultation(DEFAULT_ALLOWS_TELECONSULTATION)
            .consultationFee(DEFAULT_CONSULTATION_FEE)
            .teleconsultationFee(DEFAULT_TELECONSULTATION_FEE)
            .bio(DEFAULT_BIO)
            .spokenLanguages(DEFAULT_SPOKEN_LANGUAGES)
            .websiteUrl(DEFAULT_WEBSITE_URL)
            .officePhone(DEFAULT_OFFICE_PHONE)
            .officeAddress(DEFAULT_OFFICE_ADDRESS)
            .status(DEFAULT_STATUS)
            .isVerified(DEFAULT_IS_VERIFIED)
            .verifiedAt(DEFAULT_VERIFIED_AT)
            .nif(DEFAULT_NIF)
            .ninu(DEFAULT_NINU)
            .averageRating(DEFAULT_AVERAGE_RATING)
            .reviewCount(DEFAULT_REVIEW_COUNT)
            .version(DEFAULT_VERSION);
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
            .codeClinic(UPDATED_CODE_CLINIC)
            .uid(UPDATED_UID)
            .medicalLicenseNumber(UPDATED_MEDICAL_LICENSE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .bloodType(UPDATED_BLOOD_TYPE)
            .primarySpecialty(UPDATED_PRIMARY_SPECIALTY)
            .otherSpecialties(UPDATED_OTHER_SPECIALTIES)
            .university(UPDATED_UNIVERSITY)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .startDateOfPractice(UPDATED_START_DATE_OF_PRACTICE)
            .consultationDurationMinutes(UPDATED_CONSULTATION_DURATION_MINUTES)
            .acceptingNewPatients(UPDATED_ACCEPTING_NEW_PATIENTS)
            .allowsTeleconsultation(UPDATED_ALLOWS_TELECONSULTATION)
            .consultationFee(UPDATED_CONSULTATION_FEE)
            .teleconsultationFee(UPDATED_TELECONSULTATION_FEE)
            .bio(UPDATED_BIO)
            .spokenLanguages(UPDATED_SPOKEN_LANGUAGES)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .officePhone(UPDATED_OFFICE_PHONE)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .status(UPDATED_STATUS)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifiedAt(UPDATED_VERIFIED_AT)
            .nif(UPDATED_NIF)
            .ninu(UPDATED_NINU)
            .averageRating(UPDATED_AVERAGE_RATING)
            .reviewCount(UPDATED_REVIEW_COUNT)
            .version(UPDATED_VERSION);
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
    void checkUidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setUid(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMedicalLicenseNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setMedicalLicenseNumber(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setFirstName(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setLastName(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setBirthDate(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrimarySpecialtyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setPrimarySpecialty(null);

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
    void checkConsultationDurationMinutesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setConsultationDurationMinutes(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAcceptingNewPatientsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setAcceptingNewPatients(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllowsTeleconsultationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setAllowsTeleconsultation(null);

        // Create the DoctorProfile, which fails.
        DoctorProfileDTO doctorProfileDTO = doctorProfileMapper.toDto(doctorProfile);

        restDoctorProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(doctorProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        doctorProfile.setStatus(null);

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
            .andExpect(jsonPath("$.[*].codeClinic").value(hasItem(DEFAULT_CODE_CLINIC)))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].medicalLicenseNumber").value(hasItem(DEFAULT_MEDICAL_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].bloodType").value(hasItem(DEFAULT_BLOOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].primarySpecialty").value(hasItem(DEFAULT_PRIMARY_SPECIALTY.toString())))
            .andExpect(jsonPath("$.[*].otherSpecialties").value(hasItem(DEFAULT_OTHER_SPECIALTIES.toString())))
            .andExpect(jsonPath("$.[*].university").value(hasItem(DEFAULT_UNIVERSITY)))
            .andExpect(jsonPath("$.[*].graduationYear").value(hasItem(DEFAULT_GRADUATION_YEAR)))
            .andExpect(jsonPath("$.[*].startDateOfPractice").value(hasItem(DEFAULT_START_DATE_OF_PRACTICE.toString())))
            .andExpect(jsonPath("$.[*].consultationDurationMinutes").value(hasItem(DEFAULT_CONSULTATION_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].acceptingNewPatients").value(hasItem(DEFAULT_ACCEPTING_NEW_PATIENTS.booleanValue())))
            .andExpect(jsonPath("$.[*].allowsTeleconsultation").value(hasItem(DEFAULT_ALLOWS_TELECONSULTATION.booleanValue())))
            .andExpect(jsonPath("$.[*].consultationFee").value(hasItem(sameNumber(DEFAULT_CONSULTATION_FEE))))
            .andExpect(jsonPath("$.[*].teleconsultationFee").value(hasItem(sameNumber(DEFAULT_TELECONSULTATION_FEE))))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].spokenLanguages").value(hasItem(DEFAULT_SPOKEN_LANGUAGES)))
            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL)))
            .andExpect(jsonPath("$.[*].officePhone").value(hasItem(DEFAULT_OFFICE_PHONE)))
            .andExpect(jsonPath("$.[*].officeAddress").value(hasItem(DEFAULT_OFFICE_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].verifiedAt").value(hasItem(DEFAULT_VERIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].nif").value(hasItem(DEFAULT_NIF)))
            .andExpect(jsonPath("$.[*].ninu").value(hasItem(DEFAULT_NINU)))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].reviewCount").value(hasItem(DEFAULT_REVIEW_COUNT)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.intValue())));
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
            .andExpect(jsonPath("$.codeClinic").value(DEFAULT_CODE_CLINIC))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.medicalLicenseNumber").value(DEFAULT_MEDICAL_LICENSE_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.bloodType").value(DEFAULT_BLOOD_TYPE.toString()))
            .andExpect(jsonPath("$.primarySpecialty").value(DEFAULT_PRIMARY_SPECIALTY.toString()))
            .andExpect(jsonPath("$.otherSpecialties").value(DEFAULT_OTHER_SPECIALTIES.toString()))
            .andExpect(jsonPath("$.university").value(DEFAULT_UNIVERSITY))
            .andExpect(jsonPath("$.graduationYear").value(DEFAULT_GRADUATION_YEAR))
            .andExpect(jsonPath("$.startDateOfPractice").value(DEFAULT_START_DATE_OF_PRACTICE.toString()))
            .andExpect(jsonPath("$.consultationDurationMinutes").value(DEFAULT_CONSULTATION_DURATION_MINUTES))
            .andExpect(jsonPath("$.acceptingNewPatients").value(DEFAULT_ACCEPTING_NEW_PATIENTS.booleanValue()))
            .andExpect(jsonPath("$.allowsTeleconsultation").value(DEFAULT_ALLOWS_TELECONSULTATION.booleanValue()))
            .andExpect(jsonPath("$.consultationFee").value(sameNumber(DEFAULT_CONSULTATION_FEE)))
            .andExpect(jsonPath("$.teleconsultationFee").value(sameNumber(DEFAULT_TELECONSULTATION_FEE)))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.spokenLanguages").value(DEFAULT_SPOKEN_LANGUAGES))
            .andExpect(jsonPath("$.websiteUrl").value(DEFAULT_WEBSITE_URL))
            .andExpect(jsonPath("$.officePhone").value(DEFAULT_OFFICE_PHONE))
            .andExpect(jsonPath("$.officeAddress").value(DEFAULT_OFFICE_ADDRESS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isVerified").value(DEFAULT_IS_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.verifiedAt").value(DEFAULT_VERIFIED_AT.toString()))
            .andExpect(jsonPath("$.nif").value(DEFAULT_NIF))
            .andExpect(jsonPath("$.ninu").value(DEFAULT_NINU))
            .andExpect(jsonPath("$.averageRating").value(DEFAULT_AVERAGE_RATING.doubleValue()))
            .andExpect(jsonPath("$.reviewCount").value(DEFAULT_REVIEW_COUNT))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.intValue()));
    }

    @Test
    @Transactional
    void getDoctorProfilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        Long id = doctorProfile.getId();

        defaultDoctorProfileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDoctorProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDoctorProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByCodeClinicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where codeClinic equals to
        defaultDoctorProfileFiltering("codeClinic.equals=" + DEFAULT_CODE_CLINIC, "codeClinic.equals=" + UPDATED_CODE_CLINIC);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByCodeClinicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where codeClinic in
        defaultDoctorProfileFiltering(
            "codeClinic.in=" + DEFAULT_CODE_CLINIC + "," + UPDATED_CODE_CLINIC,
            "codeClinic.in=" + UPDATED_CODE_CLINIC
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByCodeClinicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where codeClinic is not null
        defaultDoctorProfileFiltering("codeClinic.specified=true", "codeClinic.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByCodeClinicContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where codeClinic contains
        defaultDoctorProfileFiltering("codeClinic.contains=" + DEFAULT_CODE_CLINIC, "codeClinic.contains=" + UPDATED_CODE_CLINIC);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByCodeClinicNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where codeClinic does not contain
        defaultDoctorProfileFiltering(
            "codeClinic.doesNotContain=" + UPDATED_CODE_CLINIC,
            "codeClinic.doesNotContain=" + DEFAULT_CODE_CLINIC
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where uid equals to
        defaultDoctorProfileFiltering("uid.equals=" + DEFAULT_UID, "uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUidIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where uid in
        defaultDoctorProfileFiltering("uid.in=" + DEFAULT_UID + "," + UPDATED_UID, "uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where uid is not null
        defaultDoctorProfileFiltering("uid.specified=true", "uid.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByMedicalLicenseNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where medicalLicenseNumber equals to
        defaultDoctorProfileFiltering(
            "medicalLicenseNumber.equals=" + DEFAULT_MEDICAL_LICENSE_NUMBER,
            "medicalLicenseNumber.equals=" + UPDATED_MEDICAL_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByMedicalLicenseNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where medicalLicenseNumber in
        defaultDoctorProfileFiltering(
            "medicalLicenseNumber.in=" + DEFAULT_MEDICAL_LICENSE_NUMBER + "," + UPDATED_MEDICAL_LICENSE_NUMBER,
            "medicalLicenseNumber.in=" + UPDATED_MEDICAL_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByMedicalLicenseNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where medicalLicenseNumber is not null
        defaultDoctorProfileFiltering("medicalLicenseNumber.specified=true", "medicalLicenseNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByMedicalLicenseNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where medicalLicenseNumber contains
        defaultDoctorProfileFiltering(
            "medicalLicenseNumber.contains=" + DEFAULT_MEDICAL_LICENSE_NUMBER,
            "medicalLicenseNumber.contains=" + UPDATED_MEDICAL_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByMedicalLicenseNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where medicalLicenseNumber does not contain
        defaultDoctorProfileFiltering(
            "medicalLicenseNumber.doesNotContain=" + UPDATED_MEDICAL_LICENSE_NUMBER,
            "medicalLicenseNumber.doesNotContain=" + DEFAULT_MEDICAL_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where firstName equals to
        defaultDoctorProfileFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where firstName in
        defaultDoctorProfileFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where firstName is not null
        defaultDoctorProfileFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where firstName contains
        defaultDoctorProfileFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where firstName does not contain
        defaultDoctorProfileFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where lastName equals to
        defaultDoctorProfileFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where lastName in
        defaultDoctorProfileFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where lastName is not null
        defaultDoctorProfileFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where lastName contains
        defaultDoctorProfileFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where lastName does not contain
        defaultDoctorProfileFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where birthDate equals to
        defaultDoctorProfileFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where birthDate in
        defaultDoctorProfileFiltering(
            "birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE,
            "birthDate.in=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where birthDate is not null
        defaultDoctorProfileFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where birthDate is greater than or equal to
        defaultDoctorProfileFiltering(
            "birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE,
            "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where birthDate is less than or equal to
        defaultDoctorProfileFiltering("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where birthDate is less than
        defaultDoctorProfileFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where birthDate is greater than
        defaultDoctorProfileFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where gender equals to
        defaultDoctorProfileFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where gender in
        defaultDoctorProfileFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where gender is not null
        defaultDoctorProfileFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBloodTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where bloodType equals to
        defaultDoctorProfileFiltering("bloodType.equals=" + DEFAULT_BLOOD_TYPE, "bloodType.equals=" + UPDATED_BLOOD_TYPE);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBloodTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where bloodType in
        defaultDoctorProfileFiltering(
            "bloodType.in=" + DEFAULT_BLOOD_TYPE + "," + UPDATED_BLOOD_TYPE,
            "bloodType.in=" + UPDATED_BLOOD_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByBloodTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where bloodType is not null
        defaultDoctorProfileFiltering("bloodType.specified=true", "bloodType.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByPrimarySpecialtyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where primarySpecialty equals to
        defaultDoctorProfileFiltering(
            "primarySpecialty.equals=" + DEFAULT_PRIMARY_SPECIALTY,
            "primarySpecialty.equals=" + UPDATED_PRIMARY_SPECIALTY
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByPrimarySpecialtyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where primarySpecialty in
        defaultDoctorProfileFiltering(
            "primarySpecialty.in=" + DEFAULT_PRIMARY_SPECIALTY + "," + UPDATED_PRIMARY_SPECIALTY,
            "primarySpecialty.in=" + UPDATED_PRIMARY_SPECIALTY
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByPrimarySpecialtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where primarySpecialty is not null
        defaultDoctorProfileFiltering("primarySpecialty.specified=true", "primarySpecialty.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUniversityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where university equals to
        defaultDoctorProfileFiltering("university.equals=" + DEFAULT_UNIVERSITY, "university.equals=" + UPDATED_UNIVERSITY);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUniversityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where university in
        defaultDoctorProfileFiltering(
            "university.in=" + DEFAULT_UNIVERSITY + "," + UPDATED_UNIVERSITY,
            "university.in=" + UPDATED_UNIVERSITY
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUniversityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where university is not null
        defaultDoctorProfileFiltering("university.specified=true", "university.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUniversityContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where university contains
        defaultDoctorProfileFiltering("university.contains=" + DEFAULT_UNIVERSITY, "university.contains=" + UPDATED_UNIVERSITY);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUniversityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where university does not contain
        defaultDoctorProfileFiltering("university.doesNotContain=" + UPDATED_UNIVERSITY, "university.doesNotContain=" + DEFAULT_UNIVERSITY);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGraduationYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where graduationYear equals to
        defaultDoctorProfileFiltering(
            "graduationYear.equals=" + DEFAULT_GRADUATION_YEAR,
            "graduationYear.equals=" + UPDATED_GRADUATION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGraduationYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where graduationYear in
        defaultDoctorProfileFiltering(
            "graduationYear.in=" + DEFAULT_GRADUATION_YEAR + "," + UPDATED_GRADUATION_YEAR,
            "graduationYear.in=" + UPDATED_GRADUATION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGraduationYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where graduationYear is not null
        defaultDoctorProfileFiltering("graduationYear.specified=true", "graduationYear.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGraduationYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where graduationYear is greater than or equal to
        defaultDoctorProfileFiltering(
            "graduationYear.greaterThanOrEqual=" + DEFAULT_GRADUATION_YEAR,
            "graduationYear.greaterThanOrEqual=" + (DEFAULT_GRADUATION_YEAR + 1)
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGraduationYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where graduationYear is less than or equal to
        defaultDoctorProfileFiltering(
            "graduationYear.lessThanOrEqual=" + DEFAULT_GRADUATION_YEAR,
            "graduationYear.lessThanOrEqual=" + SMALLER_GRADUATION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGraduationYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where graduationYear is less than
        defaultDoctorProfileFiltering(
            "graduationYear.lessThan=" + (DEFAULT_GRADUATION_YEAR + 1),
            "graduationYear.lessThan=" + DEFAULT_GRADUATION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByGraduationYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where graduationYear is greater than
        defaultDoctorProfileFiltering(
            "graduationYear.greaterThan=" + SMALLER_GRADUATION_YEAR,
            "graduationYear.greaterThan=" + DEFAULT_GRADUATION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStartDateOfPracticeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where startDateOfPractice equals to
        defaultDoctorProfileFiltering(
            "startDateOfPractice.equals=" + DEFAULT_START_DATE_OF_PRACTICE,
            "startDateOfPractice.equals=" + UPDATED_START_DATE_OF_PRACTICE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStartDateOfPracticeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where startDateOfPractice in
        defaultDoctorProfileFiltering(
            "startDateOfPractice.in=" + DEFAULT_START_DATE_OF_PRACTICE + "," + UPDATED_START_DATE_OF_PRACTICE,
            "startDateOfPractice.in=" + UPDATED_START_DATE_OF_PRACTICE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStartDateOfPracticeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where startDateOfPractice is not null
        defaultDoctorProfileFiltering("startDateOfPractice.specified=true", "startDateOfPractice.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStartDateOfPracticeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where startDateOfPractice is greater than or equal to
        defaultDoctorProfileFiltering(
            "startDateOfPractice.greaterThanOrEqual=" + DEFAULT_START_DATE_OF_PRACTICE,
            "startDateOfPractice.greaterThanOrEqual=" + UPDATED_START_DATE_OF_PRACTICE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStartDateOfPracticeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where startDateOfPractice is less than or equal to
        defaultDoctorProfileFiltering(
            "startDateOfPractice.lessThanOrEqual=" + DEFAULT_START_DATE_OF_PRACTICE,
            "startDateOfPractice.lessThanOrEqual=" + SMALLER_START_DATE_OF_PRACTICE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStartDateOfPracticeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where startDateOfPractice is less than
        defaultDoctorProfileFiltering(
            "startDateOfPractice.lessThan=" + UPDATED_START_DATE_OF_PRACTICE,
            "startDateOfPractice.lessThan=" + DEFAULT_START_DATE_OF_PRACTICE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStartDateOfPracticeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where startDateOfPractice is greater than
        defaultDoctorProfileFiltering(
            "startDateOfPractice.greaterThan=" + SMALLER_START_DATE_OF_PRACTICE,
            "startDateOfPractice.greaterThan=" + DEFAULT_START_DATE_OF_PRACTICE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationDurationMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationDurationMinutes equals to
        defaultDoctorProfileFiltering(
            "consultationDurationMinutes.equals=" + DEFAULT_CONSULTATION_DURATION_MINUTES,
            "consultationDurationMinutes.equals=" + UPDATED_CONSULTATION_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationDurationMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationDurationMinutes in
        defaultDoctorProfileFiltering(
            "consultationDurationMinutes.in=" + DEFAULT_CONSULTATION_DURATION_MINUTES + "," + UPDATED_CONSULTATION_DURATION_MINUTES,
            "consultationDurationMinutes.in=" + UPDATED_CONSULTATION_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationDurationMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationDurationMinutes is not null
        defaultDoctorProfileFiltering("consultationDurationMinutes.specified=true", "consultationDurationMinutes.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationDurationMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationDurationMinutes is greater than or equal to
        defaultDoctorProfileFiltering(
            "consultationDurationMinutes.greaterThanOrEqual=" + DEFAULT_CONSULTATION_DURATION_MINUTES,
            "consultationDurationMinutes.greaterThanOrEqual=" + (DEFAULT_CONSULTATION_DURATION_MINUTES + 1)
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationDurationMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationDurationMinutes is less than or equal to
        defaultDoctorProfileFiltering(
            "consultationDurationMinutes.lessThanOrEqual=" + DEFAULT_CONSULTATION_DURATION_MINUTES,
            "consultationDurationMinutes.lessThanOrEqual=" + SMALLER_CONSULTATION_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationDurationMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationDurationMinutes is less than
        defaultDoctorProfileFiltering(
            "consultationDurationMinutes.lessThan=" + (DEFAULT_CONSULTATION_DURATION_MINUTES + 1),
            "consultationDurationMinutes.lessThan=" + DEFAULT_CONSULTATION_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationDurationMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationDurationMinutes is greater than
        defaultDoctorProfileFiltering(
            "consultationDurationMinutes.greaterThan=" + SMALLER_CONSULTATION_DURATION_MINUTES,
            "consultationDurationMinutes.greaterThan=" + DEFAULT_CONSULTATION_DURATION_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAcceptingNewPatientsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where acceptingNewPatients equals to
        defaultDoctorProfileFiltering(
            "acceptingNewPatients.equals=" + DEFAULT_ACCEPTING_NEW_PATIENTS,
            "acceptingNewPatients.equals=" + UPDATED_ACCEPTING_NEW_PATIENTS
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAcceptingNewPatientsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where acceptingNewPatients in
        defaultDoctorProfileFiltering(
            "acceptingNewPatients.in=" + DEFAULT_ACCEPTING_NEW_PATIENTS + "," + UPDATED_ACCEPTING_NEW_PATIENTS,
            "acceptingNewPatients.in=" + UPDATED_ACCEPTING_NEW_PATIENTS
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAcceptingNewPatientsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where acceptingNewPatients is not null
        defaultDoctorProfileFiltering("acceptingNewPatients.specified=true", "acceptingNewPatients.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAllowsTeleconsultationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where allowsTeleconsultation equals to
        defaultDoctorProfileFiltering(
            "allowsTeleconsultation.equals=" + DEFAULT_ALLOWS_TELECONSULTATION,
            "allowsTeleconsultation.equals=" + UPDATED_ALLOWS_TELECONSULTATION
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAllowsTeleconsultationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where allowsTeleconsultation in
        defaultDoctorProfileFiltering(
            "allowsTeleconsultation.in=" + DEFAULT_ALLOWS_TELECONSULTATION + "," + UPDATED_ALLOWS_TELECONSULTATION,
            "allowsTeleconsultation.in=" + UPDATED_ALLOWS_TELECONSULTATION
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAllowsTeleconsultationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where allowsTeleconsultation is not null
        defaultDoctorProfileFiltering("allowsTeleconsultation.specified=true", "allowsTeleconsultation.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationFee equals to
        defaultDoctorProfileFiltering(
            "consultationFee.equals=" + DEFAULT_CONSULTATION_FEE,
            "consultationFee.equals=" + UPDATED_CONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationFee in
        defaultDoctorProfileFiltering(
            "consultationFee.in=" + DEFAULT_CONSULTATION_FEE + "," + UPDATED_CONSULTATION_FEE,
            "consultationFee.in=" + UPDATED_CONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationFee is not null
        defaultDoctorProfileFiltering("consultationFee.specified=true", "consultationFee.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationFee is greater than or equal to
        defaultDoctorProfileFiltering(
            "consultationFee.greaterThanOrEqual=" + DEFAULT_CONSULTATION_FEE,
            "consultationFee.greaterThanOrEqual=" + UPDATED_CONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationFee is less than or equal to
        defaultDoctorProfileFiltering(
            "consultationFee.lessThanOrEqual=" + DEFAULT_CONSULTATION_FEE,
            "consultationFee.lessThanOrEqual=" + SMALLER_CONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationFee is less than
        defaultDoctorProfileFiltering(
            "consultationFee.lessThan=" + UPDATED_CONSULTATION_FEE,
            "consultationFee.lessThan=" + DEFAULT_CONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByConsultationFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where consultationFee is greater than
        defaultDoctorProfileFiltering(
            "consultationFee.greaterThan=" + SMALLER_CONSULTATION_FEE,
            "consultationFee.greaterThan=" + DEFAULT_CONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByTeleconsultationFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where teleconsultationFee equals to
        defaultDoctorProfileFiltering(
            "teleconsultationFee.equals=" + DEFAULT_TELECONSULTATION_FEE,
            "teleconsultationFee.equals=" + UPDATED_TELECONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByTeleconsultationFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where teleconsultationFee in
        defaultDoctorProfileFiltering(
            "teleconsultationFee.in=" + DEFAULT_TELECONSULTATION_FEE + "," + UPDATED_TELECONSULTATION_FEE,
            "teleconsultationFee.in=" + UPDATED_TELECONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByTeleconsultationFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where teleconsultationFee is not null
        defaultDoctorProfileFiltering("teleconsultationFee.specified=true", "teleconsultationFee.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByTeleconsultationFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where teleconsultationFee is greater than or equal to
        defaultDoctorProfileFiltering(
            "teleconsultationFee.greaterThanOrEqual=" + DEFAULT_TELECONSULTATION_FEE,
            "teleconsultationFee.greaterThanOrEqual=" + UPDATED_TELECONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByTeleconsultationFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where teleconsultationFee is less than or equal to
        defaultDoctorProfileFiltering(
            "teleconsultationFee.lessThanOrEqual=" + DEFAULT_TELECONSULTATION_FEE,
            "teleconsultationFee.lessThanOrEqual=" + SMALLER_TELECONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByTeleconsultationFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where teleconsultationFee is less than
        defaultDoctorProfileFiltering(
            "teleconsultationFee.lessThan=" + UPDATED_TELECONSULTATION_FEE,
            "teleconsultationFee.lessThan=" + DEFAULT_TELECONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByTeleconsultationFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where teleconsultationFee is greater than
        defaultDoctorProfileFiltering(
            "teleconsultationFee.greaterThan=" + SMALLER_TELECONSULTATION_FEE,
            "teleconsultationFee.greaterThan=" + DEFAULT_TELECONSULTATION_FEE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesBySpokenLanguagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where spokenLanguages equals to
        defaultDoctorProfileFiltering(
            "spokenLanguages.equals=" + DEFAULT_SPOKEN_LANGUAGES,
            "spokenLanguages.equals=" + UPDATED_SPOKEN_LANGUAGES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesBySpokenLanguagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where spokenLanguages in
        defaultDoctorProfileFiltering(
            "spokenLanguages.in=" + DEFAULT_SPOKEN_LANGUAGES + "," + UPDATED_SPOKEN_LANGUAGES,
            "spokenLanguages.in=" + UPDATED_SPOKEN_LANGUAGES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesBySpokenLanguagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where spokenLanguages is not null
        defaultDoctorProfileFiltering("spokenLanguages.specified=true", "spokenLanguages.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesBySpokenLanguagesContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where spokenLanguages contains
        defaultDoctorProfileFiltering(
            "spokenLanguages.contains=" + DEFAULT_SPOKEN_LANGUAGES,
            "spokenLanguages.contains=" + UPDATED_SPOKEN_LANGUAGES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesBySpokenLanguagesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where spokenLanguages does not contain
        defaultDoctorProfileFiltering(
            "spokenLanguages.doesNotContain=" + UPDATED_SPOKEN_LANGUAGES,
            "spokenLanguages.doesNotContain=" + DEFAULT_SPOKEN_LANGUAGES
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByWebsiteUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where websiteUrl equals to
        defaultDoctorProfileFiltering("websiteUrl.equals=" + DEFAULT_WEBSITE_URL, "websiteUrl.equals=" + UPDATED_WEBSITE_URL);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByWebsiteUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where websiteUrl in
        defaultDoctorProfileFiltering(
            "websiteUrl.in=" + DEFAULT_WEBSITE_URL + "," + UPDATED_WEBSITE_URL,
            "websiteUrl.in=" + UPDATED_WEBSITE_URL
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByWebsiteUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where websiteUrl is not null
        defaultDoctorProfileFiltering("websiteUrl.specified=true", "websiteUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByWebsiteUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where websiteUrl contains
        defaultDoctorProfileFiltering("websiteUrl.contains=" + DEFAULT_WEBSITE_URL, "websiteUrl.contains=" + UPDATED_WEBSITE_URL);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByWebsiteUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where websiteUrl does not contain
        defaultDoctorProfileFiltering(
            "websiteUrl.doesNotContain=" + UPDATED_WEBSITE_URL,
            "websiteUrl.doesNotContain=" + DEFAULT_WEBSITE_URL
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByOfficePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where officePhone equals to
        defaultDoctorProfileFiltering("officePhone.equals=" + DEFAULT_OFFICE_PHONE, "officePhone.equals=" + UPDATED_OFFICE_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByOfficePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where officePhone in
        defaultDoctorProfileFiltering(
            "officePhone.in=" + DEFAULT_OFFICE_PHONE + "," + UPDATED_OFFICE_PHONE,
            "officePhone.in=" + UPDATED_OFFICE_PHONE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByOfficePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where officePhone is not null
        defaultDoctorProfileFiltering("officePhone.specified=true", "officePhone.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByOfficePhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where officePhone contains
        defaultDoctorProfileFiltering("officePhone.contains=" + DEFAULT_OFFICE_PHONE, "officePhone.contains=" + UPDATED_OFFICE_PHONE);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByOfficePhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where officePhone does not contain
        defaultDoctorProfileFiltering(
            "officePhone.doesNotContain=" + UPDATED_OFFICE_PHONE,
            "officePhone.doesNotContain=" + DEFAULT_OFFICE_PHONE
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where status equals to
        defaultDoctorProfileFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where status in
        defaultDoctorProfileFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where status is not null
        defaultDoctorProfileFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByIsVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where isVerified equals to
        defaultDoctorProfileFiltering("isVerified.equals=" + DEFAULT_IS_VERIFIED, "isVerified.equals=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByIsVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where isVerified in
        defaultDoctorProfileFiltering(
            "isVerified.in=" + DEFAULT_IS_VERIFIED + "," + UPDATED_IS_VERIFIED,
            "isVerified.in=" + UPDATED_IS_VERIFIED
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByIsVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where isVerified is not null
        defaultDoctorProfileFiltering("isVerified.specified=true", "isVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVerifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where verifiedAt equals to
        defaultDoctorProfileFiltering("verifiedAt.equals=" + DEFAULT_VERIFIED_AT, "verifiedAt.equals=" + UPDATED_VERIFIED_AT);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVerifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where verifiedAt in
        defaultDoctorProfileFiltering(
            "verifiedAt.in=" + DEFAULT_VERIFIED_AT + "," + UPDATED_VERIFIED_AT,
            "verifiedAt.in=" + UPDATED_VERIFIED_AT
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVerifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where verifiedAt is not null
        defaultDoctorProfileFiltering("verifiedAt.specified=true", "verifiedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where nif equals to
        defaultDoctorProfileFiltering("nif.equals=" + DEFAULT_NIF, "nif.equals=" + UPDATED_NIF);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where nif in
        defaultDoctorProfileFiltering("nif.in=" + DEFAULT_NIF + "," + UPDATED_NIF, "nif.in=" + UPDATED_NIF);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where nif is not null
        defaultDoctorProfileFiltering("nif.specified=true", "nif.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNifContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where nif contains
        defaultDoctorProfileFiltering("nif.contains=" + DEFAULT_NIF, "nif.contains=" + UPDATED_NIF);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNifNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where nif does not contain
        defaultDoctorProfileFiltering("nif.doesNotContain=" + UPDATED_NIF, "nif.doesNotContain=" + DEFAULT_NIF);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNinuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where ninu equals to
        defaultDoctorProfileFiltering("ninu.equals=" + DEFAULT_NINU, "ninu.equals=" + UPDATED_NINU);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNinuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where ninu in
        defaultDoctorProfileFiltering("ninu.in=" + DEFAULT_NINU + "," + UPDATED_NINU, "ninu.in=" + UPDATED_NINU);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNinuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where ninu is not null
        defaultDoctorProfileFiltering("ninu.specified=true", "ninu.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNinuContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where ninu contains
        defaultDoctorProfileFiltering("ninu.contains=" + DEFAULT_NINU, "ninu.contains=" + UPDATED_NINU);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByNinuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where ninu does not contain
        defaultDoctorProfileFiltering("ninu.doesNotContain=" + UPDATED_NINU, "ninu.doesNotContain=" + DEFAULT_NINU);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAverageRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where averageRating equals to
        defaultDoctorProfileFiltering("averageRating.equals=" + DEFAULT_AVERAGE_RATING, "averageRating.equals=" + UPDATED_AVERAGE_RATING);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAverageRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where averageRating in
        defaultDoctorProfileFiltering(
            "averageRating.in=" + DEFAULT_AVERAGE_RATING + "," + UPDATED_AVERAGE_RATING,
            "averageRating.in=" + UPDATED_AVERAGE_RATING
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAverageRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where averageRating is not null
        defaultDoctorProfileFiltering("averageRating.specified=true", "averageRating.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAverageRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where averageRating is greater than or equal to
        defaultDoctorProfileFiltering(
            "averageRating.greaterThanOrEqual=" + DEFAULT_AVERAGE_RATING,
            "averageRating.greaterThanOrEqual=" + (DEFAULT_AVERAGE_RATING + 1)
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAverageRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where averageRating is less than or equal to
        defaultDoctorProfileFiltering(
            "averageRating.lessThanOrEqual=" + DEFAULT_AVERAGE_RATING,
            "averageRating.lessThanOrEqual=" + SMALLER_AVERAGE_RATING
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAverageRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where averageRating is less than
        defaultDoctorProfileFiltering(
            "averageRating.lessThan=" + (DEFAULT_AVERAGE_RATING + 1),
            "averageRating.lessThan=" + DEFAULT_AVERAGE_RATING
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByAverageRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where averageRating is greater than
        defaultDoctorProfileFiltering(
            "averageRating.greaterThan=" + SMALLER_AVERAGE_RATING,
            "averageRating.greaterThan=" + DEFAULT_AVERAGE_RATING
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByReviewCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where reviewCount equals to
        defaultDoctorProfileFiltering("reviewCount.equals=" + DEFAULT_REVIEW_COUNT, "reviewCount.equals=" + UPDATED_REVIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByReviewCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where reviewCount in
        defaultDoctorProfileFiltering(
            "reviewCount.in=" + DEFAULT_REVIEW_COUNT + "," + UPDATED_REVIEW_COUNT,
            "reviewCount.in=" + UPDATED_REVIEW_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByReviewCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where reviewCount is not null
        defaultDoctorProfileFiltering("reviewCount.specified=true", "reviewCount.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByReviewCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where reviewCount is greater than or equal to
        defaultDoctorProfileFiltering(
            "reviewCount.greaterThanOrEqual=" + DEFAULT_REVIEW_COUNT,
            "reviewCount.greaterThanOrEqual=" + UPDATED_REVIEW_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByReviewCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where reviewCount is less than or equal to
        defaultDoctorProfileFiltering(
            "reviewCount.lessThanOrEqual=" + DEFAULT_REVIEW_COUNT,
            "reviewCount.lessThanOrEqual=" + SMALLER_REVIEW_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByReviewCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where reviewCount is less than
        defaultDoctorProfileFiltering("reviewCount.lessThan=" + UPDATED_REVIEW_COUNT, "reviewCount.lessThan=" + DEFAULT_REVIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByReviewCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where reviewCount is greater than
        defaultDoctorProfileFiltering("reviewCount.greaterThan=" + SMALLER_REVIEW_COUNT, "reviewCount.greaterThan=" + DEFAULT_REVIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where version equals to
        defaultDoctorProfileFiltering("version.equals=" + DEFAULT_VERSION, "version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where version in
        defaultDoctorProfileFiltering("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION, "version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where version is not null
        defaultDoctorProfileFiltering("version.specified=true", "version.specified=false");
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where version is greater than or equal to
        defaultDoctorProfileFiltering("version.greaterThanOrEqual=" + DEFAULT_VERSION, "version.greaterThanOrEqual=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where version is less than or equal to
        defaultDoctorProfileFiltering("version.lessThanOrEqual=" + DEFAULT_VERSION, "version.lessThanOrEqual=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where version is less than
        defaultDoctorProfileFiltering("version.lessThan=" + UPDATED_VERSION, "version.lessThan=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDoctorProfile = doctorProfileRepository.saveAndFlush(doctorProfile);

        // Get all the doctorProfileList where version is greater than
        defaultDoctorProfileFiltering("version.greaterThan=" + SMALLER_VERSION, "version.greaterThan=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllDoctorProfilesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = doctorProfile.getUser();
        doctorProfileRepository.saveAndFlush(doctorProfile);
        Long userId = user.getId();
        // Get all the doctorProfileList where user equals to userId
        defaultDoctorProfileShouldBeFound("userId.equals=" + userId);

        // Get all the doctorProfileList where user equals to (userId + 1)
        defaultDoctorProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultDoctorProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDoctorProfileShouldBeFound(shouldBeFound);
        defaultDoctorProfileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorProfileShouldBeFound(String filter) throws Exception {
        restDoctorProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeClinic").value(hasItem(DEFAULT_CODE_CLINIC)))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].medicalLicenseNumber").value(hasItem(DEFAULT_MEDICAL_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].bloodType").value(hasItem(DEFAULT_BLOOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].primarySpecialty").value(hasItem(DEFAULT_PRIMARY_SPECIALTY.toString())))
            .andExpect(jsonPath("$.[*].otherSpecialties").value(hasItem(DEFAULT_OTHER_SPECIALTIES.toString())))
            .andExpect(jsonPath("$.[*].university").value(hasItem(DEFAULT_UNIVERSITY)))
            .andExpect(jsonPath("$.[*].graduationYear").value(hasItem(DEFAULT_GRADUATION_YEAR)))
            .andExpect(jsonPath("$.[*].startDateOfPractice").value(hasItem(DEFAULT_START_DATE_OF_PRACTICE.toString())))
            .andExpect(jsonPath("$.[*].consultationDurationMinutes").value(hasItem(DEFAULT_CONSULTATION_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].acceptingNewPatients").value(hasItem(DEFAULT_ACCEPTING_NEW_PATIENTS.booleanValue())))
            .andExpect(jsonPath("$.[*].allowsTeleconsultation").value(hasItem(DEFAULT_ALLOWS_TELECONSULTATION.booleanValue())))
            .andExpect(jsonPath("$.[*].consultationFee").value(hasItem(sameNumber(DEFAULT_CONSULTATION_FEE))))
            .andExpect(jsonPath("$.[*].teleconsultationFee").value(hasItem(sameNumber(DEFAULT_TELECONSULTATION_FEE))))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].spokenLanguages").value(hasItem(DEFAULT_SPOKEN_LANGUAGES)))
            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL)))
            .andExpect(jsonPath("$.[*].officePhone").value(hasItem(DEFAULT_OFFICE_PHONE)))
            .andExpect(jsonPath("$.[*].officeAddress").value(hasItem(DEFAULT_OFFICE_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].verifiedAt").value(hasItem(DEFAULT_VERIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].nif").value(hasItem(DEFAULT_NIF)))
            .andExpect(jsonPath("$.[*].ninu").value(hasItem(DEFAULT_NINU)))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].reviewCount").value(hasItem(DEFAULT_REVIEW_COUNT)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.intValue())));

        // Check, that the count call also returns 1
        restDoctorProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorProfileShouldNotBeFound(String filter) throws Exception {
        restDoctorProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .codeClinic(UPDATED_CODE_CLINIC)
            .uid(UPDATED_UID)
            .medicalLicenseNumber(UPDATED_MEDICAL_LICENSE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .bloodType(UPDATED_BLOOD_TYPE)
            .primarySpecialty(UPDATED_PRIMARY_SPECIALTY)
            .otherSpecialties(UPDATED_OTHER_SPECIALTIES)
            .university(UPDATED_UNIVERSITY)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .startDateOfPractice(UPDATED_START_DATE_OF_PRACTICE)
            .consultationDurationMinutes(UPDATED_CONSULTATION_DURATION_MINUTES)
            .acceptingNewPatients(UPDATED_ACCEPTING_NEW_PATIENTS)
            .allowsTeleconsultation(UPDATED_ALLOWS_TELECONSULTATION)
            .consultationFee(UPDATED_CONSULTATION_FEE)
            .teleconsultationFee(UPDATED_TELECONSULTATION_FEE)
            .bio(UPDATED_BIO)
            .spokenLanguages(UPDATED_SPOKEN_LANGUAGES)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .officePhone(UPDATED_OFFICE_PHONE)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .status(UPDATED_STATUS)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifiedAt(UPDATED_VERIFIED_AT)
            .nif(UPDATED_NIF)
            .ninu(UPDATED_NINU)
            .averageRating(UPDATED_AVERAGE_RATING)
            .reviewCount(UPDATED_REVIEW_COUNT)
            .version(UPDATED_VERSION);
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
            .uid(UPDATED_UID)
            .birthDate(UPDATED_BIRTH_DATE)
            .bloodType(UPDATED_BLOOD_TYPE)
            .university(UPDATED_UNIVERSITY)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .allowsTeleconsultation(UPDATED_ALLOWS_TELECONSULTATION)
            .consultationFee(UPDATED_CONSULTATION_FEE)
            .teleconsultationFee(UPDATED_TELECONSULTATION_FEE)
            .spokenLanguages(UPDATED_SPOKEN_LANGUAGES)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .officePhone(UPDATED_OFFICE_PHONE)
            .status(UPDATED_STATUS)
            .verifiedAt(UPDATED_VERIFIED_AT)
            .averageRating(UPDATED_AVERAGE_RATING)
            .reviewCount(UPDATED_REVIEW_COUNT);

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
            .codeClinic(UPDATED_CODE_CLINIC)
            .uid(UPDATED_UID)
            .medicalLicenseNumber(UPDATED_MEDICAL_LICENSE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .bloodType(UPDATED_BLOOD_TYPE)
            .primarySpecialty(UPDATED_PRIMARY_SPECIALTY)
            .otherSpecialties(UPDATED_OTHER_SPECIALTIES)
            .university(UPDATED_UNIVERSITY)
            .graduationYear(UPDATED_GRADUATION_YEAR)
            .startDateOfPractice(UPDATED_START_DATE_OF_PRACTICE)
            .consultationDurationMinutes(UPDATED_CONSULTATION_DURATION_MINUTES)
            .acceptingNewPatients(UPDATED_ACCEPTING_NEW_PATIENTS)
            .allowsTeleconsultation(UPDATED_ALLOWS_TELECONSULTATION)
            .consultationFee(UPDATED_CONSULTATION_FEE)
            .teleconsultationFee(UPDATED_TELECONSULTATION_FEE)
            .bio(UPDATED_BIO)
            .spokenLanguages(UPDATED_SPOKEN_LANGUAGES)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .officePhone(UPDATED_OFFICE_PHONE)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .status(UPDATED_STATUS)
            .isVerified(UPDATED_IS_VERIFIED)
            .verifiedAt(UPDATED_VERIFIED_AT)
            .nif(UPDATED_NIF)
            .ninu(UPDATED_NINU)
            .averageRating(UPDATED_AVERAGE_RATING)
            .reviewCount(UPDATED_REVIEW_COUNT)
            .version(UPDATED_VERSION);

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
