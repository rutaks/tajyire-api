package rw.tajyire.api.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import rw.tajyire.api.enums.ETransactionType;

@Data
@Entity(name = "transactions")
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  private String description;
  private double amount;

  @Enumerated(EnumType.STRING)
  private ETransactionType transactionType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_owner", nullable = false)
  private Account accountOwner;

  private String uuid = UUID.randomUUID().toString();
}
