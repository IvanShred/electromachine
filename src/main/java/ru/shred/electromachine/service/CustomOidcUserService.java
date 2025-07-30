//package ru.shred.electromachine.service;
//
///**
// * Created by KuhtaIA on 30.07.2025
// */
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
//import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class CustomOidcUserService extends OidcUserService {
//
//    @Override
//    public OidcUser loadUser(OidcUserRequest userRequest) {
//        OidcUser oidcUser = super.loadUser(userRequest);
//
//        // Извлекаем роли из claims
//        Map<String, Object> claims = oidcUser.getClaims();
//        Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);
//
//        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
//    }
//
//    private Collection<? extends GrantedAuthority> extractAuthorities(Map<String, Object> claims) {
//        List<String> roles = (List<String>) claims.getOrDefault("roles", Collections.emptyList());
//
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                .collect(Collectors.toList());
//    }
//}
//
