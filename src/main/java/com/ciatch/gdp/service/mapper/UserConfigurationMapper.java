package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.domain.UserConfiguration;
import com.ciatch.gdp.service.dto.UserConfigurationDTO;
import com.ciatch.gdp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserConfiguration} and its DTO {@link UserConfigurationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserConfigurationMapper extends EntityMapper<UserConfigurationDTO, UserConfiguration> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UserConfigurationDTO toDto(UserConfiguration s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
