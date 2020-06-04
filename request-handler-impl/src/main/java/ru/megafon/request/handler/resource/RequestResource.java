package ru.megafon.request.handler.resource;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.megafon.request.handler.dto.RequestDto;
import ru.megafon.request.handler.model.Request;
import ru.megafon.request.handler.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/requests")
@RequiredArgsConstructor
public class RequestResource implements RequestResourceApi {

    private final RequestService requestService;
    private final MapperFacade mapper;

    @Override
    public List<RequestDto> getAll() {
        return mapper.mapAsList(requestService.getAll(), RequestDto.class);
    }

    @Override
    public RequestDto get(Long id) {
        return mapper.map(requestService.get(id), RequestDto.class);
    }

    @Override
    public RequestDto save(RequestDto requestDto) {
        var request = mapper.map(requestDto, Request.class);
        request = requestService.save(request);

        return mapper.map(request, RequestDto.class);
    }

    @Override
    public RequestDto update(RequestDto requestDto) {
        var request = mapper.map(requestDto, Request.class);
        request = requestService.update(request);

        return mapper.map(request, RequestDto.class);
    }

    @Override
    public void delete(Long id) {
        requestService.delete(id);
    }
}
