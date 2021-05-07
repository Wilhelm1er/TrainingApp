package com.sport.training.authentication.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.sport.training.domain.model.Address;
import com.sport.training.domain.model.CreditCard;
import com.sport.training.domain.model.Discipline;


/**
 * This class represents a user of the training Application.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_USER") 
public class User implements Serializable {

	// ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@Column(name="ID")
	@NotBlank(message = "username must be defined")
	private String username;
	
	@NotBlank(message = "Invalid user first name")
    private String firstname;
	
	@NotBlank(message = "Invalid user name")
    private String lastname;
	
	@Size(max=10, message="telephone nb's length exception (10 char.max)")
    private String telephone;
    
    private String email;
    
	@Size(min=4, message="password's length exception (mini. of 4 char. required)")
	private String password;
	
	private int credit;
	
	private String statut;
	
    @Embedded
    private Address address = new Address();
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_FK", referencedColumnName="ID")
	private Role role;

	// ======================================
    // =            Constructors            =
    // ======================================
    
    public User() {}

    public User(final String username) {
    	this.username=username;
    }

    public User(final String username, final String firstname, final String lastname) {
    	this.username=username;
    	this.firstname = firstname;
    	this.lastname = lastname;
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
    public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
    	this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
    	this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(final String telephone) {
    	this.telephone = telephone;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit= credit;
	}
	
    public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	public String getAddress1() {
        return address.getAddress1();
    }

    public void setAddress1(final String address1) {
        address.setAddress1(address1);
    }

    public String getAddress2() {
        return address.getAddress2();
    }

    public void setAddress2(final String address2) {
        address.setAddress2(address2);
    }

    public String getCity() {
        return address.getCity();
    }

    public void setCity(final String city) {
        address.setCity(city);
    }

    public String getState() {
        return address.getState();
    }

    public void setState(final String state) {
        address.setState(state);
    }

    public String getZipcode() {
        return address.getZipcode();
    }

    public void setZipcode(final String zipcode) {
        address.setZipcode(zipcode);
    }

    public String getCountry() {
        return address.getCountry();
    }

    public void setCountry(final String country) {
        address.setCountry(country);
    }
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("\n\tUser {");
        buf.append("\n\t\tId=").append(getUsername());
        buf.append("\n\t\tFirst Name=").append(getFirstname());
        buf.append("\n\t\tLast Name=").append(getLastname());
        buf.append("\n\t\tTelephone=").append(getTelephone());
        buf.append("\n\t\temail=").append(getEmail());
        buf.append(",address1=").append(getAddress1());
        buf.append(",address2=").append(getAddress2());
        buf.append(",city=").append(getCity());
        buf.append(",state=").append(getState());
        buf.append(",zipcode=").append(getZipcode());
        buf.append(",country=").append(getCountry());
        buf.append(",Solde de crédit=").append(getCredit());
        buf.append("\n\t}");
        return buf.toString();
    }
}
