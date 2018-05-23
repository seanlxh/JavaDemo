package com.example.demo.Config;

import org.apache.shiro.authc.AuthenticationToken;

public class ShiroToken implements AuthenticationToken {
    private String principal;

    private String credetials;

    public ShiroToken(String principal, String credetials) {
        this.principal = principal;
        this.credetials = credetials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credetials;
    }
}
