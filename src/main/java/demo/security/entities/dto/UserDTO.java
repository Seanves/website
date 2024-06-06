package demo.security.entities.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "Name mustn't be empty")
    @Size(min = 5, max = 20, message = "Name must be from 5 to 20 chars")
    private String username;

    @NotBlank(message = "Password mustn't be empty")
    private String password;

}
