package demo.security.services;

import demo.security.entities.User;
import demo.security.entities.dto.UserDTO;
import demo.security.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    private static UserService service;
    private static UserRepository repository;

    @BeforeAll
    public static void beforeAll(@Autowired UserService userService, @Autowired UserRepository userRepository) {
        service = userService;
        repository = userRepository;
    }

    @Test
    public void testChangingColor() {
        UserDTO userDTO = new UserDTO("username1", "password1");
        // registration testing in AuthenticationTest
        service.register(userDTO);

        Optional<User> optional = repository.findByUsername("username1");
        assertTrue(optional.isPresent(), "registration error");

        User user = optional.get();

        String newColor = "#ff0000";
        service.changeColor(user, newColor);

        String actualColor = repository.findByUsername("username1").get().getColor();

        assertEquals(newColor, actualColor);
    }

}
