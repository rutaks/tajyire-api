package rw.tajyire.api.dto.auth;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.tajyire.api.annotation.ValidPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
  @NotBlank(message = "First name must be provided")
  private String firstName;

  @NotBlank(message = "Last name must be provided")
  private String lastName;

  @NotBlank(message = "A username must be provided")
  private String username;

  @ValidPassword @NotBlank private String password;
}
