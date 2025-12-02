package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.Authority;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.enumeration.PatientStatus;
import com.ciatch.gdp.repository.AuthorityRepository;
import com.ciatch.gdp.repository.PatientRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.security.AuthoritiesConstants;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.dto.PatientUserDTO;
import com.ciatch.gdp.service.mapper.PatientMapper;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

@Service
@Transactional
public class PatientLifecycleService {

    private final Logger log = LoggerFactory.getLogger(PatientLifecycleService.class);

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final MailService mailService;

    public PatientLifecycleService(
        UserRepository userRepository,
        PatientRepository patientRepository,
        PatientMapper patientMapper,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        MailService mailService
    ) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.mailService = mailService;
    }

    // @Transactional
    // public PatientDTO createPatientWithUser(PatientUserDTO dto) {
    //     log.debug("Request to create Patient and User for login : {}", dto.getLogin());

    //     // --- Step A: Uniqueness Checks ---
    //     if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
    //         throw new UsernameAlreadyUsedException();
    //     }
    //     if (userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
    //         throw new EmailAlreadyUsedException();
    //     }
    //     if (dto.getNif() != null && patientRepository.existsByNif(dto.getNif())) {
    //         throw new NifAlreadyUsedException();
    //     }
    //     if (dto.getNinu() != null && patientRepository.existsByNinu(dto.getNinu())) {
    //         throw new NinuAlreadyUsedException();
    //     }
    //     if (dto.getPassportNumber() != null && patientRepository.existsByPassportNumber(dto.getPassportNumber())) {
    //         throw new PassportNumberAlreadyUsedException();
    //     }
    //     if (dto.getPatientInsuranceId() != null && patientRepository.existsByPatientInsuranceId(dto.getPatientInsuranceId())) {
    //         throw new InsuranceIdAlreadyUsedException();
    //     }

    //     // --- Step B: User Creation ---
    //     User newUser = new User();
    //     String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
    //     newUser.setLogin(dto.getLogin().toLowerCase());
    //     newUser.setPassword(encryptedPassword);
    //     newUser.setFirstName(dto.getFirstName());
    //     newUser.setLastName(dto.getLastName());
    //     if (dto.getEmail() != null) {
    //         newUser.setEmail(dto.getEmail().toLowerCase());
    //     }
    //     newUser.setLangKey(dto.getLangKey());

    //     // Activation logic
    //     newUser.setActivated(dto.isActivatedOnCreate());
    //     if (!dto.isActivatedOnCreate()) {
    //         newUser.setActivationKey(RandomUtil.generateActivationKey());
    //     }

    //     // Authorities
    //     Set<Authority> authorities = new HashSet<>();
    //     authorityRepository.findById(AuthoritiesConstants.PATIENT).ifPresent(authorities::add);
    //     // Also add USER role by default if needed, otherwise just PATIENT
    //     authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
    //     newUser.setAuthorities(authorities);

    //     userRepository.save(newUser);
    //     log.debug("Created Information for User: {}", newUser);

    //     // --- Step C: Data Generation ---
    //     UUID uid = generateUUID();
    //     String mrn = generateMedicalRecordNumber();

    //     // --- Step D: Patient Creation ---
    //     Patient patient = new Patient();
    //     patient.setUid(uid);
    //     patient.setMedicalRecordNumber(mrn);

    //     // Map fields
    //     patient.setFirstName(dto.getFirstName());
    //     patient.setLastName(dto.getLastName());
    //     patient.setBirthDate(dto.getBirthDate());
    //     patient.setPhone1(dto.getPhone1());
    //     patient.setPhone2(dto.getPhone2());
    //     patient.setAddress(dto.getAddress());
    //     patient.setGender(dto.getGender());
    //     patient.setBloodType(dto.getBloodType());
    //     patient.setInsuranceCompanyName(dto.getInsuranceCompanyName());
    //     patient.setStatus(PatientStatus.ACTIVE); // Default status

    //     // TODO: Hash NIF/NINU before saving (handled later)
    //     patient.setNif(dto.getNif());
    //     patient.setNinu(dto.getNinu());
    //     patient.setPassportNumber(dto.getPassportNumber());
    //     patient.setPatientInsuranceId(dto.getPatientInsuranceId());
    //     patient.setHeightCm(dto.getHeightCm());
    //     patient.setWeightKg(dto.getWeightKg());
    //     patient.setAntecedents(dto.getAntecedents());
    //     patient.setAllergies(dto.getAllergies());
    //     patient.setClinicalNotes(dto.getClinicalNotes());
    //     patient.setSmokingStatus(dto.getSmokingStatus());
    //     patient.setGdprConsentDate(dto.getGdprConsentDate());
    //     patient.setDeceasedDate(dto.getDeceasedDate());
    //     patient.setInsurancePolicyNumber(dto.getInsurancePolicyNumber());
    //     patient.setInsuranceCoverageType(dto.getInsuranceCoverageType());
    //     patient.setInsuranceValidFrom(dto.getInsuranceValidFrom());
    //     patient.setInsuranceValidTo(dto.getInsuranceValidTo());
    //     patient.setContactPersonName(dto.getContactPersonName());
    //     patient.setContactPersonPhone(dto.getContactPersonPhone());

    //     // Link User
    //     patient.setUser(newUser);

    //     patientRepository.save(patient);
    //     log.debug("Created Information for Patient: {}", patient);

    //     // --- Step E: Notification ---
    //     if (dto.isSendActivationEmail() && dto.getEmail() != null) {
    //         mailService.sendCreationEmail(newUser);
    //     }

    //     // --- Step F: Return ---
    //     return patientMapper.toDto(patient);
    // }

    /**
     * Create a new patient with associated user account.
     *
     * @param dto the PatientUserDTO containing both user and patient data
     * @return the created PatientDTO
     */
    @Transactional
    public PatientDTO createPatientWithUser(PatientUserDTO dto) {
        log.debug("Request to create Patient and User for login : {}", dto.getLogin());

        // --- Step A: Uniqueness Checks ---
        if (userRepository.findOneByLogin(dto.getLogin().toLowerCase()).isPresent()) {
            throw new UsernameAlreadyUsedException();
        }
        if (userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        }
        if (dto.getNif() != null && patientRepository.existsByNif(dto.getNif())) {
            throw new NifAlreadyUsedException();
        }
        if (dto.getNinu() != null && patientRepository.existsByNinu(dto.getNinu())) {
            throw new NinuAlreadyUsedException();
        }
        if (dto.getPassportNumber() != null && patientRepository.existsByPassportNumber(dto.getPassportNumber())) {
            throw new PassportNumberAlreadyUsedException();
        }
        if (dto.getPatientInsuranceId() != null && patientRepository.existsByPatientInsuranceId(dto.getPatientInsuranceId())) {
            throw new InsuranceIdAlreadyUsedException();
        }

        // --- Step B: User Creation ---
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        newUser.setLogin(dto.getLogin().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        if (dto.getEmail() != null) {
            newUser.setEmail(dto.getEmail().toLowerCase());
        }
        newUser.setLangKey(dto.getLangKey());

        // Activation logic
        newUser.setActivated(dto.isActivatedOnCreate());
        if (!dto.isActivatedOnCreate()) {
            newUser.setActivationKey(RandomUtil.generateActivationKey());
        }

        // Authorities
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.PATIENT).ifPresent(authorities::add);
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);

        // --- Step C: Data Generation ---
        UUID uid = generateUUID();
        String mrn = generateMedicalRecordNumber();

        // --- Step D: Patient Creation ---
        Patient patient = new Patient();
        patient.setUid(uid);
        patient.setMedicalRecordNumber(mrn);

        // Map all fields
        mapDtoToPatient(dto, patient);

        // Link User
        patient.setUser(newUser);

        patientRepository.save(patient);
        log.debug("Created Information for Patient: {}", patient);

        // --- Step E: Notification ---
        if (dto.isSendActivationEmail() && dto.getEmail() != null) {
            mailService.sendCreationEmail(newUser);
        }

        // --- Step F: Return ---
        return patientMapper.toDto(patient);
    }

    /**
     * Find a patient with associated user by UID.
     *
     * @param uid the UUID of the patient
     * @return an Optional containing the PatientUserDTO if found
     */
    @Transactional(readOnly = true)
    public Optional<PatientUserDTO> findPatientWithUserByUid(UUID uid) {
        log.debug("Request to get Patient with User by UID : {}", uid);

        return patientRepository
            .findOneWithUserByUid(uid)
            .map(patient -> {
                PatientUserDTO dto = new PatientUserDTO();

                // Map User fields
                User user = patient.getUser();
                if (user != null) {
                    dto.setLogin(user.getLogin());
                    dto.setEmail(user.getEmail());
                    dto.setLangKey(user.getLangKey());
                }

                // Map Patient fields
                dto.setUid(patient.getUid());
                dto.setFirstName(patient.getFirstName());
                dto.setLastName(patient.getLastName());
                dto.setBirthDate(patient.getBirthDate());
                dto.setGender(patient.getGender());
                dto.setBloodType(patient.getBloodType());
                dto.setStatus(patient.getStatus());
                dto.setAddress(patient.getAddress());
                dto.setPhone1(patient.getPhone1());
                dto.setPhone2(patient.getPhone2());
                dto.setNif(patient.getNif());
                dto.setNinu(patient.getNinu());
                dto.setPassportNumber(patient.getPassportNumber());
                dto.setHeightCm(patient.getHeightCm());
                dto.setWeightKg(patient.getWeightKg());
                dto.setContactPersonName(patient.getContactPersonName());
                dto.setContactPersonPhone(patient.getContactPersonPhone());
                dto.setAntecedents(patient.getAntecedents());
                dto.setAllergies(patient.getAllergies());
                dto.setClinicalNotes(patient.getClinicalNotes());
                dto.setSmokingStatus(patient.getSmokingStatus());
                dto.setGdprConsentDate(patient.getGdprConsentDate());
                dto.setDeceasedDate(patient.getDeceasedDate());
                dto.setInsuranceCompanyName(patient.getInsuranceCompanyName());
                dto.setPatientInsuranceId(patient.getPatientInsuranceId());
                dto.setInsurancePolicyNumber(patient.getInsurancePolicyNumber());
                dto.setInsuranceCoverageType(patient.getInsuranceCoverageType());
                dto.setInsuranceValidFrom(patient.getInsuranceValidFrom());
                dto.setInsuranceValidTo(patient.getInsuranceValidTo());

                return dto;
            });
    }

    /**
     * Update an existing patient with associated user.
     *
     * @param dto the PatientUserDTO containing updated data
     * @return the updated PatientDTO
     */
    @Transactional
    public PatientDTO updatePatientWithUser(PatientUserDTO dto) {
        log.debug("Request to update Patient and User for UID : {}", dto.getUid());

        if (dto.getUid() == null) {
            throw new IllegalArgumentException("Patient UID cannot be null for update");
        }

        // --- Step A: Fetch existing entities ---
        Patient existingPatient = patientRepository
            .findOneWithUserByUid(dto.getUid())
            .orElseThrow(() -> new IllegalArgumentException("Patient not found with UID: " + dto.getUid()));

        User existingUser = existingPatient.getUser();
        if (existingUser == null) {
            throw new IllegalStateException("Patient does not have an associated user");
        }

        // --- Step B: Uniqueness checks (only if values changed) ---
        // Email check
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            if (userRepository.findOneByEmailIgnoreCase(dto.getEmail()).isPresent()) {
                throw new EmailAlreadyUsedException();
            }
        }

        // NIF check
        if (dto.getNif() != null && !dto.getNif().equals(existingPatient.getNif())) {
            if (patientRepository.existsByNif(dto.getNif())) {
                throw new NifAlreadyUsedException();
            }
        }

        // NINU check
        if (dto.getNinu() != null && !dto.getNinu().equals(existingPatient.getNinu())) {
            if (patientRepository.existsByNinu(dto.getNinu())) {
                throw new NinuAlreadyUsedException();
            }
        }

        // Passport check
        if (dto.getPassportNumber() != null && !dto.getPassportNumber().equals(existingPatient.getPassportNumber())) {
            if (patientRepository.existsByPassportNumber(dto.getPassportNumber())) {
                throw new PassportNumberAlreadyUsedException();
            }
        }

        // Insurance ID check
        if (dto.getPatientInsuranceId() != null && !dto.getPatientInsuranceId().equals(existingPatient.getPatientInsuranceId())) {
            if (patientRepository.existsByPatientInsuranceId(dto.getPatientInsuranceId())) {
                throw new InsuranceIdAlreadyUsedException();
            }
        }

        // --- Step C: Update User (mutable fields only) ---
        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        if (dto.getEmail() != null) {
            existingUser.setEmail(dto.getEmail().toLowerCase());
        }
        existingUser.setLangKey(dto.getLangKey());
        // DO NOT update: login, password, activated, imageUrl

        userRepository.save(existingUser);
        log.debug("Updated User: {}", existingUser);

        // --- Step D: Update Patient ---
        mapDtoToPatient(dto, existingPatient);
        // DO NOT update: uid, medicalRecordNumber, user

        patientRepository.save(existingPatient);
        log.debug("Updated Patient: {}", existingPatient);

        // --- Step E: Return ---
        return patientMapper.toDto(existingPatient);
    }

    /**
     * Helper method to map PatientUserDTO to Patient entity.
     */
    private void mapDtoToPatient(PatientUserDTO dto, Patient patient) {
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setBirthDate(dto.getBirthDate());
        patient.setPhone1(dto.getPhone1());
        patient.setPhone2(dto.getPhone2());
        patient.setAddress(dto.getAddress());
        patient.setGender(dto.getGender());
        patient.setBloodType(dto.getBloodType());
        patient.setNif(dto.getNif());
        patient.setNinu(dto.getNinu());
        patient.setPassportNumber(dto.getPassportNumber());
        patient.setPatientInsuranceId(dto.getPatientInsuranceId());
        patient.setHeightCm(dto.getHeightCm());
        patient.setWeightKg(dto.getWeightKg());
        patient.setAntecedents(dto.getAntecedents());
        patient.setAllergies(dto.getAllergies());
        patient.setClinicalNotes(dto.getClinicalNotes());
        patient.setSmokingStatus(dto.getSmokingStatus());
        patient.setGdprConsentDate(dto.getGdprConsentDate());
        patient.setDeceasedDate(dto.getDeceasedDate());
        patient.setInsuranceCompanyName(dto.getInsuranceCompanyName());
        patient.setInsurancePolicyNumber(dto.getInsurancePolicyNumber());
        patient.setInsuranceCoverageType(dto.getInsuranceCoverageType());
        patient.setInsuranceValidFrom(dto.getInsuranceValidFrom());
        patient.setInsuranceValidTo(dto.getInsuranceValidTo());
        patient.setContactPersonName(dto.getContactPersonName());
        patient.setContactPersonPhone(dto.getContactPersonPhone());

        // Status handling
        if (dto.getStatus() != null) {
            patient.setStatus(dto.getStatus());
        } else {
            patient.setStatus(PatientStatus.ACTIVE); // Default for new patients
        }
    }

    private UUID generateUUID() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (patientRepository.existsByUid(uuid));
        return uuid;
    }

    private String generateMedicalRecordNumber() {
        String mrn;
        do {
            // Format: GDP-MRN-XXXXX (5 random alphanumeric)
            String suffix = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
            mrn = "GDP-MRN-" + suffix;
        } while (patientRepository.existsByMedicalRecordNumber(mrn));
        return mrn;
    }
}
