package demo.security;

import demo.security.entities.User;
import demo.security.entities.dto.UserDTO;
import demo.security.repositories.UserRepository;
import demo.security.security.UserDetailsImpl;
import demo.security.services.UserService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService service;

    private static UserDetails userDetails;
    private static UserDetails adminUserDetails;
    private static UserDetails userToChangePasswordDetails;

    private static final String TEST_USERNAME1 = "test_name",
                                TEST_PASSWORD1 = "test_password",
                                TEST_USERNAME2 = "test_name2",
                                TEST_PASSWORD2 = "test_password2";

    @BeforeAll
    public static void beforeAll(@Autowired UserService userService,
                                 @Autowired UserRepository userRepository) {

        userService.register(new UserDTO(TEST_USERNAME1, TEST_PASSWORD1));
        User userToChangePassword = userRepository.findByUsername(TEST_USERNAME1).orElseThrow();
        userToChangePasswordDetails = new UserDetailsImpl(userToChangePassword);

        userService.register(new UserDTO(TEST_USERNAME2, TEST_PASSWORD2));
        User user = userRepository.findByUsername(TEST_USERNAME2).orElseThrow();
        userDetails = new UserDetailsImpl(user);

        User admin = new User();
        admin.setRole("ROLE_ADMIN");
        adminUserDetails = new UserDetailsImpl(admin);
    }


    @Test
    public void testAuthenticatedAccess() throws Exception {
        mockMvc.perform(get("/")
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void testUnauthenticatedAccessFailure() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "username")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        mockMvc.perform(post("/process_login")
                        .param("username", TEST_USERNAME2)
                        .param("password", TEST_PASSWORD2)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testUnsuccessfulLogin() throws Exception {
        mockMvc.perform(post("/process_login")
                        .param("username", "wrong")
                        .param("password", "wrong")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void testAdminPageAccessForAdmin() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user(adminUserDetails)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAdminPageAccessDeniedForNotAdmin() throws Exception {
        mockMvc.perform(get("/admin")
                .with(user(userDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRegistrationValidation() throws Exception {
        UserDTO[] invalidUserDTOs = {
                new UserDTO("same","same"),
                new UserDTO("has space","password"),
                new UserDTO("log", "pass") // too short
        };

        for (UserDTO invalidUser : invalidUserDTOs) {
            mockMvc.perform(post("/register")
                            .param("username", invalidUser.getUsername())
                            .param("password", invalidUser.getPassword())
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("auth/register"))
                    .andExpect(model().hasErrors());
        }
    }

    @Test
    public void testFailureRegistrationExistedLogin() throws Exception {
        UserDTO userDTO = new UserDTO("login2", "password");
        service.register(userDTO);

        mockMvc.perform(post("/register")
                        .param("username", userDTO.getUsername())
                        .param("password", userDTO.getPassword())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "username"));
    }

    @Test
    public void testSuccessfulPasswordChange() throws Exception {
        mockMvc.perform(post("/changePassword")
                        .with(user(userToChangePasswordDetails))
                        .param("oldPassword", TEST_PASSWORD1)
                        .param("newPassword", "newPassword1234")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(model().hasNoErrors());

    }

    @Test
    public void testUnsuccessfulPasswordChange() throws Exception {
        mockMvc.perform(post("/changePassword")
                        .with(user(userToChangePasswordDetails))
                        .param("oldPassword", "wrong")
                        .param("newPassword", "wrong")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(model().hasErrors());
    }

}
