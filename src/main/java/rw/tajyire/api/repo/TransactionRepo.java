package rw.tajyire.api.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Account;
import rw.tajyire.api.model.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
  Page<Transaction> getAllByAccountOwner(Account account, Pageable pageable);
}
