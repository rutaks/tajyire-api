package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
  Optional<Admin> findByEmail(String email);
}
