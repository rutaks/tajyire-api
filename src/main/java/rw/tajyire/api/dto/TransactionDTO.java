package rw.tajyire.api.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import rw.tajyire.api.enums.ETransactionType;
import rw.tajyire.api.model.Transaction;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
  @NotBlank(message = "Description must be provided")
  private String description;

  @NotBlank(message = "Amount must be provided")
  @Min(value = 10, message = "Amount must not be less than 10")
  @Max(value = 100000, message = "Amount must not be more than 100000")
  private long amount;

  @NotBlank(message = "Transaction type must be provided")
  private ETransactionType transactionType;

  public Transaction getEntity() {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(this, Transaction.class);
  }
}
