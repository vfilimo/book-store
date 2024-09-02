package online.store.book.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import online.store.book.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@FieldMatch(first = "password", second = "repeatPassword",
        message = "Password must match the repeat password")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 4, max = 32)
    private String password;
    @NotBlank
    @Length(min = 4, max = 32)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
