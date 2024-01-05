package by.clevertec.lobacevich.mapper.impl;

import by.clevertec.lobacevich.dto.UserDto;
import by.clevertec.lobacevich.mapper.UserObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class ObjectMapperJson implements UserObjectMapper {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public UserDto toUserDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, UserDto.class);
    }

    @Override
    public String toJson(UserDto userDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(userDto);
    }

    @Override
    public String listToJson(List<UserDto> allUserDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(allUserDto);
    }
}
