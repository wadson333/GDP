package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.domain.enumeration.LabTestMethod;
import com.ciatch.gdp.domain.enumeration.LabTestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LabTestCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LabTestCatalogRepository extends JpaRepository<LabTestCatalog, Long> {
    /**
     * Searches for LabTestCatalog entities based on the provided criteria.
     *
     * @param name the name of the lab test to search for (can be partial or full name)
     * @param type the type of the lab test to filter by (can be null to ignore)
     * @param method the method of the lab test to filter by (can be null to ignore)
     * @param active the active status to filter by (can be null to ignore)
     * @param pageable the pagination information
     * @return a page of {@link LabTestCatalog} matching the search criteria
     */
    @Query(
        "SELECT ltc FROM LabTestCatalog ltc WHERE " +
        "(:name IS NULL OR (ltc.name IS NOT NULL AND LOWER(ltc.name) LIKE LOWER(CONCAT('%', :name, '%')))) AND " +
        "(:type IS NULL OR ltc.type = :type) AND " +
        "(:method IS NULL OR ltc.method = :method) AND " +
        "(:active IS NULL OR ltc.active = :active)"
    )
    Page<LabTestCatalog> search(
        @Param("name") String name,
        @Param("type") LabTestType type,
        @Param("method") LabTestMethod method,
        @Param("active") Boolean active,
        Pageable pageable
    );

    /**
     * Retrieves the latest version of a {@link LabTestCatalog} entity by its name.
     * <p>
     * This method executes a query to find the {@link LabTestCatalog} with the specified name,
     * ordering the results by the {@code version} field in descending order, and returns the first result.
     * </p>
     *
     * @param name the name of the lab test catalog to search for
     * @return the latest version of the {@link LabTestCatalog} with the given name, or {@code null} if not found
     */
    @Query("SELECT ltc FROM LabTestCatalog ltc WHERE " + "ltc.name = :name ORDER BY ltc.version DESC")
    LabTestCatalog findLatestVersionByName(@Param("name") String name);
}
