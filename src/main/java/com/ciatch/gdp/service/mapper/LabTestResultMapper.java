package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Consultation;
import com.ciatch.gdp.domain.LabTestCatalog;
import com.ciatch.gdp.domain.LabTestResult;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.service.dto.ConsultationDTO;
import com.ciatch.gdp.service.dto.LabTestCatalogDTO;
import com.ciatch.gdp.service.dto.LabTestResultDTO;
import com.ciatch.gdp.service.dto.PatientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LabTestResult} and its DTO {@link LabTestResultDTO}.
 */
@Mapper(componentModel = "spring")
public interface LabTestResultMapper extends EntityMapper<LabTestResultDTO, LabTestResult> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientFirstName")
    @Mapping(target = "labTest", source = "labTest", qualifiedByName = "labTestCatalogName")
    @Mapping(target = "consultation", source = "consultation", qualifiedByName = "consultationId")
    LabTestResultDTO toDto(LabTestResult s);

    @Named("patientFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    PatientDTO toDtoPatientFirstName(Patient patient);

    @Named("labTestCatalogName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LabTestCatalogDTO toDtoLabTestCatalogName(LabTestCatalog labTestCatalog);

    @Named("consultationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConsultationDTO toDtoConsultationId(Consultation consultation);
}
