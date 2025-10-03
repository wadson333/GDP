package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.domain.enumeration.LabTestMethod;
import com.ciatch.gdp.domain.enumeration.LabTestType;
import java.util.List;
import java.util.Optional;
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

    /**
     * Retrieves all versions of a lab test catalog by its name, ordered by version descending.
     *
     * @param name the name of the lab test catalog
     * @return list of all versions ordered by version number descending
     */
    @Query("SELECT ltc FROM LabTestCatalog ltc WHERE LOWER(ltc.name) = LOWER(:name) ORDER BY ltc.version DESC")
    List<LabTestCatalog> findAllVersionsByName(@Param("name") String name);

    /**
     * Retrieves all versions of a lab test catalog by its name, ordered by version descending.
     *
     * @param name the name of the lab test catalog
     * @param version the version of the lab test catalog
     * @return list of all versions ordered by version number descending
     */
    @Query("SELECT ltc FROM LabTestCatalog ltc WHERE LOWER(ltc.name) = LOWER(:name) AND ltc.version = :version")
    List<LabTestCatalog> findAllVersionsByNameAndVersion(@Param("name") String name, @Param("version") Integer version);

    /**
     * Retrieves all LabTestCatalog entries.
     *
     * @return list of all LabTestCatalog entries
     */
    @Query("SELECT ltc FROM LabTestCatalog ltc")
    List<LabTestCatalog> findAll();

    /**
     * Retrieves the latest version of each lab test catalog entry, grouped by name.
     *
     * @param pageable the pagination information
     * @return a page of {@link LabTestCatalog} representing the latest version of each entry
     */
    @Query("SELECT l FROM LabTestCatalog l WHERE l.version = " + "(SELECT MAX(l2.version) FROM LabTestCatalog l2 WHERE l2.name = l.name)")
    Page<LabTestCatalog> findLatestVersions(Pageable pageable);

    /**
     * Searches for latest versions of LabTestCatalog entities based on the provided criteria.
     * Only returns the most recent version of each lab test that matches the search criteria.
     *
     * @param name the name of the lab test to search for (can be partial or full name)
     * @param type the type of the lab test to filter by (can be null to ignore)
     * @param method the method of the lab test to filter by (can be null to ignore)
     * @param active the active status to filter by (can be null to ignore)
     * @param pageable the pagination information
     * @return a page of latest versions of {@link LabTestCatalog} matching the search criteria
     */
    @Query(
        "SELECT ltc FROM LabTestCatalog ltc WHERE ltc.version = " +
        "(SELECT MAX(l.version) FROM LabTestCatalog l WHERE l.name = ltc.name)" +
        " AND " +
        "(:name IS NULL OR (ltc.name IS NOT NULL AND LOWER(ltc.name) LIKE LOWER(CONCAT('%', :name, '%')))) AND " +
        "(:type IS NULL OR ltc.type = :type) AND " +
        "(:method IS NULL OR ltc.method = :method) AND " +
        "(:active IS NULL OR ltc.active = :active)"
    )
    Page<LabTestCatalog> searchWithLastVersion(
        @Param("name") String name,
        @Param("type") LabTestType type,
        @Param("method") LabTestMethod method,
        @Param("active") Boolean active,
        Pageable pageable
    );

    /**
     * Retrieves an active {@link LabTestCatalog} entity by its unique identifier.
     * <p>
     * This method queries for a LabTestCatalog with the specified {@code id} and ensures that
     * the entity is marked as active ({@code active = true}).
     *
     * @param id the unique identifier of the LabTestCatalog to retrieve
     * @return an {@link Optional} containing the active LabTestCatalog if found, or empty if not found or inactive
     */
    @Query("SELECT ltc FROM LabTestCatalog ltc WHERE ltc.id = :id AND ltc.active = true")
    Optional<LabTestCatalog> findActiveById(@Param("id") Long id);
}
