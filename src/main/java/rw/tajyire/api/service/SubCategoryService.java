package rw.tajyire.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rw.tajyire.api.dto.category.SubCategoryDTO;
import rw.tajyire.api.exception.EntityNotFoundException;
import rw.tajyire.api.model.Admin;
import rw.tajyire.api.model.Category;
import rw.tajyire.api.model.SubCategory;
import rw.tajyire.api.repo.SubCategoryRepo;
import rw.tajyire.api.util.AES;

@Service
public class SubCategoryService {
  @Autowired private SubCategoryRepo subCategoryRepo;
  @Autowired private CloudinaryService cloudinaryService;

  public SubCategory findByUuId(String categoryUuId) {
    return subCategoryRepo
        .findByUuidAndDeletedIsFalse(categoryUuId)
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

  public SubCategory editCategory(
      String subCategoryUuId, SubCategoryDTO subCategoryDTO, Admin admin) {
    SubCategory foundCategory = findByUuId(subCategoryUuId);
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

  public SubCategory removeCategory(String subCategoryUuId, Admin admin) {
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    String strDate = dateFormat.format(date);
    SubCategory foundCategory = findByUuId(subCategoryUuId);
    foundCategory.setDeleted(true);
    foundCategory.setName(AES.encrypt(foundCategory.getName() + " <-DELETED ON:-> " + strDate ));
    return subCategoryRepo.save(foundCategory);
  }
}
