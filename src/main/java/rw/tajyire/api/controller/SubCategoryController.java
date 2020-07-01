package rw.tajyire.api.controller;

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
import rw.tajyire.api.dto.category.SubCategoryDTO;
import rw.tajyire.api.model.ApiResponse;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.SubCategory;
import rw.tajyire.api.service.SubCategoryService;
import rw.tajyire.api.util.ConstantUtil;
import rw.tajyire.api.util.ErrorUtil;

@RestController
@RequestMapping(ConstantUtil.v1Prefix + "/sub-categories")
public class SubCategoryController {
  @Autowired private SubCategoryService subCategoryService;

  @GetMapping
  public ResponseEntity<?> getSubCategories(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
    Page<SubCategory> categoryPage = subCategoryService.getSubCategories(page, size);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Categories retrieved successfully", categoryPage);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{categoryId}")
  public ResponseEntity<?> getSubCategory(@PathVariable Long categoryId) {
    SubCategory categoryPage = subCategoryService.findById(categoryId);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category retrieved successfully", categoryPage);
    return ResponseEntity.ok(response);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createSubCategory(
      @Valid SubCategoryDTO subCategoryDTO,
      @AuthenticationPrincipal Auth auth,
      BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    SubCategory newCategory = subCategoryService.createSubCategory(subCategoryDTO, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category created successfully", newCategory);
    return ResponseEntity.ok(response);
  }

  @PutMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> editSubCategory(
      @PathVariable Long categoryId,
      @AuthenticationPrincipal Auth auth,
      SubCategoryDTO subCategoryDTO,
      BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    SubCategory editedCategory =
        subCategoryService.editCategory(categoryId, subCategoryDTO, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category modified successfully", editedCategory);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping(value = "/{categoryId}")
  public ResponseEntity<?> removeSubCategory(
      @PathVariable Long categoryId, @AuthenticationPrincipal Auth auth) throws Exception {
    SubCategory editedCategory =
        subCategoryService.removeCategory(categoryId, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category removed successfully", editedCategory);
    return ResponseEntity.ok(response);
  }
}
