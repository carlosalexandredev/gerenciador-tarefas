package com.example.demo.model.cofing.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/funcionario").access("hasAnyAuthority('USER', 'ADMIN')")
                .antMatchers("/*").access("hasAuthority('ADMIN')")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/funcionario", true).permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("Marcelo")
                .password(passwordEncoder().encode("12345"))
                .authorities("ADMIN")

                .and()
                .withUser("user1")
                .password(passwordEncoder().encode("12345"))
                .authorities("USER")

                .and()
                .withUser("user2")
                .password(passwordEncoder().encode("12345"))
                .authorities("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
    }
}