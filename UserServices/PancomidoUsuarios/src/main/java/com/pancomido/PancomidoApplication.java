package com.pancomido;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PancomidoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PancomidoApplication.class, args);
	}


	// Bean global de RestTemplate para usar con @Autowired
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
