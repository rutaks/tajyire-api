package rw.tajyire.api.config;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfigurer {
  @Value("${cloudinary.cloud_name}")
  private String cloudName;

  @Value("${cloudinary.api_key}")
  private String apiKey;

  @Value("${cloudinary.api_secret}")
  private String apiSecret;

  /**
   * This method initialize an object of {@link Cloudinary} by configuring <code>api_secret</code>,
   * <code>api_key</code> and <code>cloud_name</code>.
   *
   * @return a configured {@link Cloudinary}
   */
  @Bean
  public Cloudinary cloudinaryConfig() {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", cloudName);
    config.put("api_key", apiKey);
    config.put("api_secret", apiSecret);
    return new Cloudinary(config);
  }
}