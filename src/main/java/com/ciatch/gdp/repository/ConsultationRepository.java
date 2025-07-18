package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.Consultation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Consultation entity.
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    @Query("select consultation from Consultation consultation where consultation.doctor.login = ?#{authentication.name}")
    List<Consultation> findByDoctorIsCurrentUser();

    default Optional<Consultation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Consultation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Consultation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select consultation from Consultation consultation left join fetch consultation.doctor left join fetch consultation.patient",
        countQuery = "select count(consultation) from Consultation consultation"
    )
    Page<Consultation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select consultation from Consultation consultation left join fetch consultation.doctor left join fetch consultation.patient")
    List<Consultation> findAllWithToOneRelationships();

    @Query(
        "select consultation from Consultation consultation left join fetch consultation.doctor left join fetch consultation.patient where consultation.id =:id"
    )
    Optional<Consultation> findOneWithToOneRelationships(@Param("id") Long id);
}
