package by.clevertec.lobacevich.mapper;

import by.clevertec.lobacevich.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface UserObjectMapper {

    UserDto toUserDto(String json) throws JsonProcessingException;

    String toJson(UserDto userDto) throws JsonProcessingException;

    String listToJson(List<UserDto> allUserDto) throws JsonProcessingException;
}
