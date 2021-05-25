package com.sport.training.api;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.exception.FinderException;

@Controller
public class GestionCreditController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GestionCreditController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private RegistryService registryService;
	
	@GetMapping(path = "/credit/{username}")
	public String showCreditByUser(Model model, @PathVariable String username) throws FinderException {
		final String mname = "showCreditByUser";
		LOGGER.debug("entering "+mname);
		
		UserDTO userDTO;
		Map<Date, Integer> creditRegistryList = null;
		
		try {
			userDTO = userService.findUser(username);
			creditRegistryList = registryService.findDateAndCreditByUser(username);
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("creditRegistryList", creditRegistryList);

		return "credit";
	}

}
