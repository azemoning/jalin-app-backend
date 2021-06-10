package com.jalin.jalinappbackend.module.authentication.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jalin.jalinappbackend.module.authentication.entity.AuthorityEnum.BANKING_READ;
import static com.jalin.jalinappbackend.module.authentication.entity.AuthorityEnum.BANKING_WRITE;

public enum RoleEnum {
    ADMIN(new HashSet<>(Arrays.asList(BANKING_READ, BANKING_WRITE))),
    USER(new HashSet<>(Arrays.asList(BANKING_READ)));

    private final Set<AuthorityEnum> authorities;

    RoleEnum(Set<AuthorityEnum> authorities) {
        this.authorities = authorities;
    }

    public Set<AuthorityEnum> getAuthorities() {
        return authorities;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
