package com.example.springboot.security;

import javax.servlet.Filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .cors()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .addFilter(authorizationFilter())
            .authorizeRequests()
                .mvcMatchers("/ping").permitAll()
                .mvcMatchers("/users/list").hasRole("ADMIN")
                .mvcMatchers("/users/**").hasRole("USER")
                .anyRequest().authenticated()
        .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable();
        // @formatter:on
    }

    private Filter authorizationFilter() throws Exception {
        return new MyAuthorizationFilter(this.authenticationManager());
    }
}
