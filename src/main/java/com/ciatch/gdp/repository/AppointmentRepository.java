package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.Appointment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Appointment entity.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("select appointment from Appointment appointment where appointment.doctor.login = ?#{authentication.name}")
    List<Appointment> findByDoctorIsCurrentUser();

    default Optional<Appointment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Appointment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Appointment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select appointment from Appointment appointment left join fetch appointment.patient left join fetch appointment.doctor",
        countQuery = "select count(appointment) from Appointment appointment"
    )
    Page<Appointment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select appointment from Appointment appointment left join fetch appointment.patient left join fetch appointment.doctor")
    List<Appointment> findAllWithToOneRelationships();

    @Query(
        "select appointment from Appointment appointment left join fetch appointment.patient left join fetch appointment.doctor where appointment.id =:id"
    )
    Optional<Appointment> findOneWithToOneRelationships(@Param("id") Long id);
}
