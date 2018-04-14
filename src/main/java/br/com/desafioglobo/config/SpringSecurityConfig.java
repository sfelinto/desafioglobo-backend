package br.com.desafioglobo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
		
        httpSecurity.csrf().ignoringAntMatchers("/h2-console").disable()
                .authorizeRequests().antMatchers("/**/favicon.ico").permitAll()
                .and().authorizeRequests().antMatchers("/contato/**").permitAll()
                .and().authorizeRequests().antMatchers("/webjars/**").permitAll()
                .and().authorizeRequests().antMatchers("/static/css").permitAll()
                .and().authorizeRequests().antMatchers("/js").permitAll()
                .and().exceptionHandling().accessDeniedPage("/access_denied");
    }

}
