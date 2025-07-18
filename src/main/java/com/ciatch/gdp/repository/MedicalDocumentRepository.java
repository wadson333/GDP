package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.MedicalDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicalDocument entity.
 */
@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {
    default Optional<MedicalDocument> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicalDocument> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicalDocument> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicalDocument from MedicalDocument medicalDocument left join fetch medicalDocument.patient",
        countQuery = "select count(medicalDocument) from MedicalDocument medicalDocument"
    )
    Page<MedicalDocument> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicalDocument from MedicalDocument medicalDocument left join fetch medicalDocument.patient")
    List<MedicalDocument> findAllWithToOneRelationships();

    @Query(
        "select medicalDocument from MedicalDocument medicalDocument left join fetch medicalDocument.patient where medicalDocument.id =:id"
    )
    Optional<MedicalDocument> findOneWithToOneRelationships(@Param("id") Long id);
}
