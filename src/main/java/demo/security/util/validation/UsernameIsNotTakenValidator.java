package demo.security.util.validation;

import demo.security.services.UserDetailsServiceImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UsernameIsNotTakenValidator implements
                        ConstraintValidator<UsernameIsNotTaken, String> {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    public boolean isValid(String s, ConstraintValidatorContext cvc) {
        try {
            userDetailsService.loadUserByUsername(s);
            return false;
        } catch (UsernameNotFoundException e) { return true; }
    }
}
