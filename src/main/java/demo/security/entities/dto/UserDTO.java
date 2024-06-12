package demo.security.entities.dto;

import demo.security.util.validation.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldsNotEqual(
    field = "password",
    otherField = "username",
    message = "Password same as username"
)
public class UserDTO {

    @UsernameIsNotTaken
    @WithoutSpaces
    @Size(min = 5, max = 20, message = "Name must be from 5 to 20 chars")
    private String username;

    @StrongPassword
    private String password;

}
