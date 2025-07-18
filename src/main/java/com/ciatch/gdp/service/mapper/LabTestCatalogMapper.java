package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.service.dto.LabTestCatalogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LabTestCatalog} and its DTO {@link LabTestCatalogDTO}.
 */
@Mapper(componentModel = "spring")
public interface LabTestCatalogMapper extends EntityMapper<LabTestCatalogDTO, LabTestCatalog> {}
