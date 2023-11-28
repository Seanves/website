package demo.security.services;

import demo.security.repositories.PersonRepository;
import demo.security.entities.Person;
import demo.security.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;


    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> optional = personRepository.findByUsername(username);
        if(optional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new PersonDetails(optional.get());
    }
}
