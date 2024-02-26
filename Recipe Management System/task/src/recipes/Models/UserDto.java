package recipes.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    @Email(regexp = ".+[@].+[\\.].+", message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must have length of 8 or greater")
    private String password;
}
