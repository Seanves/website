package demo.security.services;

import demo.security.entities.User;
import demo.security.entities.dto.PasswordChangeDTO;
import demo.security.entities.dto.UserDTO;
import demo.security.repositories.UserRepository;
import jakarta.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                                                    Validator validator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }


    public void register(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User(userDTO);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public void changeColor(User user, String color) {
        user.setColor(color);
        userRepository.save(user);
    }

    public boolean changePassword(User user, PasswordChangeDTO dto, BindingResult br) {

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            br.rejectValue("oldPassword", "", "Incorrect old password");
        }

        var violations = validator.validateValue(UserDTO.class, "password", dto.getNewPassword());
        violations.forEach(v -> br.rejectValue("newPassword", "", v.getMessage()));

        if (br.hasErrors()) { return false; }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }

}
