package com.jalin.jalinappbackend.module.authentication.entity;

public enum AuthorityEnum {
    BANKING_READ("banking:read"),
    BANKING_WRITE("banking:write");

    private final String authority;

    AuthorityEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
