package rw.tajyire.api.model;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import rw.tajyire.api.dto.auth.RegisterRequestDTO;

@Data
@Entity
@DiscriminatorValue("CLIENT")
@EqualsAndHashCode(callSuper = false)
public class Client extends Person {

  public Client() {}

  public Client(String firstName, String lastName, String gender, Date dateOfBirth) {
    super(firstName, lastName, gender, dateOfBirth);
  }

  public Client(String firstName, String lastName, String gender, Date dateOfBirth, String email) {
    super(firstName, lastName, gender, dateOfBirth, email);
  }

  public Client(RegisterRequestDTO registerRequestDTO) {
    super(registerRequestDTO);
  }
}
