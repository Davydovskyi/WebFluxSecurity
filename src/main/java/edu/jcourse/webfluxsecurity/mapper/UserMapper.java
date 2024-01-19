package edu.jcourse.webfluxsecurity.mapper;

import edu.jcourse.webfluxsecurity.dto.UserDto;
import edu.jcourse.webfluxsecurity.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto entityToDto(User user);

    @InheritInverseConfiguration
    User dtoToEntity(UserDto userDto);
}