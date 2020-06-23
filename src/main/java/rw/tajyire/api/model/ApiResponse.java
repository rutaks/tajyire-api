package rw.tajyire.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse {
  private HttpStatus status;
  private String message;
  private Object payload;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;

  public ApiResponse(HttpStatus status, String message, Object payload) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.message = message;
    this.payload = payload;
  }

  public String convertToJson() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return mapper.writeValueAsString(this);
  }

  @SneakyThrows
  @Override
  public String toString() {
    return convertToJson();
  }
}
