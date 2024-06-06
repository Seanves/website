package demo.security.services;

import demo.security.entities.User;
import demo.security.entities.dto.UserDTO;
import demo.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired private final UserRepository userRepository;
    @Autowired private final PasswordEncoder passwordEncoder;


    public void register(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

//    public void changeColor(User person, String color) {
//        userRepository.updateColor(person, color);
//    }

    public void changeColor(User user, String color) {
        user.setColor(color);
        userRepository.save(user);
    }

}
