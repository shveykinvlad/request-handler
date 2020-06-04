package ru.megafon.request.handler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.megafon.request.handler.exception.UserAlreadyExistException;
import ru.megafon.request.handler.model.User;
import ru.megafon.request.handler.repository.RoleRepository;
import ru.megafon.request.handler.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        if (emailExist(user.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleRepository.findByName("USER"));

        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User get(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email = " + email + " not found"));
    }

    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
