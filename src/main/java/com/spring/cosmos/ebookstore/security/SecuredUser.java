package com.spring.cosmos.ebookstore.security;

import com.spring.cosmos.ebookstore.model.user.Address;
import com.spring.cosmos.ebookstore.model.user.CreditCard;
import com.spring.cosmos.ebookstore.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecuredUser implements UserDetails {
    private User user;

    public SecuredUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstName() {
        return this.user
                .getName()
                .getFirstName();
    }

    public String getLastName() {
        return this.user
                .getName()
                .getLastName();
    }

    public Address getAddress() {
        return this.user.getAddress();
    }

    public void setAddress(Address address){
        this.user.setAddress(address);
    }

    public String getCustomerId(){
        return this.user.getId();
    }

    public  void setCreditCardNumber(CreditCard creditCard){this.user.setCreditCard(creditCard); }

    public CreditCard getCreditCard(){return this.user.getCreditCard();}
}
