package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Medication;
import com.ciatch.gdp.service.dto.MedicationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medication} and its DTO {@link MedicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicationMapper extends EntityMapper<MedicationDTO, Medication> {}
