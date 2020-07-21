package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
  Optional<Product> findByUuidAndDeletedIsFalse(String productUuId);
  Page<Product> getAllByDeletedIsFalse(Pageable pageable);
  Page<Product> getAllByCategoryUuidAndDeletedIsFalse(Pageable pageable, String categoryUuId);
}
