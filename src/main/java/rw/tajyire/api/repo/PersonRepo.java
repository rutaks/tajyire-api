package rw.tajyire.api.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.tajyire.api.model.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {
  Optional<Person> findByEmail(String email);
}
