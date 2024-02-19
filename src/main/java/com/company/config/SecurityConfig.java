package com.company.config;

import com.company.enums.ProfileRoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // responsible for handling HTTP requests and applying security measures based on the provided configuration
    // details of what roles should a user must have when accessing certain endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/api/articles/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/articles/publisher/**").hasRole("PUBLISHER")
                .anyRequest()
                .authenticated()
                .and().httpBasic()
                .and().formLogin();
        return  httpSecurity.build();
    }

    // for login
    @Bean
    public AuthenticationProvider authenticationProvider(){
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsManager());
        //authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    // Creates a user in memory of PC and returns it
    // initial user details for testing or development purposes
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){

        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}admin123")
                .roles("ADMIN")
                .build();
        UserDetails publisher = User.builder()
                .username("publisher")
                .password("{noop}publisher123")
                .roles("PUBLISHER")
                .build();


        return new InMemoryUserDetailsManager(admin, publisher);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
