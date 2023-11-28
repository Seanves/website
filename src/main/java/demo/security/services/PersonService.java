package demo.security.services;

import demo.security.repositories.PersonRepository;
import demo.security.entities.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PersonService {

    @Autowired private final PersonRepository personRepository;
    @Autowired private final PasswordEncoder passwordEncoder;


    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    public void changeYear(Person person, int year) {
        personRepository.updateBirthyearById(person.getId(), year);
//        person.setBirthyear(year);
//        peopleRepository.save(person);
    }

}
