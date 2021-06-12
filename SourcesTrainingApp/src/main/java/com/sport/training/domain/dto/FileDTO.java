package com.sport.training.domain.dto;

import java.util.Arrays;
import java.util.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

public class FileDTO {
	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private Date date;
	private String name;
	private String type;
	private UserDTO userDTO;
	private byte[] data;

	// ======================================
	// = Constructors =
	// ======================================
	public FileDTO() {
		id = 0L;
		this.date = new Date();
		userDTO = new UserDTO();
	}

	public FileDTO(String name, String type, UserDTO userDTO, byte[] data) {
		setName(name);
		setType(type);
		setData(data);
		setUserDTO(userDTO);
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "FileDTO [id=" + id + ", date=" + date + ", name=" + name + ", type=" + type + ", userDTO=" + userDTO
				+ ", data=" + Arrays.toString(data) + "]";
	}

}
