package de.lbakker77.retracker.main.user.usecase.mapper;

import de.lbakker77.retracker.main.user.entity.model.User;
import de.lbakker77.retracker.main.user.usecase.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING  )
public interface UserMapper {

    UserDto userToUserDto(User user);
}
