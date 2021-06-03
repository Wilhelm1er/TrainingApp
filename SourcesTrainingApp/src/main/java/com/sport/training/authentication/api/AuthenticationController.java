package com.sport.training.authentication.api;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.UpdateException;

@Controller
public class AuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RegistryService registryService;

	@Autowired
	private SportService sportService;

	@GetMapping(path = "/account/{username}")
	public String showAccount(Model model, Authentication authentication) {
		final String mname = "showAccount";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;
		List<DisciplineDTO> disciplineDTOs = null;
		Set<DisciplineDTO> disciplineDTOsCoach = null;
		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userDTO = userService.findUser(userDetails.getUsername());
			disciplineDTOsCoach = registryService.findDisciplinesByCoach(userDTO.getUsername());
			disciplineDTOs = sportService.findDisciplines();
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("disciplineDTOsCoach", disciplineDTOsCoach);
		model.addAttribute("disciplineDTOs", disciplineDTOs);

		return "account";
	}
	
	@GetMapping(path = "/update-account/{username}")
	public String showUpdateAccount(Model model, @PathVariable String username) {
		final String mname = "showUpdateAccount";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;
		List<DisciplineDTO> disciplineDTOs = null;
		Set<DisciplineDTO> disciplineDTOsCoach = null;
		try {
			userDTO = userService.findUser(username);
			disciplineDTOsCoach = registryService.findDisciplinesByCoach(username);
			disciplineDTOs = sportService.findDisciplines();
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("disciplineDTOsCoach", disciplineDTOsCoach);
		model.addAttribute("disciplineDTOs", disciplineDTOs);

		return "update-account";
	}

	@PostMapping(path = "/update-account")
	public String updateAccount(@Valid @ModelAttribute UserDTO userDTO,
			@RequestParam(value = "discipline.id", required = false) String[] disciplineId, BindingResult bindingResult,
			Model model) {
		final String mname = "updateAccount";
		LOGGER.debug("entering " + mname);
		try {
			userService.updateUser(userDTO);
			for (String disciplineIdChecked : disciplineId) {
				registryService.createDisciplineRegistry(
						new DisciplineRegistryDTO(sportService.findDiscipline(disciplineIdChecked), userDTO));
			}
			model.addAttribute("message", "account updated");
			return "index";
		} catch (UpdateException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		} catch (Exception exc) {
			model.addAttribute("exception", exc.getMessage());
			return "error";
		}
	}
}
