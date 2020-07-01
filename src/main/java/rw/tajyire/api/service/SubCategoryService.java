package rw.tajyire.api.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rw.tajyire.api.dto.category.CategoryDTO;
import rw.tajyire.api.dto.category.SubCategoryDTO;
import rw.tajyire.api.exception.EntityNotFoundException;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.Category;
import rw.tajyire.api.model.SubCategory;
import rw.tajyire.api.repo.CategoryRepo;
import rw.tajyire.api.repo.SubCategoryRepo;
import rw.tajyire.api.util.AES;

@Service
public class SubCategoryService {
  @Autowired private SubCategoryRepo subCategoryRepo;
  @Autowired private CloudinaryService cloudinaryService;

  public SubCategory findById(Long categoryId) {
    return subCategoryRepo
        .findByIdAndDeletedIsFalse(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("Could not find category"));
  }

  public Page<SubCategory> getSubCategories(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
    return subCategoryRepo.findByDeletedIsFalse(pageable);
  }

  public SubCategory createSubCategory(SubCategoryDTO subCategoryDTO, Admin admin) {
    final String imageCoverUrl = cloudinaryService.uploadFile(subCategoryDTO.getCoverImage());
    SubCategory newCategory = new SubCategory(subCategoryDTO);
    newCategory.setImageCover(imageCoverUrl);
    newCategory.setCreatedBy(admin.getUuid());
    return subCategoryRepo.save(newCategory);
  }

  public SubCategory editCategory(Long subCategoryId, SubCategoryDTO subCategoryDTO, Admin admin) {
    SubCategory foundCategory = findById(subCategoryId);
    if (subCategoryDTO.getCoverImage() != null) {
      final String imageCoverUrl = cloudinaryService.uploadFile(subCategoryDTO.getCoverImage());
      foundCategory.setImageCover(imageCoverUrl);
    }
    foundCategory.setName(subCategoryDTO.getName());
    foundCategory.setParentCategory(new Category(subCategoryDTO.getParentCategoryId()));
    foundCategory.setLastModifiedBy(admin.getUuid());
    foundCategory.setLastModifiedDate(new Date());

    return subCategoryRepo.save(foundCategory);
  }

  public SubCategory removeCategory(Long subCategoryId, Admin admin) {
    SubCategory foundCategory = findById(subCategoryId);
    foundCategory.setDeleted(true);
    foundCategory.setName(AES.encrypt(foundCategory.getName()));
    return subCategoryRepo.save(foundCategory);
  }
}
