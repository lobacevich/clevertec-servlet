package by.clevertec.lobacevich.runner;

import by.clevertec.lobacevich.data.UserDto;
import by.clevertec.lobacevich.entity.User;
import by.clevertec.lobacevich.mapper.UserMapper;
import by.clevertec.lobacevich.mapper.UserMapperImpl;
import by.clevertec.lobacevich.service.UserService;
import by.clevertec.lobacevich.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

public class Main {

    private static final UserMapper MAPPER = new UserMapperImpl();
    private static final UserService USER_SERVICE = UserServiceImpl.getInstance();

    public static void main(String[] args) throws JsonProcessingException {
        String json1 = "{\"id\":null,\"firstname\":\"Alex\",\"lastname\":\"Murfhy\",\"dateOfBirth\":\"1997-05-17\",\"email\":\"1234@gmail.com\"}";
        String json2 = "{\"id\":null,\"firstname\":\"Nick\",\"lastname\":\"Smith\",\"dateOfBirth\":\"1990-02-27\",\"email\":\"1235@gmail.com\"}";
        String json3 = "{\"id\":1,\"firstname\":\"Mike\",\"lastname\":\"Carter\",\"dateOfBirth\":\"2001-12-06\",\"email\":\"1236@gmail.com\"}";
        String json4 = "{\"id\":2,\"firstname\":\"Bob\",\"lastname\":\"Bond\",\"dateOfBirth\":\"1980-07-02\",\"email\":\"1237@gmail.com\"}";
        sendToCreate(json1);
        sendToCreate(json2);
        sendToUpdate(json3);
        sendToUpdate(json4);
    }

    public static UserDto sendToCreate(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapper();
        UserDto dto = MAPPER.toUserDto(objectMapper.readValue(json, User.class));
        return USER_SERVICE.createUser(dto);
    }

    public static void sendToUpdate(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = getObjectMapper();
        UserDto dto = MAPPER.toUserDto(objectMapper.readValue(json, User.class));
        USER_SERVICE.updateUser(dto);
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
