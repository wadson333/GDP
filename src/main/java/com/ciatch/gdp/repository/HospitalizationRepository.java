package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.Hospitalization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hospitalization entity.
 */
@Repository
public interface HospitalizationRepository extends JpaRepository<Hospitalization, Long> {
    @Query(
        "select hospitalization from Hospitalization hospitalization where hospitalization.attendingDoctor.login = ?#{authentication.name}"
    )
    List<Hospitalization> findByAttendingDoctorIsCurrentUser();

    default Optional<Hospitalization> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Hospitalization> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Hospitalization> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select hospitalization from Hospitalization hospitalization left join fetch hospitalization.patient left join fetch hospitalization.attendingDoctor",
        countQuery = "select count(hospitalization) from Hospitalization hospitalization"
    )
    Page<Hospitalization> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select hospitalization from Hospitalization hospitalization left join fetch hospitalization.patient left join fetch hospitalization.attendingDoctor"
    )
    List<Hospitalization> findAllWithToOneRelationships();

    @Query(
        "select hospitalization from Hospitalization hospitalization left join fetch hospitalization.patient left join fetch hospitalization.attendingDoctor where hospitalization.id =:id"
    )
    Optional<Hospitalization> findOneWithToOneRelationships(@Param("id") Long id);
}
