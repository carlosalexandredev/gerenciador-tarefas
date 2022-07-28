package com.example.demo.model.cofing.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * @Method  passwordEncoder()
     * @Description Método de criptografia de senha
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @Method configure(HttpSecurity http)
     * @Description Método de configuração de acesso de usuário.
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
//      http.csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/").permitAll()
//                .antMatchers(HttpMethod.GET, "/usuario").hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST, "/usuario").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT, "/usuario").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/usuario").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and().formLogin().permitAll()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/funcionario").access("hasAnyAuthority('USER', 'ADMIN')")
                .antMatchers("/admin").access("hasAuthority('ADMIN')")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/funcionario", true).permitAll()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }

    /**
     * @Method configure(AuthenticationManagerBuilder auth)
     * @Description Método de configuração das credencias de acesso de usuário.
     * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("david")
                .password(passwordEncoder().encode("123"))
                .authorities("ADMIN")

                .and()
                .withUser("user")
                .password(passwordEncoder().encode("123"))
                .authorities("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
//        web.ignoring().antMatchers("/static/**");
    }
}