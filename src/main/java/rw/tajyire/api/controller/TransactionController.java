package rw.tajyire.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rw.tajyire.api.dto.TransactionDTO;
import rw.tajyire.api.model.Account;
import rw.tajyire.api.model.ApiResponse;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Transaction;
import rw.tajyire.api.service.TransactionService;
import rw.tajyire.api.util.ErrorUtil;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
  @Autowired private TransactionService transactionService;

  @PostMapping
  public ResponseEntity<?> registerTransaction(
      @RequestBody TransactionDTO transactionDTO,
      @AuthenticationPrincipal Auth auth,
      BindingResult bindingResult) {
    ErrorUtil.checkForError(bindingResult);
    Account foundAccount =
        transactionService.registerTransaction(
            transactionDTO.getEntity(), auth.getPerson().toUser());
    ApiResponse apiResponse =
        new ApiResponse(HttpStatus.OK, "Transaction was successful", foundAccount);
    return ResponseEntity.ok(apiResponse);
  }

  @GetMapping
  public ResponseEntity<?> getAccountTransactions(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @AuthenticationPrincipal Auth auth) {
    Page<Transaction> foundTransactions =
        transactionService.getAccountTransactions(auth.getPerson().toUser(), page, size);
    ApiResponse response =
        new ApiResponse(HttpStatus.OK, "Transactions found successfully", foundTransactions);
    return ResponseEntity.ok(response);
  }
}
