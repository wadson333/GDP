package com.ciatch.gdp.repository;

import com.ciatch.gdp.domain.UserConfiguration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserConfiguration entity.
 */
@Repository
public interface UserConfigurationRepository extends JpaRepository<UserConfiguration, Long> {
    default Optional<UserConfiguration> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserConfiguration> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserConfiguration> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userConfiguration from UserConfiguration userConfiguration left join fetch userConfiguration.user",
        countQuery = "select count(userConfiguration) from UserConfiguration userConfiguration"
    )
    Page<UserConfiguration> findAllWithToOneRelationships(Pageable pageable);

    @Query("select userConfiguration from UserConfiguration userConfiguration left join fetch userConfiguration.user")
    List<UserConfiguration> findAllWithToOneRelationships();

    @Query(
        "select userConfiguration from UserConfiguration userConfiguration left join fetch userConfiguration.user where userConfiguration.id =:id"
    )
    Optional<UserConfiguration> findOneWithToOneRelationships(@Param("id") Long id);
}
