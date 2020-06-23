package rw.tajyire.api.dto.auth;

import javax.validation.constraints.Email;
import lombok.Data;

@Data
public class ForgotPasswordDTO {
  @Email
  private String email;
}
