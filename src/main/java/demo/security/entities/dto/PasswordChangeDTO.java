package demo.security.entities.dto;

import lombok.Data;

@Data
public class PasswordChangeDTO {
    private String newPassword;
    private String oldPassword;
}
