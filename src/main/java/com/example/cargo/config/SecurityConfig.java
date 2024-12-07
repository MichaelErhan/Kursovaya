package com.example.cargo.config;

import com.example.cargo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Настраиваем шифрование паролей
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Настраиваем менеджер аутентификации (автоматически подхватит UserDetailsService)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/error/403"); // Перенаправляем на кастомную страницу
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin_panel/**").hasRole("ADMIN") // Только для админа
                        .requestMatchers("/moderate/**").hasAnyRole("ADMIN", "MODERATOR") // Для модераторов и админов
                        .requestMatchers("/blog/admin/**").hasAnyRole("ADMIN", "MODERATOR") // Для модераторов и админов
                        .requestMatchers("/cargolist/**").authenticated() // Только для авторизованных пользователей
                        .requestMatchers("/blog/**").authenticated() // Только для авторизованных пользователей
                        .requestMatchers("/").authenticated() // Только для авторизованных пользователей
                        .anyRequest().permitAll() // Все остальные запросы доступны всем
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Кастомная страница логина
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .csrf(AbstractHttpConfigurer::disable  // Отключение CSRF-защиты
                );

        return http.build();
    }
}
