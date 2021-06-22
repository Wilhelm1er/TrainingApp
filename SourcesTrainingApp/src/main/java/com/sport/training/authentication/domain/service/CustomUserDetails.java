package com.sport.training.authentication.domain.service;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private Double credit;
	private String statut;
	private List<SimpleGrantedAuthority> grantedAuthorities;

	public CustomUserDetails(String username, String password, List<SimpleGrantedAuthority> grantedAuthorities, Double credit, String statut) {
		this.username=username;
		this.password=password;
		this.grantedAuthorities=grantedAuthorities;
		this.credit=credit;
		this.statut=statut;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {;
	    return grantedAuthorities;
	}

	@Override
	public String getPassword() {
	    return this.password;
	}

	@Override
	public String getUsername() {
	    return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		//if(statut.equals("INVALIDE")) {
		//	return false;
		//}
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

	public Double getCredit() {
	    return this.credit;
	}
	public Double setCredit(Double credit) {
	    return this.credit = credit;
	}
	
	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

}
