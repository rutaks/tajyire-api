package rw.tajyire.api.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDTO {
  @NotBlank(message = "Category should have a name")
  private String name;
  private MultipartFile coverImage;
  @NotNull(message = "Sub-Category should have a Parent Category")
  private Long parentCategoryId;
}
