package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Category;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
  Optional<Category> findByIdAndDeletedIsFalse(Long categoryId);
  Page<Category> findByDeletedIsFalse(Pageable pageable);
}
