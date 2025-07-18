package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.LabTestResult;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LabTestResult entity.
 */
@Repository
public interface LabTestResultRepository extends JpaRepository<LabTestResult, Long> {
    default Optional<LabTestResult> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LabTestResult> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LabTestResult> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select labTestResult from LabTestResult labTestResult left join fetch labTestResult.patient left join fetch labTestResult.labTest",
        countQuery = "select count(labTestResult) from LabTestResult labTestResult"
    )
    Page<LabTestResult> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select labTestResult from LabTestResult labTestResult left join fetch labTestResult.patient left join fetch labTestResult.labTest"
    )
    List<LabTestResult> findAllWithToOneRelationships();

    @Query(
        "select labTestResult from LabTestResult labTestResult left join fetch labTestResult.patient left join fetch labTestResult.labTest where labTestResult.id =:id"
    )
    Optional<LabTestResult> findOneWithToOneRelationships(@Param("id") Long id);
}
