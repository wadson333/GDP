package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.PrescriptionItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrescriptionItem entity.
 */
@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {
    default Optional<PrescriptionItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PrescriptionItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PrescriptionItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select prescriptionItem from PrescriptionItem prescriptionItem left join fetch prescriptionItem.medication",
        countQuery = "select count(prescriptionItem) from PrescriptionItem prescriptionItem"
    )
    Page<PrescriptionItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select prescriptionItem from PrescriptionItem prescriptionItem left join fetch prescriptionItem.medication")
    List<PrescriptionItem> findAllWithToOneRelationships();

    @Query(
        "select prescriptionItem from PrescriptionItem prescriptionItem left join fetch prescriptionItem.medication where prescriptionItem.id =:id"
    )
    Optional<PrescriptionItem> findOneWithToOneRelationships(@Param("id") Long id);
}
