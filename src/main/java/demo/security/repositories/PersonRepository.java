package demo.security.repositories;

import demo.security.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface PersonRepository extends JpaRepository<Person,Integer> {

    Optional<Person> findByUsername(String username);
    Person findById(int id);

    List<Person> findAll();

    @Override Person save(Person person);

    @Modifying
    @Query("UPDATE Person p SET p.birthyear = :birthyear WHERE p.id = :id")
    void updateBirthyearById(int id, int birthyear);

//    void updateColor(Person person, String color);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Person p SET p.password = :newPassword WHERE p.id = :personId")
//    void updatePasswordById(int personId, String newPassword);
}
