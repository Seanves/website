package demo.security.services;

import demo.security.entities.User;
import demo.security.entities.dto.UserDTO;
import demo.security.repositories.UserRepository;
import demo.security.util.UserDTOValidator;
import jakarta.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOValidator userDTOValidator;
    private final Validator defaultValidator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       UserDTOValidator userDTOValidator, Validator defaultValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOValidator = userDTOValidator;
        this.defaultValidator = defaultValidator;
    }


    public void register(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void changeColor(User user, String color) {
        user.setColor(color);
        userRepository.save(user);
    }

    public boolean changePassword(User user, String newPassword, String oldPassword, BindingResult br) {

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            br.rejectValue("oldPassword", "", "Wrong old password");
        }
        if (newPassword.equals(oldPassword)) {
            br.rejectValue("newPassword", "", "New password is the same as current");
        }

        userDTOValidator.validatePassword(newPassword, user.getUsername(), br, "newPassword");

        var violations = defaultValidator.validateValue(UserDTO.class, "password", newPassword);
        violations.forEach(v ->  System.out.println("violation = " + v.getMessage()));

        if (br.hasErrors()) { return false; }

        user.setPassword( passwordEncoder.encode(newPassword) );
        userRepository.save(user);
        return true;
    }

}
