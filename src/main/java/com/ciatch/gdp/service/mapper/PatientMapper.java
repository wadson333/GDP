package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Patient;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.service.dto.PatientDTO;
import com.ciatch.gdp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Patient} and its DTO {@link PatientDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    // @Mapping(target = "user", source = "user.email", qualifiedByName = "email")
    // @Mapping(target = "user", source = "user.imageUrl", qualifiedByName = "imageUrl")
    PatientDTO toDto(Patient s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    // @Mapping(target = "email", source = "email")
    // @Mapping(target = "imageUrl", source = "imageUrl")
    UserDTO toDtoUserId(User user);
}
