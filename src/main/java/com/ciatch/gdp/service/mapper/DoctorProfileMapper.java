package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.DoctorProfile;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.service.dto.DoctorProfileDTO;
import com.ciatch.gdp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DoctorProfile} and its DTO {@link DoctorProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface DoctorProfileMapper extends EntityMapper<DoctorProfileDTO, DoctorProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    DoctorProfileDTO toDto(DoctorProfile s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
