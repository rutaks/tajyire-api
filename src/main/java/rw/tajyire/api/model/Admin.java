package rw.tajyire.api.model;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@DiscriminatorValue("ADMIN")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Admin extends Person {

  private boolean active = false;

  public Admin(String firstName, String lastName, String gender, Date dateOfBirth, String email) {
    super(firstName, lastName, gender, dateOfBirth, email);
  }

  public Admin(String firstName, String lastName, String email) {
    super(firstName, lastName, email);
  }
}
