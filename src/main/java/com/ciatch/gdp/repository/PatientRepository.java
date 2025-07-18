package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.Patient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Patient entity.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    default Optional<Patient> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Patient> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Patient> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select patient from Patient patient left join fetch patient.user",
        countQuery = "select count(patient) from Patient patient"
    )
    Page<Patient> findAllWithToOneRelationships(Pageable pageable);

    @Query("select patient from Patient patient left join fetch patient.user")
    List<Patient> findAllWithToOneRelationships();

    @Query("select patient from Patient patient left join fetch patient.user where patient.id =:id")
    Optional<Patient> findOneWithToOneRelationships(@Param("id") Long id);
}
