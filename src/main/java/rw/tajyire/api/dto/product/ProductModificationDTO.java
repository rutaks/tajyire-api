package rw.tajyire.api.dto.product;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModificationDTO {
  @NotBlank(message = "Category should have a name")
  private String name;
  @DecimalMin(value = "1", inclusive = false, message = "Price can not be negative")
  private Double price;
  @NotBlank(message = "Price Should be given a currency")
  private String priceCurrency;
  @DecimalMin(value = "0", inclusive = false, message = "Discount price can not be negative")
  private Double discountPrice;
  private String discountExpiryDate;
  private MultipartFile[] newImages;
  private String[] removedImages;
  @NotNull(message = "Product should have a Category")
  private Long categoryId;
}
