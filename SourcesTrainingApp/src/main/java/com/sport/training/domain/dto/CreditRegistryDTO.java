package com.sport.training.domain.dto;

import java.io.Serializable;
import java.sql.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of an Event registration. This class only transfers data from a distant
 * service to a client.
 */
@SuppressWarnings("serial")
public class CreditRegistryDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private Date mouvementDate;
	private UserDTO userDTO;
	private int credit;

	// ======================================
	// = Constructors =
	// ======================================
	public CreditRegistryDTO(final UserDTO userDTO, final int credit) {
		setUserDTO(userDTO);
		setCredit(credit);
	}

	// ======================================
	// = Getters and Setters =
	// ======================================
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getMouvementDate() {
		return mouvementDate;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Credit RegistryDTO{");
		buf.append("id=").append(getId());
		buf.append(",mouvementDate=").append(getMouvementDate());
		buf.append(",credit=").append(getCredit());
		buf.append(",userID=").append(getUserDTO().getUsername());
		buf.append('}');
		return buf.toString();
	}
}
