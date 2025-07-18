package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Hospitalization;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.service.dto.HospitalizationDTO;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hospitalization} and its DTO {@link HospitalizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface HospitalizationMapper extends EntityMapper<HospitalizationDTO, Hospitalization> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientFirstName")
    @Mapping(target = "attendingDoctor", source = "attendingDoctor", qualifiedByName = "userLogin")
    HospitalizationDTO toDto(Hospitalization s);

    @Named("patientFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    PatientDTO toDtoPatientFirstName(Patient patient);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
