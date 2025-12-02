package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.Patient;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    boolean existsByNif(String nif);
    boolean existsByNinu(String ninu);
    boolean existsByUid(UUID uid);
    boolean existsByPassportNumber(String passportNumber);
    boolean existsByPatientInsuranceId(String patientInsuranceId);
    boolean existsByMedicalRecordNumber(String medicalRecordNumber);

    /**
     * Find a patient by UID with its associated user eagerly loaded.
     */
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.user WHERE p.uid = :uid")
    Optional<Patient> findOneWithUserByUid(UUID uid);
}
