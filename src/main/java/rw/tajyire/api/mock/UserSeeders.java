package rw.tajyire.api.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rw.tajyire.api.exception.OperationFailedException;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Client;
import rw.tajyire.api.repo.AdminRepo;
import rw.tajyire.api.repo.AuthRepo;
import rw.tajyire.api.repo.ClientRepo;

@Component
@Slf4j
public class UserSeeders {
  private static final String TAG = "Seeders";

  private static final List<String> normalRole = new ArrayList<>(Arrays.asList("ROLE_USER"));
  private static final List<String> adminRole = new ArrayList<>(Arrays.asList("ROLE_ADMIN"));

  @Autowired PasswordEncoder passwordEncoder;
  @Autowired private ClientRepo clientRepo;
  @Autowired private AdminRepo adminRepo;
  @Autowired private AuthRepo authRepo;

  public boolean seed() {
    try {
      this.seedUsersTable();
      return true;
    } catch (Exception e) {
      log.error(TAG, e.getMessage());
      return false;
    }
  }

  public void seedUsersTable() {
    try {
      Admin admin = new Admin("Admin", "One", "male", new Date(), "rutaksam@gmail.com");
      adminRepo.save(admin);
      Auth auth1 = new Auth(admin, "admin1", passwordEncoder.encode("Password123!"), adminRole);
      authRepo.save(auth1);

      Client client = new Client("User", "Two", "male", new Date(), "rootsum.dev@gmail.com");
      clientRepo.save(client);
      Auth auth2 = new Auth(client, "user1", passwordEncoder.encode("Password123!"), normalRole);
      authRepo.save(auth2);
    } catch (Exception e) {
      throw new OperationFailedException("Could Not Run User Seeds");
    }
  }
}
