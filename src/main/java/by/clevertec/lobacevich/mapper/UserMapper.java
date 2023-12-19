package by.clevertec.lobacevich.mapper;

import by.clevertec.lobacevich.data.UserDto;
import by.clevertec.lobacevich.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
