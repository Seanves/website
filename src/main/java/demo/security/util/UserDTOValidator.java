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

        if (user.getPassword() .equals( user.getUsername()) && !user.getPassword().isBlank()) {
            errors.rejectValue("password", "", "Password and name are the same");
        }

        if (user.getUsername().contains(" ")) {
            errors.rejectValue("username", "", "Username contains space");
        }

        try {
            userDetailsService.loadUserByUsername(user.getUsername());
            errors.rejectValue("username", "", "Name already taken");
        } catch (UsernameNotFoundException e) {}

    }
}
