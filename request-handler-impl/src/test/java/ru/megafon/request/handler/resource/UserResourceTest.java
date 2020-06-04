package ru.megafon.request.handler.resource;

import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import ru.megafon.request.handler.IntegrationTest;
import ru.megafon.request.handler.data.TestData;
import ru.megafon.request.handler.dto.UserDto;
import ru.megafon.request.handler.repository.RoleRepository;
import ru.megafon.request.handler.repository.UserRepository;

@Sql(scripts = {
        "classpath:database/resource/user.sql",
        "classpath:database/resource/role.sql",
        "classpath:database/resource/user_role.sql"
})
@IntegrationTest
class UserResourceTest {

    private final TestData data = new TestData();

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MapperFacade mapper;

    private String getBasePath() {
        return "http://localhost:" + port + "/api/users";
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getAllWithUserRole() {
        userRepository.saveAll(data.getUsers());
        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .getForEntity(getBasePath(), UserDto[].class);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    void getAllWithAdminRole() {
        userRepository.saveAll(data.getUsers());
        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .getForEntity(getBasePath(), UserDto[].class);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    void getAllWithUnauthorizedRole() {
        userRepository.saveAll(data.getUsers());
        var response = restTemplate.getForEntity(getBasePath(), Object.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getWithUserRole() {
        var expected = userRepository.save(data.getUser());
        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .getForEntity(getBasePath() + "/{email}", UserDto.class, expected.getEmail());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals(expected.getEmail(), response.getBody().getEmail());
        });
    }

    @Test
    void getWithAdminRole() {
        var expected = userRepository.save(data.getUser());
        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .getForEntity(getBasePath() + "/{email}", UserDto.class, expected.getEmail());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals(expected.getEmail(), response.getBody().getEmail());
        });
    }

    @Test
    void getWithUnauthorizedRole() {
        var expected = userRepository.save(data.getUser());
        var response = restTemplate.getForEntity(getBasePath() + "/{email}", UserDto.class, expected.getEmail());

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void saveWithUserRole() {
        var httpEntity = new HttpEntity<>(data.getUserDto(), null);
        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .postForEntity(getBasePath(), httpEntity, Object.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void saveWithAdminRole() {
        var httpEntity = new HttpEntity<>(data.getUserDto(), null);
        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .postForEntity(getBasePath(), httpEntity, Object.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void saveWithUnauthorizedRole() {
        var httpEntity = new HttpEntity<>(data.getUserDto(), null);
        var response = restTemplate.postForEntity(getBasePath(), httpEntity, UserDto.class);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    public void updateForUserRole() {
        var userDto = mapper.map(userRepository.save(data.getUser()), UserDto.class);
        userDto.setPassword("newPassword");
        var httpEntity = new HttpEntity<>(userDto, null);

        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .exchange(getBasePath() + "/{email}", HttpMethod.PUT, httpEntity, UserDto.class, userDto.getEmail());

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void updateForAdminRole() {
        var userDto = mapper.map(userRepository.save(data.getUser()), UserDto.class);
        userDto.setPassword("newPassword");
        var httpEntity = new HttpEntity<>(userDto, null);

        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .exchange(getBasePath() + "/{email}", HttpMethod.PUT, httpEntity, UserDto.class, userDto.getEmail());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    public void deleteWithUserRole() {
        var request = userRepository.save(data.getUser());
        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .exchange(getBasePath() + "/{id}", HttpMethod.DELETE, null, Object.class, request.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            Assertions.assertTrue(userRepository.findById(request.getId()).isPresent());
        });
    }

    @Test
    public void deleteWithAdminRole() {
        var request = userRepository.save(data.getUser());
        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .exchange(getBasePath() + "/{id}", HttpMethod.DELETE, null, Object.class, request.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Assertions.assertTrue(userRepository.findById(request.getId()).isEmpty());
        });
    }

    @Test
    public void deleteWithUnauthorizedRole() {
        var request = userRepository.save(data.getUser());
        var response = restTemplate.exchange(getBasePath() + "/{id}", HttpMethod.DELETE, null, Object.class, request.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            Assertions.assertTrue(userRepository.findById(request.getId()).isPresent());
        });
    }
}