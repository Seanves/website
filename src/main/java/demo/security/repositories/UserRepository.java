package demo.security.repositories;

import demo.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);
    User findById(int id);

    List<User> findAll();

    @Override
    User save(User user);

//    @Modifying
//    @Query("UPDATE User p SET p.birthYear = :birthYear WHERE p.id = :id")
//    void updateBirthYearById(int id, int birthYear);

//    void updateColor(User person, String color);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE User p SET p.password = :newPassword WHERE p.id = :personId")
//    void updatePasswordById(int personId, String newPassword);
}
