package com.ciatch.gdp.service.mapper;

import com.ciatch.gdp.domain.Notification;
import com.ciatch.gdp.domain.User;
import com.ciatch.gdp.service.dto.NotificationDTO;
import com.ciatch.gdp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "targetUser", source = "targetUser", qualifiedByName = "userLogin")
    NotificationDTO toDto(Notification s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
