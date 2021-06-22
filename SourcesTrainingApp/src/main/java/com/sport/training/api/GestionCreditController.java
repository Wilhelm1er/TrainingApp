package com.sport.training.api;

import java.util.Date;
import java.util.Map;

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
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.CreditRegistryDTO;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.exception.CreateException;
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
		LOGGER.debug("entering " + mname);

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

	@GetMapping(path = "/add-credit/{username}")
	public String showAddCredit(Model model, @PathVariable String username) throws FinderException {
		final String mname = "addCredit";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;

		try {
			userDTO = userService.findUser(username);
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);

		return "add-credit";
	}

	@GetMapping(path = "/withdraw-credit/{username}")
	public String showWithdrawCredit(Model model, @PathVariable String username) throws FinderException {
		final String mname = "withdrawCredit";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;

		try {
			userDTO = userService.findUser(username);
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);

		return "withdraw-credit";
	}

	@PostMapping(path = "/add-credit")
	public String addCredit(@Valid @ModelAttribute UserDTO userDTO, @Valid @ModelAttribute Double addCredit,
			Model model) {
		final String mname = "addCredit";
		LOGGER.debug("entering " + mname);
		try {
			registryService.createCreditRegistry(new CreditRegistryDTO(userDTO, addCredit));

			model.addAttribute("message", "credit added");
			return "credit";
		} catch (CreateException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}

	@PostMapping(path = "/withdraw-credit")
	public String withdrawCredit(@Valid @ModelAttribute UserDTO userDTO, @Valid @ModelAttribute Double withdrawCredit,
			Model model) {
		final String mname = "withdrawCredit";
		LOGGER.debug("entering " + mname);
		try {
			registryService.createCreditRegistry(new CreditRegistryDTO(userDTO, withdrawCredit));

			model.addAttribute("message", "credit withdrawed");
			return "credit";
		} catch (CreateException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}
}
