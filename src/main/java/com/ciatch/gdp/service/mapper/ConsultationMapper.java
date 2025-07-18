package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Consultation;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.Prescription;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.service.dto.ConsultationDTO;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.dto.PrescriptionDTO;
import com.ciatch.gdp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consultation} and its DTO {@link ConsultationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsultationMapper extends EntityMapper<ConsultationDTO, Consultation> {
    @Mapping(target = "prescription", source = "prescription", qualifiedByName = "prescriptionId")
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "userLogin")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientFirstName")
    ConsultationDTO toDto(Consultation s);

    @Named("prescriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrescriptionDTO toDtoPrescriptionId(Prescription prescription);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("patientFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    PatientDTO toDtoPatientFirstName(Patient patient);
}
