package com.train.sports.authentication.domain.dto;

import java.io.Serializable;

import com.train.sports.domain.dto.AddressDTO;
import com.train.sports.domain.dto.CreditCardDTO;

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
    private int credit;
	private AddressDTO addressDTO = new AddressDTO();
    private CreditCardDTO creditCardDTO = new CreditCardDTO();
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
    
    public CreditCardDTO getCreditCardDTO() {
		return creditCardDTO;
	}
    
    public void setCreditCardDTO(CreditCardDTO creditCardDTO) {
		this.creditCardDTO=creditCardDTO;
	}

    public String getCreditCardNumber() {
        return creditCardDTO.getCreditCardNumber();
    }

    public void setCreditCardNumber( String creditCardNumber) {
        creditCardDTO.setCreditCardNumber(creditCardNumber);
    }

    public String getCreditCardType() {
        return creditCardDTO.getCreditCardType();
    }

    public void setCreditCardType( String creditCardType) {
        creditCardDTO.setCreditCardType(creditCardType);
    }

    public String getCreditCardExpiryDate() {
        return creditCardDTO.getCreditCardExpiryDate();
    }

    public void setCreditCardExpiryDate( String creditCardExpiryDate) {
        creditCardDTO.setCreditCardExpiryDate(creditCardExpiryDate);
    }
    
    public int getCredit() {
  		return credit;
  	}

  	public void setCredit(int credit) {
  		this.credit = credit;
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
        buf.append("CustomerDTO{");
        buf.append("id=").append(getUsername());
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
        buf.append(",creditCardNumber=").append(getCreditCardNumber());
        buf.append(",creditCardType=").append(getCreditCardType());
        buf.append(",creditCardExpiryDate=").append(getCreditCardExpiryDate());
        buf.append(",Solde de crédit=").append(getCredit());
        buf.append('}');
        return buf.toString();
    }
}
