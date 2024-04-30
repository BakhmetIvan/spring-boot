package mate.academy.library.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @Email
    @NotBlank
    @Size(max = 255)
    private String email;
    @NotBlank
    @Length(min = 8, max = 20)
    @ToString.Exclude
    private String password;
}
