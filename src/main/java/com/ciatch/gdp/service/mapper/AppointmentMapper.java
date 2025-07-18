package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Appointment;
import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.service.dto.AppointmentDTO;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientFirstName")
    @Mapping(target = "doctor", source = "doctor", qualifiedByName = "userLogin")
    AppointmentDTO toDto(Appointment s);

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
