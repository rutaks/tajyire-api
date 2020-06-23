package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Auth;
import rw.tajyire.api.model.Person;
import rw.tajyire.api.model.ResetPasswordToken;

@Repository
public interface AuthRepo extends JpaRepository<Auth, Long> {
  Optional<Auth> findByUsername(String username);
  Optional<Auth> findByPerson(Person person);
  Optional<Auth> findByResetPasswordTokens(ResetPasswordToken resetPasswordToken);
}
