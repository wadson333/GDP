package com.ciatch.gdp.security;

import static org.assertj.core.api.Assertions.*;

import com.ciatch.gdp.IntegrationTest;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.enumeration.Gender;
import com.ciatch.gdp.domain.enumeration.PatientStatus;
import com.ciatch.gdp.repository.PatientRepository;
import com.ciatch.gdp.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
class CryptoAttributeConverterIT {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setLogin("testuser_" + System.currentTimeMillis());
        testUser.setEmail("test@example.com");
        testUser = userRepository.saveAndFlush(testUser);
    }

    @Test
    @Transactional
    void testEncryptedFieldsAreStoredEncrypted() {
        // Given
        String clearPhone = "+33612345678";
        String clearAddress = "123 Rue de la Santé, Paris";
        String clearAllergies = "Pénicilline, Aspirine";

        Patient patient = new Patient();
        patient.setFirstName("Jean");
        patient.setLastName("Dupont");
        patient.setBirthDate(LocalDate.of(1980, 1, 1));
        patient.setGender(Gender.MALE);
        patient.setStatus(PatientStatus.ACTIVE);
        patient.setPhone1(clearPhone);
        patient.setAddress(clearAddress);
        patient.setAllergies(clearAllergies);
        patient.setUser(testUser);

        // When
        Patient saved = patientRepository.saveAndFlush(patient);
        entityManager.clear(); // Clear persistence context

        // Then - Check database contains encrypted data
        String dbPhone = jdbcTemplate.queryForObject("SELECT phone_1 FROM patient WHERE id = ?", String.class, saved.getId());

        String dbAddress = jdbcTemplate.queryForObject("SELECT address FROM patient WHERE id = ?", String.class, saved.getId());

        String dbAllergies = jdbcTemplate.queryForObject("SELECT allergies FROM patient WHERE id = ?", String.class, saved.getId());

        // Encrypted data should not match cleartext
        assertThat(dbPhone).isNotEqualTo(clearPhone);
        assertThat(dbAddress).isNotEqualTo(clearAddress);
        assertThat(dbAllergies).isNotEqualTo(clearAllergies);

        // But should be valid Base64
        assertThat(dbPhone).matches("^[A-Za-z0-9+/]+=*$");
        assertThat(dbAddress).matches("^[A-Za-z0-9+/]+=*$");
        assertThat(dbAllergies).matches("^[A-Za-z0-9+/]+=*$");

        // And when loading, should decrypt correctly
        Patient loaded = patientRepository.findById(saved.getId()).orElseThrow();
        assertThat(loaded.getPhone1()).isEqualTo(clearPhone);
        assertThat(loaded.getAddress()).isEqualTo(clearAddress);
        assertThat(loaded.getAllergies()).isEqualTo(clearAllergies);
    }

    @Test
    @Transactional
    void testCleartextFieldsRemainUnencrypted() {
        // Given
        String nif = "1234567890";
        String ninu = "9876543210";
        String mrn = "MRN-12345";

        Patient patient = new Patient();
        patient.setFirstName("Marie");
        patient.setLastName("Martin");
        patient.setBirthDate(LocalDate.of(1990, 5, 15));
        patient.setGender(Gender.FEMALE);
        patient.setStatus(PatientStatus.ACTIVE);
        patient.setPhone1("+33698765432");
        patient.setNif(nif);
        patient.setNinu(ninu);
        patient.setMedicalRecordNumber(mrn);
        patient.setUser(testUser);

        // When
        Patient saved = patientRepository.saveAndFlush(patient);
        entityManager.clear();

        // Then - Check cleartext fields are NOT encrypted
        String dbNif = jdbcTemplate.queryForObject("SELECT nif FROM patient WHERE id = ?", String.class, saved.getId());

        String dbNinu = jdbcTemplate.queryForObject("SELECT ninu FROM patient WHERE id = ?", String.class, saved.getId());

        String dbMrn = jdbcTemplate.queryForObject("SELECT medical_record_number FROM patient WHERE id = ?", String.class, saved.getId());

        assertThat(dbNif).isEqualTo(nif);
        assertThat(dbNinu).isEqualTo(ninu);
        assertThat(dbMrn).isEqualTo(mrn);
    }

    @Test
    @Transactional
    void testNullEncryptedFieldsHandling() {
        // Given
        Patient patient = new Patient();
        patient.setFirstName("Test");
        patient.setLastName("User");
        patient.setBirthDate(LocalDate.of(1985, 3, 20));
        patient.setGender(Gender.OTHER);
        patient.setStatus(PatientStatus.ACTIVE);
        patient.setPhone1("+33612345678");
        patient.setUser(testUser);
        // Leave encrypted fields null: phone2, address, allergies, etc.

        // When
        Patient saved = patientRepository.saveAndFlush(patient);
        entityManager.clear();

        // Then
        Patient loaded = patientRepository.findById(saved.getId()).orElseThrow();
        assertThat(loaded.getPhone2()).isNull();
        assertThat(loaded.getAddress()).isNull();
        assertThat(loaded.getAllergies()).isNull();
        assertThat(loaded.getAntecedents()).isNull();
        assertThat(loaded.getClinicalNotes()).isNull();
    }

    @Test
    @Transactional
    void testUpdateEncryptedFields() {
        // Given
        Patient patient = new Patient();
        patient.setFirstName("Update");
        patient.setLastName("Test");
        patient.setBirthDate(LocalDate.of(1975, 8, 10));
        patient.setGender(Gender.MALE);
        patient.setStatus(PatientStatus.ACTIVE);
        patient.setPhone1("+33611111111");
        patient.setAllergies("Aucune allergie connue");
        patient.setUser(testUser);

        Patient saved = patientRepository.saveAndFlush(patient);
        Long patientId = saved.getId();
        entityManager.clear();

        // When - Update encrypted field
        Patient toUpdate = patientRepository.findById(patientId).orElseThrow();
        String newAllergies = "Allergie au latex découverte";
        toUpdate.setAllergies(newAllergies);
        patientRepository.saveAndFlush(toUpdate);
        entityManager.clear();

        // Then
        Patient updated = patientRepository.findById(patientId).orElseThrow();
        assertThat(updated.getAllergies()).isEqualTo(newAllergies);

        // Check database has new encrypted value
        String dbAllergies = jdbcTemplate.queryForObject("SELECT allergies FROM patient WHERE id = ?", String.class, patientId);

        assertThat(dbAllergies).isNotEqualTo(newAllergies);
        assertThat(dbAllergies).matches("^[A-Za-z0-9+/]+=*$");
    }
}
