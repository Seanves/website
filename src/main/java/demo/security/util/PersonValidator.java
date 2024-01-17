package demo.security.util;

import demo.security.services.PersonDetailsService;
import demo.security.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person)o;

        if (person.getPassword() .equals( person.getUsername()) && !person.getPassword().isBlank()) {
            errors.rejectValue("password", "", "Password and name are the same");
        }

        try {
            personDetailsService.loadUserByUsername(person.getUsername());
            errors.rejectValue("username", "", "Name already taken");
        } catch (UsernameNotFoundException e) {}

    }
}
