package com._119.wepro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WeproApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeproApplication.class, args);
  }

}
