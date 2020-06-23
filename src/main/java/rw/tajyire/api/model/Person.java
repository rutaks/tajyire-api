package rw.tajyire.api.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import rw.tajyire.api.dto.auth.RegisterRequestDTO;
import rw.tajyire.api.enums.EGender;

@Data
@Entity(name = "people")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  private String firstName;

  private String lastName;

  @Column(unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  private EGender gender;

  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  private String uuid = UUID.randomUUID().toString();

  public Person() {}

  public Person(String firstName, String lastName, String gender, Date dateOfBirth) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = EGender.valueOf(gender.toUpperCase());
  }

  public Person(String firstName, String lastName, String gender, Date dateOfBirth, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.gender = EGender.valueOf(gender.toUpperCase());
    this.email = email;
  }

  public Person(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public Person(RegisterRequestDTO registerRequestDTO) {
    this.firstName = registerRequestDTO.getFirstName();
    this.lastName = registerRequestDTO.getLastName();
  }

  public Client toUser() {
    return (Client) this;
  }
}
