package com.example.paymentsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Настройка BCryptPasswordEncoder как PasswordEncoder
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF защиты
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/api/users/**").permitAll() // Разрешить доступ к /api/users/** для всех
                        //.anyRequest().authenticated() // Все остальные запросы должны быть аутентифицированы
                        .anyRequest().permitAll() // Разрешить доступ ко всем запросам без аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Указание URL страницы входа
                        .permitAll() // Разрешить всем доступ к странице входа
                )
                .logout(logout -> logout
                        .permitAll() // Разрешить всем доступ к странице выхода
                );

        return http.build();
    }
}
