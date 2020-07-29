package rw.tajyire.api.controller;

import java.text.ParseException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rw.tajyire.api.dto.product.ProductCreationDTO;
import rw.tajyire.api.dto.product.ProductModificationDTO;
import rw.tajyire.api.exception.OperationFailedException;
import rw.tajyire.api.exception.ValidationException;
import rw.tajyire.api.model.ApiResponse;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Product;
import rw.tajyire.api.service.ProductService;
import rw.tajyire.api.util.ConstantUtil;
import rw.tajyire.api.util.ErrorUtil;

@RestController
@RequestMapping(ConstantUtil.v1Prefix + "/products")
public class ProductController {
  @Autowired private ProductService productService;

  @GetMapping
  public ResponseEntity<?> getProducts(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
    Page<Product> productPage = productService.getProducts(page, size);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Products retrieved successfully", productPage);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{productUuId}")
  public ResponseEntity<?> getProduct(@PathVariable String productUuId) {
    Product product = productService.findByUuId(productUuId);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Product retrieved successfully", product);
    return ResponseEntity.ok(response);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createProduct(
      @Valid ProductCreationDTO dto,
      @AuthenticationPrincipal Auth auth,
      BindingResult bindingResult) {
    try {
      ErrorUtil.checkForError(bindingResult);
      Product newProduct = productService.createProduct(dto, auth.getPerson().toAdmin());
      ApiResponse response =
          new ApiResponse(HttpStatus.OK, "Product created successfully", newProduct);
      return ResponseEntity.ok(response);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new ValidationException("Provide proper discount date ");
    }catch (Exception e){
      e.printStackTrace();
      throw new OperationFailedException("Could not process request, Error: " + e.getMessage());
    }
  }

  @PutMapping(value = "/{productUuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> editProduct(
      @PathVariable String productUuId,
      @AuthenticationPrincipal Auth auth,
      ProductModificationDTO dto,
      BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    Product editedProduct =
        productService.editProduct(productUuId, dto, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Product modified successfully", editedProduct);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping(value = "/{productUuId}")
  public ResponseEntity<?> removeProduct(
      @PathVariable String productUuId, @AuthenticationPrincipal Auth auth) throws Exception {
    Product removedProduct = productService.removeProduct(productUuId, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Product removed successfully", removedProduct);
    return ResponseEntity.ok(response);
  }
}
