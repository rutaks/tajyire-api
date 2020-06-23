package rw.tajyire.api.dto.auth;

import javax.validation.constraints.NotBlank;
import rw.tajyire.api.annotation.ValidPassword;

public class AuthRequestDTO {
  @NotBlank(message = "Username can not be empty")
  private String username;
  @ValidPassword(message = "Password not valid")
  private String password;

  public AuthRequestDTO() {}

  public AuthRequestDTO(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
