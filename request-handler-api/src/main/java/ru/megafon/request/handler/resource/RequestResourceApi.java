package ru.megafon.request.handler.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.megafon.request.handler.dto.RequestDto;

import java.security.Principal;
import java.util.List;

public interface RequestResourceApi {

    @GetMapping
    List<RequestDto> getAll();

    @GetMapping("/{id}")
    RequestDto get(@PathVariable("id") Long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RequestDto save(@RequestBody RequestDto requestDto);

    @PutMapping("/{id}")
    RequestDto update(@RequestBody RequestDto requestDto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") Long id);
}
