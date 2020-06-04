package ru.megafon.request.handler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.megafon.request.handler.model.Request;
import ru.megafon.request.handler.repository.RequestRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    @PreAuthorize(value = "isAuthenticated()")
    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    @PreAuthorize(value = "isAuthenticated()")
    public Request get(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request with id = " + id + " not found"));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public Request save(Request request) {
        return requestRepository.save(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Request update(Request request) {
        return requestRepository.save(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(Long id) {
        requestRepository.deleteById(id);
    }
}
