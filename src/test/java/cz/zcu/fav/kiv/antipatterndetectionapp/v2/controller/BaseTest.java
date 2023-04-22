package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.AntiPatternDetectionAppApplication;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AntiPatternDetectionAppApplication.class)
public abstract class BaseTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private UserRepository userRepository;



    @Before
    public void init() {
        User user = new User("foo", "foo@foo.foo", "foo");

        when(userRepository.findByName("non-existing")).thenReturn(null);
        when(userRepository.findByName("foo")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

}
