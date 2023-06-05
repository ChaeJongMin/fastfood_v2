package com.example.demo.config.auth.jwt.UserData;

import com.example.demo.domain.Customer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetail implements UserDetails {
    private Customer customer;
    private int id;
    public CustomUserDetail(Customer customer){
        this.customer=customer;
        this.id=customer.getId();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(this.customer.getRole().getKey()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.customer.getUserPasswd();
    }

    @Override
    public String getUsername() {
        return this.customer.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
