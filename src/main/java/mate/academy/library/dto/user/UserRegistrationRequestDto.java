package mate.academy.library.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import mate.academy.library.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password",
                second = "repeatPassword",
                message = "The password fields don't match")
})
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
    @NotBlank
    @Length(min = 8, max = 20)
    @ToString.Exclude
    private String password;
    @NotBlank
    @Length(min = 8, max = 20)
    @ToString.Exclude
    private String repeatPassword;
    @NotBlank
    @Size(max = 255)
    private String firstName;
    @NotBlank
    @Size(max = 255)
    private String lastName;
    @Size(max = 255)
    private String shoppingAddress;
}
