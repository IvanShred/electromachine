package ru.shred.electromachine.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                //.requestCache(cache -> cache.requestCache(new CustomRequestCache()))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login").permitAll() // Разрешаем доступ к Vaadin-странице
//                        //.requestMatchers(new RouteRequestMatcher()).permitAll() // Для Vaadin internal routes
//                        //.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
//                        .requestMatchers("/azur/**").hasRole("ADMIN")
//                        .requestMatchers(
//                                "/VAADIN/**",
//                                "/favicon.ico",
//                                "/robots.txt",
//                                "/manifest.webmanifest",
//                                "/frontend/**",
//                                "/webjars/**",
//                                "/h2-console/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        var authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder);
//        return new ProviderManager(authProvider);
//    }
//}

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Разрешаем доступ ко всем URL
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()
        );

        // Отключаем CSRF для Vaadin
        http.csrf(csrf -> csrf.disable());

        super.configure(http);
    }
}