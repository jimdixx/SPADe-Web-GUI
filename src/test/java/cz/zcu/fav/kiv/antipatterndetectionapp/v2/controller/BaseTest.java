package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.DetectionService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
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
    private AntiPatternRepository antiPatternRepository;

    @MockBean
    private ConfigRepository configurationRepository;

    @BeforeEach
    public void init() {
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

        /* Logout of user */
        Map<String,Object> logoutJson = new HashMap<>();
        logoutJson.put("message", "User logged out");
        String logoutBody = JSONBuilder.buildJSON(logoutJson);
        ResponseEntity<String> logoutResponse = ResponseEntity.ok(logoutBody);
        when(requestBuilder.sendRequestResponse(eq("http://localhost:8081/logout"), anyMap())).thenReturn(logoutResponse);

        /* Configuration */
        /* /configuration_name */
        User config_user = new User("config_user");
        config_user.setId(1);
        when(userRepository.findByName("config_user")).thenReturn(config_user);
        List<Object[]> configs = new ArrayList<>();
        Object[] pom_pole = new Object[2];
        pom_pole[0] = "default config";
        pom_pole[1] = 1;
        configs.add(pom_pole);
        when(configurationRepository.getAllUserConfigurationNames(config_user.getId())).thenReturn(configs);

        /* configuration */
        List<Configuration> configuration = new ArrayList<>();
        Configuration con = new Configuration("{\r\n    \"configuration\": [\r\n        {\r\n            \"antiPattern\": \"TooLongSprint\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxIterationLength\",\r\n                    \"value\": \"21\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxNumberOfTooLongIterations\",\r\n                    \"value\": \"0\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"VaryingSprintLength\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxDaysDifference\",\r\n                    \"value\": \"7\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxIterationChanged\",\r\n                    \"value\": \"1\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"BusinessAsUsual\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"divisionOfIterationsWithRetrospective\",\r\n                    \"value\": \"66.66f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithRetrospective\",\r\n                    \"value\": \"%retr%||%revi%||%week%scrum%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"SpecifyNothing\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"minNumberOfWikiPagesWithSpecification\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minNumberOfActivitiesWithSpecification\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minAvgLengthOfActivityDescription\",\r\n                    \"value\": \"150\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithProjectSpecification\",\r\n                    \"value\": \"%dsp%||%specifikace%||%specification%||%vize%proj%||%vize%produ%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"RoadToNowhere\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"minNumberOfWikiPagesWithProjectPlan\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minNumberOfActivitiesWithProjectPlan\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithProjectPlan\",\r\n                    \"value\": \"%pl?n projektu%||%project plan%||%plan project%||%projektov? pl?n%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"LongOrNonExistentFeedbackLoops\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"divisionOfIterationsWithFeedbackLoop\",\r\n                    \"value\": \"50.00f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxGapBetweenFeedbackLoopRate\",\r\n                    \"value\": \"2f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithFeedbackLoop\",\r\n                    \"value\": \"%sch?z%z?kazn?k%||%p?edveden?%z?kazn?k%||%z?kazn%demo%||%sch?z%zadavat%||%inform%sch?z%||%z?kazn%||%zadavatel%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"NinetyNinetyRule\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxDivisionRange\",\r\n                    \"value\": \"1.25f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxBadDivisionLimit\",\r\n                    \"value\": \"2\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"UnknownPoster\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsInvalidNames\",\r\n                    \"value\": \"%unknown%||%anonym%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"BystanderApathy\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsInvalidContributors\",\r\n                    \"value\": \"%dependabot%\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maximumPercentageOfTasksWithoutTeamwork\",\r\n                    \"value\": \"30f\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"YetAnotherProgrammer\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxNumberOfNewContributors\",\r\n                    \"value\": \"5\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"numberOfFirstMonthsWithoutDetection\",\r\n                    \"value\": \"2\"\r\n                }\r\n            ]\r\n        }\r\n    ]\r\n}"
        , "Y", "default_config");
        configuration.add(con);
        when(configurationRepository.getAllUserConfigurations(config_user.getId())).thenReturn(configuration);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

}
