package ru.megafon.request.handler.resource;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.megafon.request.handler.dto.UserDto;
import ru.megafon.request.handler.model.User;
import ru.megafon.request.handler.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserResource implements UserResourceApi {

    private final UserService userService;
    private final MapperFacade mapper;

    @Override
    public List<UserDto> getAll() {
        return mapper.mapAsList(userService.getAll(), UserDto.class);
    }

    @Override
    public UserDto get(String email) {
        return mapper.map(userService.get(email), UserDto.class);
    }

    @Override
    public UserDto save(UserDto userDto) {
        var user = mapper.map(userDto, User.class);
        user = userService.register(user);

        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto userDto) {
        var user = mapper.map(userDto, User.class);
        user = userService.update(user);

        return mapper.map(user, UserDto.class);
    }

    @Override
    public void delete(Long id) {
        userService.delete(id);
    }
}
