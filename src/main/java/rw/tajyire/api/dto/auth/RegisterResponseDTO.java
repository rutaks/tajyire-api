package rw.tajyire.api.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import rw.tajyire.api.model.Person;

@Data
@NoArgsConstructor
public class RegisterResponseDTO {
  private String jwt;
  private Person person;

  public RegisterResponseDTO(String jwt, Person person) {
    this.jwt = jwt;
    this.person = person;
  }
}
