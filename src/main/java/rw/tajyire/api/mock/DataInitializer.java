package rw.tajyire.api.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private UserSeeders userSeeders;

  @Override
  public void run(String... args) throws Exception {
    userSeeders.seed();
  }
}
