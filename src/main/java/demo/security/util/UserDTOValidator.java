package demo.security.util;

import demo.security.entities.dto.UserDTO;
import demo.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserDTOValidator implements Validator {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserDTOValidator(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDTO user = (UserDTO)o;

        if (user.getUsername().contains(" ")) {
            errors.rejectValue("username", "", "Username contains space");
        }

        validatePassword(user.getPassword(), user.getUsername(), errors, "password");

        try {
            userDetailsService.loadUserByUsername(user.getUsername());
            errors.rejectValue("username", "", "Username already taken");
        } catch (UsernameNotFoundException e) {}

    }

    public void validatePassword(String password, String username, Errors errors, String field) {
        if (password.equals(username) && !password.isBlank()) {
            errors.rejectValue(field, "", "Password and name are the same");
        }

        if (password.contains(" ")) {
            errors.rejectValue(field, "", "Password contains space");
        }
    }
}
