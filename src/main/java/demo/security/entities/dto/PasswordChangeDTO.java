package demo.security.entities.dto;

import demo.security.util.validation.FieldsNotEqual;
import lombok.Data;

@Data
@FieldsNotEqual(
    field = "newPassword",
    otherField = "oldPassword",
    message = "New password same as old"
)
public class PasswordChangeDTO {
    private String newPassword;
    private String oldPassword;
}
