package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.ResetPasswordToken;

@Repository
public interface ResetPasswordTokenRepo extends JpaRepository<ResetPasswordToken, Long> {
  Optional<ResetPasswordToken> findByToken(String token);
}
