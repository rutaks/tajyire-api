package rw.tajyire.api.model;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private String userName;
  private String password;
  private boolean active;
  private List<GrantedAuthority> authorities;

  public CustomUserDetails() {}

  public CustomUserDetails(Auth auth) {
    this.userName = auth.getUsername();
    this.password = auth.getPassword();
    this.active = auth.isActive();
  }

  public CustomUserDetails(String userName) {
    this.userName = userName;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
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
}
