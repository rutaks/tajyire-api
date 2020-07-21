package rw.tajyire.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rw.tajyire.api.dto.category.CategoryDTO;
import rw.tajyire.api.exception.EntityNotFoundException;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.Category;
import rw.tajyire.api.model.SubCategory;
import rw.tajyire.api.repo.CategoryRepo;
import rw.tajyire.api.repo.SubCategoryRepo;
import rw.tajyire.api.util.AES;

@Service
public class CategoryService {
  @Autowired private CategoryRepo categoryRepo;
  @Autowired private SubCategoryRepo subCategoryRepo;
  @Autowired private CloudinaryService cloudinaryService;

  public Category findByUuId(String categoryId) {
    return categoryRepo
        .findByUuidAndDeletedIsFalse(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("Could not find category"));
  }

  public Page<SubCategory> getCategoriesSubCategories(String categoryUuId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
    return subCategoryRepo.findByParentCategoryUuidAndDeletedIsFalse(categoryUuId, pageable);
  }

  public List<SubCategory> getCategoriesSubCategories(String categoryUuId) {
    return subCategoryRepo.findByParentCategoryUuidAndDeletedIsFalse(categoryUuId);
  }

  public Page<Category> getCategories(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
    return categoryRepo.findByDeletedIsFalse(pageable);
  }

  public List<Category> getCategories() {
    return categoryRepo.findByDeletedIsFalse();
  }

  public Category createCategory(CategoryDTO categoryDTO, Admin admin) {
    final String imageCoverUrl = cloudinaryService.uploadFile(categoryDTO.getCoverImage());
    Category newCategory = new Category(categoryDTO);
    newCategory.setImageCover(imageCoverUrl);
    newCategory.setCreatedBy(admin.getUuid());
    return categoryRepo.save(newCategory);
  }

  public Category editCategory(String categoryUuId, CategoryDTO categoryDTO, Admin admin) {
    Category foundCategory = findByUuId(categoryUuId);
    if (categoryDTO.getCoverImage() != null) {
      final String imageCoverUrl = cloudinaryService.uploadFile(categoryDTO.getCoverImage());
      foundCategory.setImageCover(imageCoverUrl);
    }
    foundCategory.setName(categoryDTO.getName());
    foundCategory.setLastModifiedBy(admin.getUuid());
    foundCategory.setLastModifiedDate(new Date());

    return categoryRepo.save(foundCategory);
  }

  public Category removeCategory(String categoryId, Admin admin) {
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    String strDate = dateFormat.format(date);
    Category foundCategory = findByUuId(categoryId);
    foundCategory.setDeleted(true);
    foundCategory.setName(AES.encrypt(foundCategory.getName() + " <-DELETED ON:-> " + strDate ));
    return categoryRepo.save(foundCategory);
  }
}
