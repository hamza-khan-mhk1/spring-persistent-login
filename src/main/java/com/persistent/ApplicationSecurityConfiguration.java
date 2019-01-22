package com.persistent;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated()
						.and()
							.formLogin()
								.loginPage("/login")
								.permitAll()
						.and()
							.rememberMe()
								.rememberMeCookieName("remember-me-cookie")
								.tokenValiditySeconds(24 * 60 * 60) // token expiry time: 1 day   --- default is 2 weeks
								.tokenRepository(persistentTokenRepository())
						.and()
							.logout().logoutSuccessUrl("/login")
							.permitAll();
	/*
     * 
     * This solution – the PersistentTokenBasedRememberMeServices uses a unique series identifier for the user.
     *  This identifies the initial login of the user and remains constant each time the user gets logged in automatically
     *   during that persistent session. It also contains a random token that is regenerated each time a user logs in via 
     *   the persisted remember-me functions.
     *   This combination of randomly generated series and token are persisted, making a brute force attack very unlikely.			 
     * 
     */
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
		 
	}	
}