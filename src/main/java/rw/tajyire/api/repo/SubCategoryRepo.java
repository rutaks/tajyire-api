package rw.tajyire.api.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Category;
import rw.tajyire.api.model.SubCategory;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
  Optional<SubCategory> findByUuidAndDeletedIsFalse(String subCategoryUuId);

  Page<SubCategory> findByDeletedIsFalse(Pageable pageable);

  Page<SubCategory> findByParentCategoryAndDeletedIsFalse(Category category, Pageable pageable);

  Page<SubCategory> findByParentCategoryUuidAndDeletedIsFalse(
      String categoryUuid, Pageable pageable);

  List<SubCategory> findByParentCategoryUuidAndDeletedIsFalse(String categoryUuid);
}
