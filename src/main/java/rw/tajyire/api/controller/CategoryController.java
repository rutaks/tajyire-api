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
import rw.tajyire.api.dto.category.CategoryDTO;
import rw.tajyire.api.model.ApiResponse;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Category;
import rw.tajyire.api.model.SubCategory;
import rw.tajyire.api.service.CategoryService;
import rw.tajyire.api.util.ConstantUtil;
import rw.tajyire.api.util.ErrorUtil;

@RestController
@RequestMapping(ConstantUtil.v1Prefix + "/categories")
public class CategoryController {
  @Autowired private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<?> getCategories(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
    Page<Category> categoryPage = categoryService.getCategories(page, size);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Categories retrieved successfully", categoryPage);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{categoryId}/sub-categories")
  public ResponseEntity<?> getCategorySubCategory(@PathVariable Long categoryId,@RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {
    Page<SubCategory> subCategoryPage = categoryService.getCategoriesSubCategories(categoryId, page, size);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Sub Category retrieved successfully", subCategoryPage);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{categoryId}")
  public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
    Category categoryPage = categoryService.findById(categoryId);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category retrieved successfully", categoryPage);
    return ResponseEntity.ok(response);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createCategory(
      @Valid CategoryDTO categoryDTO,
      @AuthenticationPrincipal Auth auth,
      BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    Category newCategory = categoryService.createCategory(categoryDTO, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category created successfully", newCategory);
    return ResponseEntity.ok(response);
  }

  @PutMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> editCategory(
      @PathVariable Long categoryId,
      @AuthenticationPrincipal Auth auth,
      CategoryDTO categoryDTO,
      BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    Category editedCategory =
        categoryService.editCategory(categoryId, categoryDTO, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category modified successfully", editedCategory);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping(value = "/{categoryId}")
  public ResponseEntity<?> editCategory(
      @PathVariable Long categoryId, @AuthenticationPrincipal Auth auth) throws Exception {
    Category editedCategory =
        categoryService.removeCategory(categoryId, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category removed successfully", editedCategory);
    return ResponseEntity.ok(response);
  }
}
