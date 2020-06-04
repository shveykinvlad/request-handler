package ru.megafon.request.handler.data;

import ru.megafon.request.handler.dto.RequestDto;
import ru.megafon.request.handler.dto.UserDto;
import ru.megafon.request.handler.model.Request;
import ru.megafon.request.handler.model.User;

import java.util.Arrays;
import java.util.List;

public class TestData {

    public RequestDto getRequestDto() {
        return new RequestDto()
                .setText("Text1")
                .setTitle("Title1");
    }

    public Request getRequest() {
        return new Request()
                .setText("Text1")
                .setTitle("Title1");
    }

    public List<Request> getRequests() {
        return Arrays.asList(
                new Request()
                        .setText("Text1")
                        .setTitle("Title1"),
                new Request()
                        .setText("Text2")
                        .setTitle("Title2"),
                new Request()
                        .setText("Text2")
                        .setTitle("Title2"));
    }

    public UserDto getUserDto() {
        return new UserDto()
                .setEmail("user1@user.com")
                .setPassword("$2a$10$ARVD4bLsvXNXBP3orqacjOojT6XbyXLlD6PhVbkGeiNNd73dyS");
    }

    public User getUser() {
        return new User()
                .setEmail("user1@user.com")
                .setPassword("$2a$10$ARVD4bLsvXNXBP3orqacjOojT6XbyXLlD6PhVbkGeiNNd73dyS");
    }

    public List<User> getUsers() {
        return Arrays.asList(
                new User()
                        .setEmail("user1@user.com")
                        .setPassword("password"),
                new User()
                        .setEmail("user2@user.com")
                        .setPassword("password"),
                new User()
                        .setEmail("user3@user.com")
                        .setPassword("password"));
    }
}
