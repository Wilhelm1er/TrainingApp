package com.sport.training.authentication.domain.dto;

import java.io.Serializable;

import com.sport.training.domain.dto.AddressDTO;

/**
 * This class follows the Data Transfert Object design pattern.
 * It is a client view of a User. This class only
 * transfers data from a distant service to a client.
 */
@SuppressWarnings("serial")
public class UserDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String username;
    private String firstname;
    private String lastname;
    private String telephone;
    private String email;
    private String password;
    private Double credit;
    private String statut;
	private AddressDTO addressDTO = new AddressDTO();
    private String roleName;


	// ======================================
    // =            Constructors            =
    // ======================================
    public UserDTO() {
    }
    
    public UserDTO(final String username) {
    	this.username=username;
    }

    public UserDTO(final String username, final String firstname, final String lastname) {
        setUsername(username);
        setFirstname(firstname);
        setLastname(lastname);
        setCredit(0.0);
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

    public void setTelephone( String telephone) {
    	this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
    	this.email = email;
    }
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress1() {
        return addressDTO.getAddress1();
    }

    public void setAddress1( String address1) {
        addressDTO.setAddress1(address1);
    }

    public String getAddress2() {
        return addressDTO.getAddress2();
    }

    public void setAddress2( String address2) {
        addressDTO.setAddress2(address2);
    }

    public String getCity() {
        return addressDTO.getCity();
    }

    public void setCity( String city) {
        addressDTO.setCity(city);
    }

    public String getState() {
        return addressDTO.getState();
    }

    public void setState( String state) {
        addressDTO.setState(state);
    }

    public String getZipcode() {
        return addressDTO.getZipcode();
    }

    public void setZipcode( String zipcode) {
        addressDTO.setZipcode(zipcode);
    }

    public String getCountry() {
        return addressDTO.getCountry();
    }

    public void setCountry( String country) {
        addressDTO.setCountry(country);
    }
    
    public Double getCredit() {
  		return credit;
  	}

  	public void setCredit(Double credit) {
  		this.credit = credit;
  	}
  	
	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

    @Override
	public String toString() {
         StringBuffer buf = new StringBuffer();
        buf.append("UserDTO{");
        buf.append("username=").append(getUsername());
        buf.append(",firstname=").append(getFirstname());
        buf.append(",lastname=").append(getLastname());
        buf.append(",telephone=").append(getTelephone());
        buf.append(",email=").append(getEmail());
        buf.append(",address1=").append(getAddress1());
        buf.append(",address2=").append(getAddress2());
        buf.append(",city=").append(getCity());
        buf.append(",state=").append(getState());
        buf.append(",zipcode=").append(getZipcode());
        buf.append(",country=").append(getCountry());
        buf.append(",Solde de crédit=").append(getCredit());
        buf.append('}');
        return buf.toString();
    }
}
