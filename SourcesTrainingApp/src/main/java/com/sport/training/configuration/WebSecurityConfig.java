package com.sport.training.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import com.sport.training.domain.service.ShoppingCartService;
import com.sport.training.domain.service.ShoppingCartServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true) // permettra @Secured ({"ROLE_ATHLETE"}) ...
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final String mname = "configure";
		LOGGER.debug("entering "+mname);
		
		String[] staticResources = { "/img/**"};

        http
        	.csrf()
        		.and()
	        .formLogin()
	        	.loginPage("/login")				// the custom login page
	        	.defaultSuccessUrl("/")				// the landing page after a successful login
	        	.and()								// possible : failureUrl() : the landing page after an unsuccessful login
	        .logout()								// must be a POST with csrf on
	        	.logoutSuccessUrl("/")
	        	.and()
	        .authorizeRequests()
	        	.antMatchers(staticResources).permitAll()
	        	.antMatchers("/", "/login","/new-athlete","/new-coach","/find-activities","/find-events","/find-event","/find-coachs","/disciplines","/discipline/*","/activities","/activities/*","/activity/*","/coachs","/credit","/create-activity/*","/create-event/*","/events/*").permitAll()
	        	.anyRequest().authenticated();
	}
	
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // https://www.baeldung.com/spring-mvc-session-attributes
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ShoppingCartService shoppingCartService() {
        return new ShoppingCartServiceImpl();
    }    
}
