package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Medication;
import com.ciatch.gdp.domain.Prescription;
import com.ciatch.gdp.domain.PrescriptionItem;
import com.ciatch.gdp.service.dto.MedicationDTO;
import com.ciatch.gdp.service.dto.PrescriptionDTO;
import com.ciatch.gdp.service.dto.PrescriptionItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrescriptionItem} and its DTO {@link PrescriptionItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrescriptionItemMapper extends EntityMapper<PrescriptionItemDTO, PrescriptionItem> {
    @Mapping(target = "medication", source = "medication", qualifiedByName = "medicationName")
    @Mapping(target = "prescription", source = "prescription", qualifiedByName = "prescriptionId")
    PrescriptionItemDTO toDto(PrescriptionItem s);

    @Named("medicationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MedicationDTO toDtoMedicationName(Medication medication);

    @Named("prescriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrescriptionDTO toDtoPrescriptionId(Prescription prescription);
}
