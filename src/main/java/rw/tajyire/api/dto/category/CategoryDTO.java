package rw.tajyire.api.dto.category;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
  @NotBlank(message = "Category should have a name")
  private String name;
  private MultipartFile coverImage;
}
