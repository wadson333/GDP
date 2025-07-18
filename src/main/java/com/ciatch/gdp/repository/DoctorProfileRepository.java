package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.DoctorProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DoctorProfile entity.
 */
@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    default Optional<DoctorProfile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DoctorProfile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DoctorProfile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select doctorProfile from DoctorProfile doctorProfile left join fetch doctorProfile.user",
        countQuery = "select count(doctorProfile) from DoctorProfile doctorProfile"
    )
    Page<DoctorProfile> findAllWithToOneRelationships(Pageable pageable);

    @Query("select doctorProfile from DoctorProfile doctorProfile left join fetch doctorProfile.user")
    List<DoctorProfile> findAllWithToOneRelationships();

    @Query("select doctorProfile from DoctorProfile doctorProfile left join fetch doctorProfile.user where doctorProfile.id =:id")
    Optional<DoctorProfile> findOneWithToOneRelationships(@Param("id") Long id);
}
