package utc.miage.tp.tmdb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TmdbConfig {

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
