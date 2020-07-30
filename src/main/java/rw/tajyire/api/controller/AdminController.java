package rw.tajyire.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rw.tajyire.api.dto.auth.AdminCreationDTO;
import rw.tajyire.api.exception.CustomAuthenticationException;
import rw.tajyire.api.exception.OperationFailedException;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.ApiResponse;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Person;
import rw.tajyire.api.service.AdminService;
import rw.tajyire.api.service.AuthService;
import rw.tajyire.api.util.ConstantUtil;
import rw.tajyire.api.util.ErrorUtil;
import rw.tajyire.api.util.JwtUtil;

@RestController
@RequestMapping(ConstantUtil.v1Prefix + "/admins")
public class AdminController {

  @Autowired
  private AuthenticationManager authManager;
  @Autowired
  private AuthService userDetailsService;
  @Autowired
  private AdminService adminService;
  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping
  public ResponseEntity<?> createAdmin(
      HttpServletRequest request,
      @Valid @RequestBody AdminCreationDTO adminCreationDTO,
      @AuthenticationPrincipal Auth auth,
      BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    try {
      if (!userDetailsService.isAdmin(auth.getPerson())) {
        throw new CustomAuthenticationException("Invalid access");
      }
      Person person = adminService.createAdmin(adminCreationDTO, request);
      ApiResponse response =
          new ApiResponse(
              HttpStatus.OK, "Email was successfully sent to " + adminCreationDTO.getEmail(), person);
      return ResponseEntity.ok(response);
    } catch (DataIntegrityViolationException e) {
      throw new OperationFailedException("User with the same email exists");
    }
  }

  @GetMapping
  public ResponseEntity<?> getCategories(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
    ApiResponse response = null;
    Page<Admin> adminPage = adminService.getAdmins(page, size);
    response =
        new ApiResponse(HttpStatus.OK, "Admins retrieved successfully", adminPage);

    return ResponseEntity.ok(response);
  }
}
