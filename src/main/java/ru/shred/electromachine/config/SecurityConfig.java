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

//import com.vaadin.flow.spring.security.VaadinWebSecurity;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends VaadinWebSecurity {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors(withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/**").permitAll()
//                        .requestMatchers("/**").permitAll());
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("*"));
//        configuration.setAllowedMethods(List.of("*"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/VAADIN/**",
                                "/frontend/**",
                                "/webjars/**",
                                "/images/**",
                                "/favicon.ico",
                                "/login/oauth2/**",
                                "/oauth2/authorization/**",
                                "/?v-r=**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(customizer -> customizer
                                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        /*.jwt(Customizer.withDefaults())*/)
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(this.oauth2UserAuthoritiesMapper())
                        )
                        .authorizationEndpoint(authEndpoint -> authEndpoint
                                .authorizationRequestResolver(pkceResolver(clientRegistrationRepository))))
                .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository)))
                .csrf(csrf -> csrf.disable());
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()));
        return http.build();
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    private OAuth2AuthorizationRequestResolver pkceResolver(ClientRegistrationRepository clientRegistrationRepository) {
        var resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        return resolver;
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        var logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

        return logoutSuccessHandler;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        var authConverter = new JwtAuthenticationConverter();
        authConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authConverter;
    }

    // Маппер ролей для OAuth2 Login
    private GrantedAuthoritiesMapper oauth2UserAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OAuth2UserAuthority oauth2Authority) {
                    Map<String, Object> attributes = oauth2Authority.getAttributes();

                    if (attributes.containsKey("roles")) {
                        try {
                            @SuppressWarnings("unchecked")
                            List<String> roles = (List<String>) attributes.get("roles");
                            roles.forEach(role ->
                                    mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                            );
                        } catch (ClassCastException e) {
                            log.warn("Failed to parse roles claim", e);
                        }
                    }
                }
            });

            return mappedAuthorities;
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(true);
    }
}