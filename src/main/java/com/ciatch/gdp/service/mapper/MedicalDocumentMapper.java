package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.MedicalDocument;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.service.dto.MedicalDocumentDTO;
import com.ciatch.gdp.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalDocument} and its DTO {@link MedicalDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalDocumentMapper extends EntityMapper<MedicalDocumentDTO, MedicalDocument> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientFirstName")
    MedicalDocumentDTO toDto(MedicalDocument s);

    @Named("patientFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    PatientDTO toDtoPatientFirstName(Patient patient);
}
