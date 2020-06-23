package rw.tajyire.api.util;

import java.io.IOException;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rw.tajyire.api.exception.CustomAuthenticationException;
import rw.tajyire.api.exception.MismatchException;
import rw.tajyire.api.exception.ValidationException;
import rw.tajyire.api.model.ApiResponse;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({CustomAuthenticationException.class})
  public ResponseEntity<ApiResponse> handleAccessDeniedException(Exception ex) {
    log.error(Arrays.toString(ex.getStackTrace()));
    ApiResponse body = new ApiResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ApiResponse body = new ApiResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler({MismatchException.class, DateTimeException.class, ValidationException.class,
      UsernameNotFoundException.class})
  public ResponseEntity<ApiResponse> handleValidationErrors(Exception ex) {
    log.error(ex.getLocalizedMessage());
    ex.printStackTrace();
    ApiResponse body = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler({IncorrectResultSizeDataAccessException.class, Exception.class})
  public ResponseEntity<ApiResponse> handleOtherErrors(Exception ex) {
    log.error(ex.getLocalizedMessage());
    ex.printStackTrace();
    ApiResponse body = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler({IOException.class})
  public ResponseEntity<ApiResponse> handleInternalErrors(Exception ex) {
    log.error(ex.getLocalizedMessage());
    ApiResponse body = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    return ResponseEntity.badRequest().body(body);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("status", status.value());
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
    body.put("errors", errors);
    return new ResponseEntity<>(body, headers, status);
  }
}
