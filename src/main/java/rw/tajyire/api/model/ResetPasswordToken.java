package rw.tajyire.api.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "reset_password_tokens")
public class ResetPasswordToken {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  private String previousPassword;
  private String token;
  private Date expiryDate;
  private boolean isActive = true;

  @ManyToOne
  @JoinColumn(name = "auth_id", nullable = false)
  private Auth auth;
}
