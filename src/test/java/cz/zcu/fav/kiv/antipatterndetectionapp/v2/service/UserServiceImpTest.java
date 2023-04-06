package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.dials.UserModelStatusCodes;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 * Tests for userServiceImplementation
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserServiceImpl.class)
@WithMockUser
public class UserServiceImpTest {

    @TestConfiguration
    static class UserServiceImlpTestConfig{

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    /**
     * UserService
     */
    @Autowired
    private UserService userService;



    /**
     * Mocked userRepository
     */
    @MockBean
    private UserRepository userRepository;

    /**
     * Mocked User
     */
    private final User mockUser = new User("test", "test@tt.cz", "Ahoj99");

    /**
     * Testing registration implementation
     * To test scenario where user want register which name, with more than 255 chars
     */
    @Test
    public void whenUserNameIsInvalid_thenReturnMSG() {
        User MUser = new User("asdasdasdasdasdasdasdafjsdjbfsdfbsdkncbsdcsdlkjdsbvjkhsbdvjhbxcvbjxcnvbxbjhdsbfjs" +
                "fsjdkdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddhfbsdjfhbsdjhbjckvxjhbvjhfbsjhbdjhsbvhbsckjhvbsdhfbsdjhvbjxhcbvhbjhvbxcv", "tt@aa.ct");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    /**
     * Testing registration implementation
     * To test scenario where user want register which email, with more than 255 chars
     */
    @Test
    public void whenUserEmailIsInvalid_thenReturnMSG() {
        User MUser = new User("Petr", "tasdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddt@aa.ct");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    /**
     * Testing registration implementation
     * To test scenario where user want register which name and email, with more than 255 chars
     */
    @Test
    public void whenUserNameAndEmailIsInvalid_thenReturnMSG() {
        User MUser = new User("asdasdasdasdasdasdasdafjsdjbfsdfbsdkncbsdcsdlkjdsbvjkhsbdvjhbxcvbjxcnvbxbjhdsbfjs" +
                "fsjdkdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddhfbsdjfhbsdjhbjckvxjhbvjhfbsjhbdjhsbvhbsckjhvbsdhfbsdjhvbjxhcbvhbjhvbxcv", "tasddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddt@aa.ct");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    @Test
    public void whenUserNULLName_thenReturnMSG() {
        User MUser = new User(null, "tt@asd.cf", "Ahoj99");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    @Test
    public void whenUserNULLEmail_thenReturnMSG() {
        User MUser = new User("tt", null, "Ahoj99");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    @Test
    public void whenUserNULLPassword_thenReturnMSG() {
        User MUser = new User("tt", "tt@asd.cf", null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    @Test
    public void whenUserNULLNameEmail_thenReturnMSG() {
        User MUser = new User(null, null, "Ahoj99");
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    @Test
    public void whenUserNULLNamePassword_thenReturnMSG() {
        User MUser = new User(null, "tt@asd.cf", null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    @Test
    public void whenUserNULLEmailPassword_thenReturnMSG() {
        User MUser = new User("tt", null, null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    @Test
    public void whenUserNULLNameEmailPassword_thenReturnMSG() {
        User MUser = new User(null, null, null);
        UserModelStatusCodes foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, UserModelStatusCodes.INVALID_USER_ARGUMENTS);
    }

    /**
     * Testing registration implementation
     * To test happy-day scenario where everything is ok
     */
    @Test
    public void whenUserIsNotRegistered_thenRegisterHim() {
        Mockito.when(userRepository.save(any())).thenReturn(mockUser);

        UserModelStatusCodes foundCode = userService.registerUser(mockUser);

        assertEquals(foundCode, UserModelStatusCodes.USER_CREATED);
    }

    /**
     * Testing registration implementation
     * To test scenario where user want to register which name, that is already taken
     */
    @Test
    public void whenUserIsRegistered_thenSendMSG() {
        Mockito.when(userRepository.findByName(mockUser.getName())).thenReturn(mockUser);

        UserModelStatusCodes foundCode = userService.registerUser(mockUser);

        assertEquals(foundCode, UserModelStatusCodes.USER_EXISTS);
    }

    /**
     * Testing login implementation
     * for happy-day scenario user is registered
     */
    @Test
    public void whenUserIsRegistered_thenLoggedHim() {

        Mockito.when(userRepository.findByName(mockUser.getName())).thenReturn(mockUser);

        String name = "test";
        UserModelStatusCodes foundCode = userService.loginUser(new User(name, ""));

        assertEquals(foundCode, UserModelStatusCodes.USER_LOGGED_IN);
    }

    /**
     * Testing login implementation
     * for users name is not registered
     */
    @Test
    public void whenUserIsNotRegistered_thenReturnMSG() {

        //Mockito.when(userRepository.findByName(mockUser.getName())).thenReturn(mockUser);

        String name = "notest";
        UserModelStatusCodes foundCode = userService.loginUser(new User(name, ""));

        assertEquals(foundCode, UserModelStatusCodes.USER_LOGIN_FAILED);
    }

}
