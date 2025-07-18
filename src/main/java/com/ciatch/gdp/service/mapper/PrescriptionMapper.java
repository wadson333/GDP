package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Prescription;
import com.ciatch.gdp.service.dto.PrescriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Prescription} and its DTO {@link PrescriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrescriptionMapper extends EntityMapper<PrescriptionDTO, Prescription> {}
