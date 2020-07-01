package rw.tajyire.api.dto.auth;

import rw.tajyire.api.model.Person;

public class AuthResponseDTO {
  private String jwt;
  private Person user;

  public AuthResponseDTO(String jwt, Person user) {
    this.jwt = jwt;
    this.user = user;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public Person getUser() {
    return user;
  }

  public void setUser(Person user) {
    this.user = user;
  }
}
