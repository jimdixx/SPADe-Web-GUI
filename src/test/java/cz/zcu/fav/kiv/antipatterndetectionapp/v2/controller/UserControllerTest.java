package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.UserService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class)
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void registerUserNew() throws Exception {
        Mockito.when(userService.registerUser(any())).thenReturn(UserModelStatusCodes.USER_CREATED);
        HashMap<String,String> map = new HashMap<>();
        map.put("name","pepa");
        map.put("password","ahojSvete");
        map.put("email","yxz@ahoj.cz");
        String json = JSONBuilder.buildJson(map);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v2/user/register")
                .accept(MediaType.APPLICATION_JSON).content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }
    @Test
    public void registerUserInvalidArguments() throws Exception {
        Mockito.when(userService.registerUser(any())).thenReturn(UserModelStatusCodes.INVALID_USER_ARGUMENTS);
        HashMap<String,String> map = new HashMap<>();
        map.put("name","pepa");
        map.put("password","ahojSvete");
        map.put("email","yxz");
        String json = JSONBuilder.buildJson(map);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v2/user/register")
                .accept(MediaType.APPLICATION_JSON).content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }



    @Test
    public void registerUserExists() throws Exception {
        Mockito.when(userService.registerUser(any())).thenReturn(UserModelStatusCodes.USER_EXISTS);
        HashMap<String,String> map = new HashMap<>();
        map.put("name","pepa");
        map.put("password","ahojSvete");
        map.put("email","yxz@ahoj.cz");
        String json = JSONBuilder.buildJson(map);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v2/user/register")
                .accept(MediaType.APPLICATION_JSON).content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void loginValidUser() throws Exception {
        Mockito.when(userService.loginUser(any())).thenReturn(UserModelStatusCodes.USER_LOGGED_IN);
        HashMap<String,String> map = new HashMap<>();
        map.put("name","pepa");
        map.put("password","ahojSvete");
        map.put("email","yxz@ahoj.cz");
        String json = JSONBuilder.buildJson(map);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
              .post("/v2/user/login")
              .accept(MediaType.APPLICATION_JSON).content(json)
              .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void loginInValidUser() throws Exception {
        Mockito.when(userService.loginUser(any())).thenReturn(UserModelStatusCodes.USER_LOGIN_FAILED);
        HashMap<String,String> map = new HashMap<>();
        map.put("name","pepa");
        map.put("password","ahojSvete");
        map.put("email","yxz@ahoj.cz");
        String json = JSONBuilder.buildJson(map);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v2/user/login")
                .accept(MediaType.APPLICATION_JSON).content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void logoutUser() throws Exception {
        Mockito.when(userService.logoutUser(any())).thenReturn(UserModelStatusCodes.USER_LOGGED_OUT);
        HashMap<String,String> map = new HashMap<>();
        map.put("name","pepa");
        map.put("password","ahojSvete");
        map.put("email","yxz");
        String json = JSONBuilder.buildJson(map);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v2/user/logout")
                .accept(MediaType.APPLICATION_JSON).content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }



}