package br.com.desafioglobo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("br.com.desafioglobo.repositories")
public class JPAConfig {
	
}
