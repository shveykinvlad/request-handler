package ru.megafon.request.handler.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.megafon.request.handler.dto.UserDto;

import java.util.List;

public interface UserResourceApi {

    @GetMapping
    List<UserDto> getAll();

    @GetMapping("/{email}")
    UserDto get(@PathVariable("email") String email);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto save(@RequestBody UserDto userDto);

    @PutMapping("/{id}")
    UserDto update(@RequestBody UserDto userDto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") Long id);
}
