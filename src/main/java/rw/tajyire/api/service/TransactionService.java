package rw.tajyire.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rw.tajyire.api.enums.ETransactionType;
import rw.tajyire.api.exception.EntityNotFoundException;
import rw.tajyire.api.model.Account;
import rw.tajyire.api.model.Client;
import rw.tajyire.api.model.Transaction;
import rw.tajyire.api.repo.AccountRepo;
import rw.tajyire.api.repo.TransactionRepo;

@Service
public class TransactionService {
  @Autowired private AccountRepo accountRepo;
  @Autowired private TransactionRepo transactionRepo;

  public Account registerTransaction(Transaction transaction, Client client) {
    Account foundAccount =
        accountRepo
            .findByClient(client)
            .orElseThrow(() -> new EntityNotFoundException("Could not find account"));
    double newBalance =
        (transaction.getTransactionType().equals(ETransactionType.EXPENSE))
            ? foundAccount.getBalance() - transaction.getAmount()
            : foundAccount.getBalance() + transaction.getAmount();
    foundAccount.setBalance(newBalance);
    transaction.setAccountOwner(foundAccount);
    transactionRepo.save(transaction);
    accountRepo.save(foundAccount);
    return foundAccount;
  }

  public Page<Transaction> getAccountTransactions(Client client, int page, int size) {
    Account foundAccount =
        accountRepo
            .findByClient(client)
            .orElseThrow(() -> new EntityNotFoundException("Could not find account"));
    return transactionRepo.getAllByAccountOwner(foundAccount, PageRequest.of(page, size));
  }
}
