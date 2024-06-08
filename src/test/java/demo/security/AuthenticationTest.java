package demo.security;

import demo.security.entities.User;
import demo.security.entities.dto.UserDTO;
import demo.security.security.UserDetailsImpl;
import demo.security.services.UserService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
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


    @BeforeAll
    public static void beforeAll() {
        userDetails = new UserDetailsImpl(new User());
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
    public void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testSuccessRegistration() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "username")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().hasNoErrors());
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
                .with(user(adminUserDetails)))
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

}
