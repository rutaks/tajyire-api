package rw.tajyire.api.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.tajyire.api.dto.auth.AuthRequestDTO;
import rw.tajyire.api.dto.auth.AuthResponseDTO;
import rw.tajyire.api.dto.auth.ForgotPasswordDTO;
import rw.tajyire.api.dto.auth.RegisterRequestDTO;
import rw.tajyire.api.dto.auth.RegisterResponseDTO;
import rw.tajyire.api.dto.auth.ResetPasswordDTO;
import rw.tajyire.api.exception.CustomAuthenticationException;
import rw.tajyire.api.model.ApiResponse;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Person;
import rw.tajyire.api.service.AuthService;
import rw.tajyire.api.util.ConstantUtil;
import rw.tajyire.api.util.ErrorUtil;
import rw.tajyire.api.util.JwtUtil;

@RestController
@RequestMapping(ConstantUtil.v1Prefix + "/auth")
public class AuthController {

  private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Incorrect username or password";
  @Autowired private AuthenticationManager authManager;
  @Autowired private AuthService userDetailsService;
  @Autowired private JwtUtil jwtUtil;

  @PostMapping("/admin-login")
  public ResponseEntity<?> adminLogin(
      @RequestBody @Valid AuthRequestDTO authRequestDTO, BindingResult bindingResult)
      throws Exception {
    try {
      final Auth userDetails = getAccount(authRequestDTO, bindingResult);
      if (!userDetailsService.isAdmin(userDetails.getPerson()))
        throw new CustomAuthenticationException(ACCOUNT_NOT_FOUND_MESSAGE);
      final String jwt = jwtUtil.generateToken(userDetails);
      AuthResponseDTO dto = new AuthResponseDTO(jwt, userDetails.getPerson());
      ApiResponse response = new ApiResponse(HttpStatus.OK, "Login Successful", dto);
      return ResponseEntity.ok(response);
    } catch (BadCredentialsException e) {
      throw new CustomAuthenticationException(ACCOUNT_NOT_FOUND_MESSAGE);
    }
  }

  @PostMapping("/client-login")
  public ResponseEntity<?> clientLogin(
      @RequestBody @Valid AuthRequestDTO authRequestDTO, BindingResult bindingResult)
      throws Exception {
    try {
      final Auth userDetails = getAccount(authRequestDTO, bindingResult);
      if (!userDetailsService.isClient(userDetails.getPerson()))
        throw new CustomAuthenticationException(ACCOUNT_NOT_FOUND_MESSAGE);
      final String jwt = jwtUtil.generateToken(userDetails);
      AuthResponseDTO dto = new AuthResponseDTO(jwt, userDetails.getPerson());
      ApiResponse response = new ApiResponse(HttpStatus.OK, "Login Successful", dto);
      return ResponseEntity.ok(response);
    } catch (BadCredentialsException e) {
      throw new CustomAuthenticationException(ACCOUNT_NOT_FOUND_MESSAGE);
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @Valid @RequestBody RegisterRequestDTO registerRequestDTO, BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    Person person = userDetailsService.register(registerRequestDTO);
    final Auth userDetails =
        userDetailsService.loadUserByUsername(registerRequestDTO.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);
    final RegisterResponseDTO responseDTO = new RegisterResponseDTO(jwt, person);
    ApiResponse response = new ApiResponse(HttpStatus.OK, "Registration Successful", responseDTO);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("/final-step/{token}")
  public ResponseEntity<?> validateAdminAccount(
      @PathVariable String token,
      @Valid @RequestBody ResetPasswordDTO resetPasswordDTO,
      BindingResult bindingResult) {
    ErrorUtil.checkForError(bindingResult);
    userDetailsService.resetPassword(token, resetPasswordDTO);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Account was validated successfully", null);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(
      HttpServletRequest request,
      @Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO,
      BindingResult bindingResult)
      throws IOException {
    ErrorUtil.checkForError(bindingResult);
    userDetailsService.sendForgotPasswordRequest(forgotPasswordDTO.getEmail(), request);
    ApiResponse response =
        new ApiResponse(
            HttpStatus.OK, "Email was successfully sent to " + forgotPasswordDTO.getEmail(), null);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/reset-password/{token}")
  public ResponseEntity<?> resetPassword(
      @PathVariable String token,
      @Valid @RequestBody ResetPasswordDTO resetPasswordDTO,
      BindingResult bindingResult) {
    ErrorUtil.checkForError(bindingResult);
    userDetailsService.resetPassword(token, resetPasswordDTO);
    ApiResponse response = new ApiResponse(HttpStatus.OK, "Password was reset successfully", null);
    return ResponseEntity.ok(response);
  }

  public Auth getAccount(AuthRequestDTO authRequestDTO, BindingResult bindingResult)
      throws CustomAuthenticationException {
    try {
      ErrorUtil.checkForError(bindingResult);
      authManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authRequestDTO.getUsername(), authRequestDTO.getPassword()));
      return userDetailsService.loadUserByUsername(authRequestDTO.getUsername());
    } catch (BadCredentialsException e) {
      throw new CustomAuthenticationException("Incorrect username or password");
    }
  }
}
