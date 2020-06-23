package rw.tajyire.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rw.tajyire.api.dto.auth.AdminCreationDTO;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Person;
import rw.tajyire.api.model.ResetPasswordToken;
import rw.tajyire.api.repo.AdminRepo;
import rw.tajyire.api.repo.AuthRepo;
import rw.tajyire.api.repo.ResetPasswordTokenRepo;
import rw.tajyire.api.util.DateUtil;

@Service
public class AdminService {

  @Autowired PasswordEncoder passwordEncoder;
  @Autowired private AuthRepo authRepo;
  @Autowired private AdminRepo adminRepo;
  @Autowired private ResetPasswordTokenRepo resetPasswordTokenRepo;
  @Autowired private MailService mailService;

  public Person createAdmin(AdminCreationDTO adminCreationDTO, HttpServletRequest request)
      throws IOException {
    Admin newAdmin =
        new Admin(
            adminCreationDTO.getFirstName(),
            adminCreationDTO.getLastName(),
            adminCreationDTO.getEmail());
    adminRepo.save(newAdmin);
    Auth newAuth =
        new Auth(
            newAdmin, adminCreationDTO.getEmail(), new ArrayList<>(Arrays.asList("ROLE_ADMIN")));
    authRepo.save(newAuth);
    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    final String token = UUID.randomUUID().toString();
    final String url = String.format("%s/final-step/%s", "http://" + request.getLocalName(), token);
    resetPasswordToken.setToken(token);
    resetPasswordToken.setAuth(newAuth);
    resetPasswordToken.setExpiryDate(DateUtil.addHoursToJavaUtilDate(new Date(), 3));
    resetPasswordToken.setPreviousPassword(newAuth.getPassword());
    resetPasswordTokenRepo.save(resetPasswordToken);
    mailService.sendText(
        "no-reply@tajyire.rw",
        "Tajyire B2C",
        newAdmin.getEmail(),
        "Password Reset",
        "Hey "
            + newAdmin.getFirstName()
            + " "
            + newAdmin.getLastName()
            + "\n use the following link to validate your account: \n"
            + url);
    return newAdmin;
  }
}
