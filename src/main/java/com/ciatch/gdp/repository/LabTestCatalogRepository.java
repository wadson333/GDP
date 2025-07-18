package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.LabTestCatalog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LabTestCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LabTestCatalogRepository extends JpaRepository<LabTestCatalog, Long> {}
