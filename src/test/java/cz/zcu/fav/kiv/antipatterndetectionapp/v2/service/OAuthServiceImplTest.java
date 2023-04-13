package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OAuthServiceImplTest {

    @Autowired
    private OAuthService oAuthService;

    @MockBean
    private RequestBuilder requestBuilder;

    /**
     * Mocked User
     */
    private final User mockUser = new User("foo", "foo@foo.cz", "foo");
    
    @Test
    public void loginValidUser(){
        ResponseEntity<String> response = ResponseEntity.ok().body("eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyMGU4NmJiMC1lMDU4LTQwYTMtYjQyZC02ZTBjOWIyMmQ5MWQiL" +
                "CJzdWIiOiJmb28iLCJpYXQiOjE2ODE0MTY2ODAsImV4cCI6MTY4MTQxNjk4MH0.YJTwPEI4njQqYRuLGilf_oVl0gGD5BWFmdolk1O" +
                "vYWIgTcoIAFwh6bwqrW7XM6Hlj-ItYj9EmihYIqN5gfJVtg");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", mockUser.getName());
//        when(requestBuilder.sendRequestResponse(anyString(), anyCollection())).thenReturn(response);
//        try(MockedStatic<RequestBuilder> requestBuilderMockedStatic = Mockito.mockStatic(RequestBuilder.class)){
//                requestBuilderMockedStatic.when(() -> RequestBuilder.sendRequestResponse(anyString(), mockHashMap)).thenReturn(response);
//        }

        ResponseEntity<String> response1 = oAuthService.loginUser(mockUser);

        assertEquals(response, response1);

    }


    /*    public ResponseEntity<String> loginUser(User user) {
        final String userName = user.getName();

        if(userName == null) {
            return null;
        }
        //HttpURLConnection con = RequestBuilder.createConnection(AUTH_URL);
        HashMap<String, String> requestBody = new HashMap<>();

        requestBody.put("name", userName);

        return RequestBuilder.sendRequestResponse(AUTH_URL_LOGIN, requestBody);
    }
     */









}
