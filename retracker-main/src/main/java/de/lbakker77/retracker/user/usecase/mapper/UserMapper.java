package de.lbakker77.retracker.user.usecase.mapper;

import de.lbakker77.retracker.user.entity.model.User;
import de.lbakker77.retracker.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING  )
public interface UserMapper {

    UserDto userToUserDto(User user);
}
