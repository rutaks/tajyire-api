package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Client;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
  Optional<Client> findByEmail(String email);
}
