package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Account;
import rw.tajyire.api.model.Client;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
  Optional<Account> findByClient(Client client);
}
