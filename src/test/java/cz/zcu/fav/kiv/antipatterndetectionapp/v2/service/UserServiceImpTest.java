package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

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
    private final User mockUser = new User("test", "test@tt.cz");

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
        int foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, 0);
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
        int foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, 0);
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
        int foundCode = userService.registerUser(MUser);

        assertEquals(foundCode, 0);
    }

    /**
     * Testing registration implementation
     * To test happy-day scenario where everything is ok
     */
    @Test
    public void whenUserIsNotRegistered_thenRegisterHim() {
        Mockito.when(userRepository.findByName("neni_v_DB")).thenReturn(null);

        int foundCode = userService.registerUser(mockUser);

        assertEquals(foundCode, 1);
    }

    /**
     * Testing registration implementation
     * To test scenario where user want to register which name, that is already taken
     */
    @Test
    public void whenUserIsRegistered_thenSendMSG() {
        Mockito.when(userRepository.findByName(mockUser.getName())).thenReturn(mockUser);

        int foundCode = userService.registerUser(mockUser);

        assertEquals(foundCode, -1);
    }

    /**
     * Testing login implementation
     * for happy-day scenario user is registered
     */
    @Test
    public void whenUserIsRegistered_thenLoggedHim() {

        Mockito.when(userRepository.findByName(mockUser.getName())).thenReturn(mockUser);

        String name = "test";
        int foundCode = userService.loginUser(new User(name, ""));

        assertEquals(foundCode, 1);
    }

    /**
     * Testing login implementation
     * for users name is not registered
     */
    @Test
    public void whenUserIsNotRegistered_thenReturnMSG() {

        //Mockito.when(userRepository.findByName(mockUser.getName())).thenReturn(mockUser);

        String name = "notest";
        int foundCode = userService.loginUser(new User(name, ""));

        assertEquals(foundCode, 0);
    }

}
