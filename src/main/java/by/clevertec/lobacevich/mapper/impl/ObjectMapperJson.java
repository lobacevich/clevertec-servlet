package by.clevertec.lobacevich.mapper.impl;

import by.clevertec.lobacevich.dto.UserDto;
import by.clevertec.lobacevich.mapper.UserObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.List;

public class ObjectMapperJson implements UserObjectMapper {

    @Getter
    private static final ObjectMapperJson INSTANCE = new ObjectMapperJson();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ObjectMapperJson() {
        setObjectMapper();
    }

    private void setObjectMapper() {
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
