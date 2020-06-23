package rw.tajyire.api.exception;

public class OperationFailedException extends RuntimeException {
  public OperationFailedException(String message) {
    super(message);
  }
}
