package com.sport.training.authentication.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;

@Controller
public class NewAccountController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewAccountController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping(path = "/new-account")
	public String newAccount(Model model) {
		final String mname = "newAccount";
		LOGGER.debug("entering "+mname);
		
		model.addAttribute("userDTO", new UserDTO());
		return "new-account";
	}

	@PostMapping(path = "/new-account")
	public String createAccount(@Valid UserDTO userDTO, Model model) {
		final String mname = "createAccount";
		LOGGER.debug("entering "+mname);

		try {
			userService.createUser(userDTO);
			model.addAttribute("message","account created");
			return "index";
		} catch (CreateException e) {
			if(e instanceof DuplicateKeyException)
				model.addAttribute("exception", "this id is already assigned");
			else
				model.addAttribute("exception", e.getMessage());
			return "error";
		} catch(Exception exc) {
			model.addAttribute("exception", exc.getMessage());
			return "error";
		}
	}

}
