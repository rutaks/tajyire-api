package rw.tajyire.api.controller;

import java.util.List;
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
    ApiResponse response = null;
    if (page == -1) {
      List<Category> categoryPage = categoryService.getCategories();
      response =
          new ApiResponse(HttpStatus.OK, "Categories retrieved successfully", categoryPage);
    } else {
      Page<Category> categoryPage = categoryService.getCategories(page, size);
      response =
          new ApiResponse(HttpStatus.OK, "Categories retrieved successfully", categoryPage);
    }


    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{categoryUuId}/sub-categories")
  public ResponseEntity<?> getCategorySubCategory(
      @PathVariable String categoryUuId,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {
    ApiResponse response = null;
    if (page == -1) {
      List<SubCategory> subCategoryPage = categoryService.getCategoriesSubCategories(categoryUuId);
      response =
          new ApiResponse(HttpStatus.OK, "Sub Category retrieved successfully", subCategoryPage);
    } else {
      Page<SubCategory> subCategoryPage =
          categoryService.getCategoriesSubCategories(categoryUuId, page, size);
      response =
          new ApiResponse(HttpStatus.OK, "Sub Category retrieved successfully", subCategoryPage);
    }
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{categoryUuId}")
  public ResponseEntity<?> getCategory(@PathVariable String categoryUuId) {
    Category categoryPage = categoryService.findByUuId(categoryUuId);
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

  @PutMapping(value = "/{categoryUuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> editCategory(
      @PathVariable String categoryUuId,
      @AuthenticationPrincipal Auth auth,
      CategoryDTO categoryDTO,
      BindingResult bindingResult)
      throws Exception {
    ErrorUtil.checkForError(bindingResult);
    Category editedCategory =
        categoryService.editCategory(categoryUuId, categoryDTO, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category modified successfully", editedCategory);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping(value = "/{categoryUuId}")
  public ResponseEntity<?> deleteCategory(
      @PathVariable String categoryUuId, @AuthenticationPrincipal Auth auth) throws Exception {
    Category editedCategory =
        categoryService.removeCategory(categoryUuId, auth.getPerson().toAdmin());
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Category removed successfully", editedCategory);
    return ResponseEntity.ok(response);
  }
}
