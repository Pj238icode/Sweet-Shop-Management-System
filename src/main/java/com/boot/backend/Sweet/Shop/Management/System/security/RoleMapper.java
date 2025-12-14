package com.boot.backend.Sweet.Shop.Management.System.security;

import com.boot.backend.Sweet.Shop.Management.System.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class RoleMapper {
    public static Collection<? extends GrantedAuthority> map(Role role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
