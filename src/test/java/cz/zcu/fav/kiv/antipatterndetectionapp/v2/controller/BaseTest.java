package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectServiceImpl;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.app.Metadata;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.app.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.app.AboutPageRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.app.UserRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.JSONBuilder;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.RequestBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest()
public abstract class BaseTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RequestBuilder requestBuilder;

    @MockBean
    private ProjectServiceImpl projectService;

    @MockBean
    private AboutPageRepository aboutPageRepository;

    @BeforeEach
    public void init() {

        setupUserControllerMocks();
        setupFilterMocks();
        setupAboutControllerMocks();

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    private void setupUserControllerMocks() {
        User user = new User("foo", "foo@foo.foo", "76D3BC41C9F588F7FCD0D5BF4718F8F84B1C41B20882703100B9EB9413807C01");
        User userWrongPassword = new User("foo", "foo@foo.foo", "foo");
        ResponseEntity<String> loginResponse = ResponseEntity.ok("token");

        /* Registration of user */
        when(userRepository.findByName("new_user")).thenReturn(null);
        when(userRepository.findByName("existing")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        /* Login of user */
        when(userRepository.findByName("existing")).thenReturn(user);
        when(requestBuilder.sendRequestResponse(eq("http://localhost:8081/login"), anyMap())).thenReturn(loginResponse);
        when(userRepository.findByName("non_existing")).thenReturn(null);
        when(userRepository.findByName("foo")).thenReturn(userWrongPassword);

        /* Logout of user */
        Map<String,Object> logoutJson = new HashMap<>();
        logoutJson.put("message", "User logged out");
        String logoutBody = JSONBuilder.buildJSON(logoutJson);
        ResponseEntity<String> logoutResponse = ResponseEntity.ok(logoutBody);
        when(requestBuilder.sendRequestResponse(eq("http://localhost:8081/logout"), anyMap())).thenReturn(logoutResponse);
    }

    private void setupFilterMocks() {
        /* Filter mock */
        Map<String,Object> filterJson = new HashMap<>();
        filterJson.put("message", "User authorized");
        String filterBody = JSONBuilder.buildJSON(filterJson);
        ResponseEntity<String> oauth = ResponseEntity.ok(filterBody);
        when(requestBuilder.sendRequestResponse(anyString(), eq("token"))).thenReturn(oauth);

        Map<String,Object> filterJson2 = new HashMap<>();
        filterJson2.put("message", "User unauthorized");
        String filterBody2 = JSONBuilder.buildJSON(filterJson2);
        ResponseEntity<String> oauth2 = ResponseEntity.status(500).body(filterBody2);
        when(requestBuilder.sendRequestResponse(anyString(), eq("wrong"))).thenReturn(oauth2);
    }

    private void setupAboutControllerMocks() {
        List<Metadata> metadata = new ArrayList<>();
        Metadata met = new Metadata();
        met.setAppDataValue("[ { \"version\": \"1.0.0\", \"authors\": [ { \"name\": \"Ondrej Vane\", \"email\": \"vaneo@students.zcu.cz\"}], \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.\"}, { \"version\": \"1.1.0\", \"authors\": [ { \"name\": \"Ondrej Vane\", \"email\": \"vaneo@students.zcu.cz\"}], \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.\"}, { \"version\": \"1.2.0\", \"authors\": [ { \"name\": \"Petr Stepanek\", \"email\": \"petrs1@students.zcu.cz\"}], \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Ten selected anti-patterns are implemented in this application.\"}, { \"version\": \"2.0.0\", \"authors\": [ { \"name\": \"Petr Stepanek\", \"email\": \"petrs1@students.zcu.cz\"}, { \"name\": \"Petr Urban\", \"email\": \"urbanp@students.zcu.cz\"}, { \"name\": \"Jiri Trefil\", \"email\": \"trefil@students.zcu.cz\"}, { \"name\": \"Vaclav Hrabik\", \"email\": \"hrabikv@students.zcu.cz\"}], \"description\": \"TODO\"}]");
        met.setId(1L);
        met.setAppDataKey("basics");
        metadata.add(met);
        when(aboutPageRepository.findAll()).thenReturn(metadata);
    }

}
