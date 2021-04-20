package com.train.sports.authentication.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.train.sports.authentication.domain.dto.UserDTO;
import com.train.sports.authentication.domain.service.UserService;
import com.train.sports.exception.FinderException;
import com.train.sports.exception.UpdateException;

@Controller
public class AuthenticationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private UserService userService;
	
	@GetMapping(path = "/update-account/{username}")
	public String showAccount(Model model, @PathVariable String username) {
		final String mname = "showAccount";
		LOGGER.debug("entering "+mname);
		
		UserDTO userDTO;
		try {
			userDTO = userService.findUser(username);
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		return "update-account";
	}

	@PostMapping(path = "/update-account")
	public String updateAccount(@Valid @ModelAttribute UserDTO userDTO, Model model) {
		final String mname = "updateAccount";
		LOGGER.debug("entering "+mname);
		try {
			userService.updateUser(userDTO);
			 model.addAttribute("message","account updated");
			return "index";
		} catch (UpdateException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		} catch(Exception exc) {
			model.addAttribute("exception", exc.getMessage());
			return "error";
		}
	}
	
}
