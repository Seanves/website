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

    @Size(min = 5, max = 20, message = "Name must be from 5 to 20 chars")
    private String username;

    @Size(min = 5, max = 30, message = "Password must be from 5 to 30 chars")
    private String password;

}
