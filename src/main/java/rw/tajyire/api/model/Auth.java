package rw.tajyire.api.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rw.tajyire.api.dto.auth.RegisterRequestDTO;

@Data
@Entity
@Table(name = "auth")
public class Auth implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @OneToOne private Person person;

  @Column(unique = true)
  private String username;

  private String password;

  private boolean active = true;

  private boolean accountNonExpired = true;

  private boolean accountNonLocked = true;

  private boolean credentialsNonExpired = true;

  private boolean isEnabled = true;

  @OneToMany(mappedBy = "auth")
  private Set<ResetPasswordToken> resetPasswordTokens;

  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  public Auth() {}

  public Auth(RegisterRequestDTO registerRequestDTO, Person person, List<String> roles) {
    this.person = person;
    this.username = registerRequestDTO.getUsername();
    this.password = registerRequestDTO.getPassword();
    this.roles = roles;
  }

  public Auth(Person person, String username, String password, List<String> roles) {
    this.person = person;
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public Auth(Person person, String username, List<String> roles) {
    this.person = person;
    this.username = username;
    this.roles = roles;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public boolean isActive() {
    return active;
  }
}
