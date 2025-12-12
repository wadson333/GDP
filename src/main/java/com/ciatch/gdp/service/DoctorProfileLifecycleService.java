package com.ciatch.gdp.service;

import com.ciatch.gdp.domain.Authority;
import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.enumeration.DoctorStatus;
import com.ciatch.gdp.domain.enumeration.NotificationType;
import com.ciatch.gdp.repository.DoctorProfileRepository;
import com.ciatch.gdp.repository.UserRepository;
import com.ciatch.gdp.security.AuthoritiesConstants;
import com.ciatch.gdp.service.dto.AdminUserDTO;
import com.ciatch.gdp.service.dto.DoctorProfileDTO;
import com.ciatch.gdp.service.dto.DoctorProfileUserDTO;
import com.ciatch.gdp.service.dto.DoctorVerificationDTO;
import com.ciatch.gdp.service.event.DoctorLifecycleEvent;
import com.ciatch.gdp.service.mapper.DoctorProfileMapper;
import com.ciatch.gdp.web.rest.errors.*;
import jakarta.validation.Valid;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing Doctor Profile lifecycle including atomic Doctor+User creation.
 */
@Service
@Transactional
public class DoctorProfileLifecycleService {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorProfileLifecycleService.class);
    private static final String CLINIC_CODE_PREFIX = "GDP-D-";
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 4;

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper doctorProfileMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final SecureRandom random = new SecureRandom();

    public DoctorProfileLifecycleService(
        DoctorProfileRepository doctorProfileRepository,
        DoctorProfileMapper doctorProfileMapper,
        UserRepository userRepository,
        UserService userService,
        ApplicationEventPublisher eventPublisher
    ) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.doctorProfileMapper = doctorProfileMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Atomically create a Doctor Profile with associated User account.
     *
     * @param dto the combined DTO containing both User and DoctorProfile data
     * @return the persisted DoctorProfileDTO
     */
    @Transactional
    public DoctorProfileDTO createDoctorProfileWithUser(@Valid DoctorProfileUserDTO dto) {
        LOG.debug("Request to create DoctorProfile with User : {}", dto);

        // Step A - Uniqueness Checks
        performUniquenessChecks(dto);

        // Step B - User Creation
        User createdUser = createUser(dto);

        // Step C - Data Generation
        UUID uid = generateUUID();
        String codeClinic = generateUniqueClinicCode();

        // Step D - Doctor Profile Creation
        DoctorProfile doctorProfile = buildDoctorProfile(dto, createdUser, uid, codeClinic);
        doctorProfile = doctorProfileRepository.save(doctorProfile);

        // Step E - Publish Event for Async Notification
        LOG.debug("Publishing NEW_DOCTOR_PENDING event for user: {}", createdUser.getLogin());
        eventPublisher.publishEvent(new DoctorLifecycleEvent(createdUser, doctorProfile, NotificationType.NEW_DOCTOR_PENDING, null));

        // Step F - Return
        return doctorProfileMapper.toDto(doctorProfile);
    }

    /**
     * Get combined DoctorProfile and User data by UID.
     *
     * @param uid the UID of the doctor profile
     * @return Optional containing the combined DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<DoctorProfileUserDTO> getDoctorWithUserByUid(UUID uid) {
        LOG.debug("Request to get DoctorProfile with User by UID : {}", uid);

        return doctorProfileRepository
            .findByUid(uid)
            .map(doctorProfile -> {
                DoctorProfileUserDTO dto = new DoctorProfileUserDTO();
                User user = doctorProfile.getUser();

                // User fields
                dto.setLogin(user.getLogin());
                dto.setEmail(user.getEmail());
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
                dto.setLangKey(user.getLangKey());

                // DoctorProfile fields
                dto.setMedicalLicenseNumber(doctorProfile.getMedicalLicenseNumber());
                dto.setBirthDate(doctorProfile.getBirthDate());
                dto.setGender(doctorProfile.getGender());
                dto.setBloodType(doctorProfile.getBloodType());
                dto.setPrimarySpecialty(doctorProfile.getPrimarySpecialty());
                dto.setOtherSpecialties(doctorProfile.getOtherSpecialties());
                dto.setUniversity(doctorProfile.getUniversity());
                dto.setGraduationYear(doctorProfile.getGraduationYear());
                dto.setStartDateOfPractice(doctorProfile.getStartDateOfPractice());
                dto.setConsultationDurationMinutes(doctorProfile.getConsultationDurationMinutes());
                dto.setAcceptingNewPatients(doctorProfile.getAcceptingNewPatients());
                dto.setAllowsTeleconsultation(doctorProfile.getAllowsTeleconsultation());
                dto.setConsultationFee(doctorProfile.getConsultationFee());
                dto.setTeleconsultationFee(doctorProfile.getTeleconsultationFee());
                dto.setBio(doctorProfile.getBio());
                dto.setSpokenLanguages(doctorProfile.getSpokenLanguages());
                dto.setWebsiteUrl(doctorProfile.getWebsiteUrl());
                dto.setOfficePhone(doctorProfile.getOfficePhone());
                dto.setOfficeAddress(doctorProfile.getOfficeAddress());
                dto.setNif(doctorProfile.getNif());
                dto.setNinu(doctorProfile.getNinu());

                // Read-only fields stored for reference (but not editable)
                dto.setUid(doctorProfile.getUid());
                dto.setCodeClinic(doctorProfile.getCodeClinic());

                return dto;
            });
    }

    /**
     * Update Doctor Profile with associated User account atomically.
     *
     * @param dto the combined DTO containing updated data
     * @return the updated DoctorProfileDTO
     */
    @Transactional
    public DoctorProfileDTO updateDoctorWithUser(@Valid DoctorProfileUserDTO dto) {
        LOG.debug("Request to update DoctorProfile with User : {}", dto);

        // Validation: Find existing DoctorProfile by UID
        DoctorProfile existingDoctorProfile = doctorProfileRepository
            .findByUid(dto.getUid())
            .orElseThrow(() -> new BadRequestAlertException("Doctor Profile not found", "doctorProfile", "idnotfound"));

        User existingUser = existingDoctorProfile.getUser();
        if (existingUser == null) {
            throw new BadRequestAlertException("Associated user not found", "user", "usernotfound");
        }

        // Check email uniqueness if changed
        if (!existingUser.getEmail().equalsIgnoreCase(dto.getEmail())) {
            userRepository
                .findOneByEmailIgnoreCase(dto.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(existingUser.getId())) {
                        throw new EmailAlreadyUsedException();
                    }
                });
        }

        // Update User mutable fields
        updateUserFields(existingUser, dto);

        // Update DoctorProfile fields (excluding immutable fields)
        updateDoctorProfileFields(existingDoctorProfile, dto);

        // Save changes
        User updatedUser = userRepository.save(existingUser);
        DoctorProfile updatedDoctorProfile = doctorProfileRepository.save(existingDoctorProfile);

        return doctorProfileMapper.toDto(updatedDoctorProfile);
    }

    /**
     * Verify (approve or reject) a Doctor Profile.
     *
     * @param uid the UID of the doctor profile to verify
     * @param verification the verification decision and optional comment
     * @return the updated DoctorProfileDTO
     */
    @Transactional
    public DoctorProfileDTO verifyDoctor(UUID uid, @Valid DoctorVerificationDTO verification) {
        LOG.debug("Request to verify DoctorProfile with UID: {}, approved: {}", uid, verification.getApproved());

        // Fetch the doctor profile
        DoctorProfile doctorProfile = doctorProfileRepository
            .findByUid(uid)
            .orElseThrow(() -> new BadRequestAlertException("Doctor Profile not found", "doctorProfile", "idnotfound"));

        User user = doctorProfile.getUser();
        if (user == null) {
            throw new BadRequestAlertException("Associated user not found", "user", "usernotfound");
        }

        // Check if already verified
        if (DoctorStatus.ACTIVE.equals(doctorProfile.getStatus())) {
            LOG.warn("Doctor profile {} is already active", uid);
            throw new BadRequestAlertException("Doctor is already verified and active", "doctorProfile", "alreadyverified");
        }

        if (verification.getApproved()) {
            // APPROVAL FLOW
            LOG.info("Approving doctor profile: {}", uid);

            doctorProfile.setStatus(DoctorStatus.ACTIVE);
            doctorProfile.setIsVerified(true);
            doctorProfile.setVerifiedAt(Instant.now());

            // Activate the user account if not already activated
            if (!user.isActivated()) {
                user.setActivated(true);
                userRepository.save(user);
            }

            // Save doctor profile
            doctorProfile = doctorProfileRepository.save(doctorProfile);

            // Publish Event for Async Notification
            LOG.debug("Publishing DOCTOR_PROFILE_APPROVED event for user: {}", user.getLogin());
            eventPublisher.publishEvent(new DoctorLifecycleEvent(user, doctorProfile, NotificationType.DOCTOR_PROFILE_APPROVED, null));
        } else {
            // REJECTION FLOW
            LOG.info("Rejecting doctor profile: {} with reason: {}", uid, verification.getComment());

            doctorProfile.setStatus(DoctorStatus.REJECTED);
            doctorProfile.setIsVerified(false);
            doctorProfile.setVerifiedAt(null);

            // Save doctor profile
            doctorProfile = doctorProfileRepository.save(doctorProfile);

            // Publish Event for Async Notification
            LOG.debug("Publishing DOCTOR_PROFILE_REJECTED event for user: {}", user.getLogin());
            eventPublisher.publishEvent(
                new DoctorLifecycleEvent(user, doctorProfile, NotificationType.DOCTOR_PROFILE_REJECTED, verification.getComment())
            );
        }

        return doctorProfileMapper.toDto(doctorProfile);
    }

    private void updateUserFields(User user, DoctorProfileUserDTO dto) {
        // Update mutable fields only
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setLangKey(dto.getLangKey());
        // DO NOT update: login, password, imageUrl, activated, authorities
    }

    private void updateDoctorProfileFields(DoctorProfile doctorProfile, DoctorProfileUserDTO dto) {
        // Update user denormalized fields
        doctorProfile.setFirstName(dto.getFirstName());
        doctorProfile.setLastName(dto.getLastName());

        // Personal information (mutable)
        doctorProfile.setBirthDate(dto.getBirthDate());
        doctorProfile.setGender(dto.getGender());
        doctorProfile.setBloodType(dto.getBloodType());

        // Medical information (specialty can be updated)
        doctorProfile.setPrimarySpecialty(dto.getPrimarySpecialty());
        doctorProfile.setOtherSpecialties(dto.getOtherSpecialties());

        // Education and experience
        doctorProfile.setUniversity(dto.getUniversity());
        doctorProfile.setGraduationYear(dto.getGraduationYear());
        doctorProfile.setStartDateOfPractice(dto.getStartDateOfPractice());

        // Practice settings
        doctorProfile.setConsultationDurationMinutes(dto.getConsultationDurationMinutes());
        doctorProfile.setAcceptingNewPatients(dto.getAcceptingNewPatients());
        doctorProfile.setAllowsTeleconsultation(dto.getAllowsTeleconsultation());
        doctorProfile.setConsultationFee(dto.getConsultationFee());
        doctorProfile.setTeleconsultationFee(dto.getTeleconsultationFee());

        // Additional information
        doctorProfile.setBio(dto.getBio());
        doctorProfile.setSpokenLanguages(dto.getSpokenLanguages());
        doctorProfile.setWebsiteUrl(dto.getWebsiteUrl());
        doctorProfile.setOfficePhone(dto.getOfficePhone());
        doctorProfile.setOfficeAddress(dto.getOfficeAddress());

        // Identification (NIF/NINU can be updated if not set)
        if (dto.getNif() != null && !dto.getNif().equals(doctorProfile.getNif())) {
            if (doctorProfileRepository.existsByNif(dto.getNif())) {
                throw new NifAlreadyUsedException();
            }
            // TODO: Hash NIF for security
            doctorProfile.setNif(dto.getNif());
        }

        if (dto.getNinu() != null && !dto.getNinu().equals(doctorProfile.getNinu())) {
            if (doctorProfileRepository.existsByNinu(dto.getNinu())) {
                throw new NinuAlreadyUsedException();
            }
            // TODO: Hash NINU for security
            doctorProfile.setNinu(dto.getNinu());
        }
        // IMMUTABLE FIELDS - DO NOT UPDATE:
        // - uid (set at creation)
        // - codeClinic (set at creation)
        // - medicalLicenseNumber (set at creation)
        // - status, isVerified, verifiedAt (managed separately)
        // - averageRating, reviewCount (calculated fields)
        // - version (managed by JPA)
    }

    private void performUniquenessChecks(DoctorProfileUserDTO dto) {
        // Check User uniqueness
        userRepository
            .findOneByLogin(dto.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                throw new LoginAlreadyUsedException();
            });

        userRepository
            .findOneByEmailIgnoreCase(dto.getEmail())
            .ifPresent(existingUser -> {
                throw new EmailAlreadyUsedException();
            });

        // Check DoctorProfile uniqueness
        if (dto.getNif() != null && doctorProfileRepository.existsByNif(dto.getNif())) {
            throw new NifAlreadyUsedException();
        }

        if (dto.getNinu() != null && doctorProfileRepository.existsByNinu(dto.getNinu())) {
            throw new NinuAlreadyUsedException();
        }

        if (doctorProfileRepository.existsByMedicalLicenseNumber(dto.getMedicalLicenseNumber())) {
            throw new MedicalLicenseAlreadyUsedException();
        }
    }

    private User createUser(DoctorProfileUserDTO dto) {
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setLogin(dto.getLogin());
        adminUserDTO.setEmail(dto.getEmail());
        adminUserDTO.setFirstName(dto.getFirstName());
        adminUserDTO.setLastName(dto.getLastName());
        adminUserDTO.setLangKey(dto.getLangKey() != null ? dto.getLangKey() : "en");
        adminUserDTO.setActivated(dto.isActivatedOnCreate());

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.DOCTOR);
        authorities.add(AuthoritiesConstants.USER);
        adminUserDTO.setAuthorities(authorities);

        return userService.createUser(adminUserDTO);
    }

    private String generateUniqueClinicCode() {
        String codeClinic;
        int attempts = 0;
        final int maxAttempts = 20;

        do {
            StringBuilder code = new StringBuilder(CLINIC_CODE_PREFIX);
            for (int i = 0; i < CODE_LENGTH; i++) {
                code.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
            }
            codeClinic = code.toString();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new IllegalStateException("Failed to generate unique clinic code after " + maxAttempts + " attempts");
            }
        } while (doctorProfileRepository.existsByCodeClinic(codeClinic));

        return codeClinic;
    }

    private DoctorProfile buildDoctorProfile(DoctorProfileUserDTO dto, User user, UUID uid, String codeClinic) {
        DoctorProfile doctorProfile = new DoctorProfile();

        // Generated fields
        doctorProfile.setUid(uid);
        doctorProfile.setCodeClinic(codeClinic);
        doctorProfile.setStatus(DoctorStatus.PENDING_APPROVAL);
        doctorProfile.setUser(user);

        // User fields (duplicated for denormalization)
        doctorProfile.setFirstName(dto.getFirstName());
        doctorProfile.setLastName(dto.getLastName());

        // Medical fields
        doctorProfile.setMedicalLicenseNumber(dto.getMedicalLicenseNumber());
        doctorProfile.setPrimarySpecialty(dto.getPrimarySpecialty());
        doctorProfile.setOtherSpecialties(dto.getOtherSpecialties());

        // Personal information
        doctorProfile.setBirthDate(dto.getBirthDate());
        doctorProfile.setGender(dto.getGender());
        doctorProfile.setBloodType(dto.getBloodType());

        // Education and experience
        doctorProfile.setUniversity(dto.getUniversity());
        doctorProfile.setGraduationYear(dto.getGraduationYear());
        doctorProfile.setStartDateOfPractice(dto.getStartDateOfPractice());

        // Practice settings
        doctorProfile.setConsultationDurationMinutes(dto.getConsultationDurationMinutes());
        doctorProfile.setAcceptingNewPatients(dto.getAcceptingNewPatients());
        doctorProfile.setAllowsTeleconsultation(dto.getAllowsTeleconsultation());
        doctorProfile.setConsultationFee(dto.getConsultationFee());
        doctorProfile.setTeleconsultationFee(dto.getTeleconsultationFee());

        // Additional information
        doctorProfile.setBio(dto.getBio());
        doctorProfile.setSpokenLanguages(dto.getSpokenLanguages());
        doctorProfile.setWebsiteUrl(dto.getWebsiteUrl());
        doctorProfile.setOfficePhone(dto.getOfficePhone());
        doctorProfile.setOfficeAddress(dto.getOfficeAddress());

        // Identification
        // TODO: Hash NIF/NINU for security
        doctorProfile.setNif(dto.getNif());
        doctorProfile.setNinu(dto.getNinu());

        // Initialize verification status
        doctorProfile.setIsVerified(false);
        doctorProfile.setAverageRating(0.0);
        doctorProfile.setReviewCount(0);

        return doctorProfile;
    }

    private UUID generateUUID() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (doctorProfileRepository.existsByUid(uuid));
        return uuid;
    }
}
