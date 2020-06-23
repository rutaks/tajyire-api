package rw.tajyire.api.dto.auth;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.tajyire.api.model.Person;

@Data
@NoArgsConstructor
public class AdminCreationDTO {
  @NotBlank(message = "First name must be provided")
  private String firstName;

  @NotBlank(message = "Last name must be provided")
  private String lastName;

  @NotBlank(message = "An email must be provided")
  private String email;

}
