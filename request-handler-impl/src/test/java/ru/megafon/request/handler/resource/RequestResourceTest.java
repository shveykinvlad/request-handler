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
import ru.megafon.request.handler.dto.RequestDto;
import ru.megafon.request.handler.repository.RequestRepository;
import ru.megafon.request.handler.repository.RoleRepository;
import ru.megafon.request.handler.repository.UserRepository;

@Sql(scripts = {
        "classpath:database/resource/user.sql",
        "classpath:database/resource/role.sql",
        "classpath:database/resource/user_role.sql"
})
@IntegrationTest
public class RequestResourceTest {

    private final TestData data = new TestData();

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MapperFacade mapper;

    private String getBasePath() {
        return "http://localhost:" + port + "/api/requests/";
    }

    @AfterEach
    void tearDown() {
        requestRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void getAllWithUserRole() {
        var expected = requestRepository.saveAll(data.getRequests());
        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .getForEntity(getBasePath(), RequestDto[].class);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals(expected.size(), response.getBody().length);
        });
    }

    @Test
    public void getAllWithAdminRole() {
        var expected = requestRepository.saveAll(data.getRequests());
        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .getForEntity(getBasePath(), RequestDto[].class);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals(expected.size(), response.getBody().length);
        });
    }

    @Test
    public void getAllWithUnauthorizedRole() {
        requestRepository.saveAll(data.getRequests());
        var response = restTemplate.getForEntity(getBasePath(), Object.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getWithUserRole() {
        var expected = requestRepository.save(data.getRequest());
        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .getForEntity(getBasePath() + "/{id}", RequestDto.class, expected.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals(expected.getId(), response.getBody().getId());
        });
    }

    @Test
    public void getWithAdminRole() {
        var expected = requestRepository.save(data.getRequest());
        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .getForEntity(getBasePath() + "/{id}", RequestDto.class, expected.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals(expected.getId(), response.getBody().getId());
        });
    }

    @Test
    public void getWithUnauthorizedRole() {
        var expected = requestRepository.save(data.getRequest());
        var response = restTemplate.getForEntity(getBasePath() + "/{id}", Object.class, expected.getId());

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void saveWithUserRole() {
        var httpEntity = new HttpEntity<>(data.getRequestDto(), null);

        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .postForEntity(getBasePath(), httpEntity, RequestDto.class);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    public void saveWithAdminRole() {
        var httpEntity = new HttpEntity<>(data.getRequestDto(), null);

        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .postForEntity(getBasePath(), httpEntity, RequestDto.class);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
        });
    }

    @Test
    public void updateForUserRole() {
        var requestDto = mapper.map(requestRepository.save(data.getRequest()), RequestDto.class);
        requestDto.setText("updated text");
        var httpEntity = new HttpEntity<>(requestDto, null);

        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .exchange(getBasePath() + "/{id}", HttpMethod.PUT, httpEntity, RequestDto.class, requestDto.getId());

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void updateForAdminRole() {
        var requestDto = mapper.map(requestRepository.save(data.getRequest()), RequestDto.class);
        requestDto.setText("updated text");
        var httpEntity = new HttpEntity<>(requestDto, null);

        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .exchange(getBasePath() + "/{id}", HttpMethod.PUT, httpEntity, RequestDto.class, requestDto.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertEquals(requestDto.getText(), response.getBody().getText());
        });
    }

    @Test
    public void deleteWithUserRole() {
        var request = requestRepository.save(data.getRequest());
        var response = restTemplate.withBasicAuth("user@user.com", "password")
                .exchange(getBasePath() + "/{id}", HttpMethod.DELETE, null, Object.class, request.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            Assertions.assertTrue(requestRepository.findById(request.getId()).isPresent());
        });
    }

    @Test
    public void deleteWithAdminRole() {
        var request = requestRepository.save(data.getRequest());
        var response = restTemplate.withBasicAuth("admin@admin.com", "password")
                .exchange(getBasePath() + "/{id}", HttpMethod.DELETE, null, Object.class, request.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Assertions.assertTrue(requestRepository.findById(request.getId()).isEmpty());
        });
    }

    @Test
    public void deleteWithUnauthorizedRole() {
        var request = requestRepository.save(data.getRequest());
        var response = restTemplate.exchange(getBasePath() + "/{id}", HttpMethod.DELETE, null, Object.class, request.getId());

        Assertions.assertAll(() -> {
            Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            Assertions.assertTrue(requestRepository.findById(request.getId()).isPresent());
        });
    }
}