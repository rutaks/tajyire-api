package rw.tajyire.api.service;

import java.io.IOException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.tajyire.api.dto.auth.RegisterRequestDTO;
import rw.tajyire.api.dto.auth.ResetPasswordDTO;
import rw.tajyire.api.exception.EntityNotFoundException;
import rw.tajyire.api.exception.MismatchException;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Client;
import rw.tajyire.api.model.Person;
import rw.tajyire.api.model.ResetPasswordToken;
import rw.tajyire.api.repo.AuthRepo;
import rw.tajyire.api.repo.ClientRepo;
import rw.tajyire.api.repo.PersonRepo;
import rw.tajyire.api.repo.ResetPasswordTokenRepo;
import rw.tajyire.api.util.DateUtil;

@Service
public class AuthService implements UserDetailsService {

  @Autowired PasswordEncoder passwordEncoder;
  @Autowired private AuthRepo authRepo;
  @Autowired private ClientRepo clientRepo;
  @Autowired private PersonRepo personRepo;
  @Autowired private ResetPasswordTokenRepo resetPasswordTokenRepo;
  @Autowired private MailService mailService;

  @Override
  public Auth loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Auth> auth = authRepo.findByUsername(username);
    auth.orElseThrow(() -> new UsernameNotFoundException("Could not find: " + username));
    return auth.get();
  }

  public Person register(RegisterRequestDTO registerRequestDTO) throws Exception {
    try {
      Client client = clientRepo.save(new Client(registerRequestDTO));
      registerRequestDTO.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
      Auth auth = new Auth(registerRequestDTO, client, new ArrayList<>(Arrays.asList("ROLE_USER")));
      authRepo.save(auth);
      return client;
    } catch (DataIntegrityViolationException e) {
      throw new Exception("Account with the same email exists");
    } catch (Exception e) {
      throw new Exception("Could Not Create Account: " + e.getMessage());
    }
  }

  public void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) {
    Optional<ResetPasswordToken> resetPasswordToken = resetPasswordTokenRepo.findByToken(token);
    resetPasswordToken.orElseThrow(
        () -> new EntityNotFoundException("Reset Password request not found"));

    if (!resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())) {
      throw new MismatchException("Passwords do not match");
    }
    // TODO: REFACTOR TO COMPARE CURRENT DATE WITH DB DATE. ISSUE IS THAT CURRENT DATE COMES IN
    // `CAT` TIMEZONE AND COMPARISON FAILS
    if (!resetPasswordToken.get().isActive()) {
      throw new DateTimeException("The Token has expired");
    }

    Optional<Auth> auth = authRepo.findByResetPasswordTokens(resetPasswordToken.get());
    auth.orElseThrow(() -> new EntityNotFoundException("Account was not found"));

    auth.get().setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));

    authRepo.save(auth.get());

    resetPasswordToken.get().setActive(false);
    resetPasswordTokenRepo.save(resetPasswordToken.get());
  }

  public void sendForgotPasswordRequest(String email, HttpServletRequest request)
      throws IOException {
    Optional<Person> user = personRepo.findByEmail(email);
    user.orElseThrow(() -> new EntityNotFoundException("Could not find user with email: " + email));
    Optional<Auth> auth = authRepo.findByPerson(user.get());
    auth.orElseThrow(
        () -> new EntityNotFoundException("Could not find user auth account with email: " + email));

    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();

    final String token = UUID.randomUUID().toString();
    final String url =
        String.format("%s/reset-password/%s", "http://" + request.getLocalName(), token);

    resetPasswordToken.setToken(token);
    resetPasswordToken.setAuth(auth.get());
    resetPasswordToken.setExpiryDate(DateUtil.addHoursToJavaUtilDate(new Date(), 3));
    resetPasswordToken.setPreviousPassword(auth.get().getPassword());
    resetPasswordTokenRepo.save(resetPasswordToken);

    mailService.sendText(
        "no-reply@tajyire.rw",
        "Tajyire B2C",
        email,
        "Password Reset",
        "Hey "
            + user.get().getFirstName()
            + " "
            + user.get().getLastName()
            + "\n use the following link to reset your account: \n"
            + url);
  }

  public boolean isAdmin(Person person) {
    return person.getClass().isAssignableFrom(Admin.class);
  }

  public boolean isClient(Person person) {
    return person.getClass().isAssignableFrom(Client.class);
  }
}
