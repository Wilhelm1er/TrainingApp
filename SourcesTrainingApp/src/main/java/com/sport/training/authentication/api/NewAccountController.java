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
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;

@Controller
public class NewAccountController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewAccountController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegistryService registryService;
	
	@Autowired
	private SportService sportService;
	
	@GetMapping(path = "/new-athlete")
	public String newAthlete(Model model) {
		final String mname = "newAthlete";
		LOGGER.debug("entering "+mname);
		
		model.addAttribute("userDTO", new UserDTO());
		return "new-athlete";
	}

	@PostMapping(path = "/new-athlete")
	public String createAthlete(@Valid UserDTO userDTO, Model model) {
		final String mname = "createAthlete";
		LOGGER.debug("entering "+mname);
		try {
			userDTO.setRoleName("ROLE_ATHLETE");
			userService.createUser(userDTO);
			model.addAttribute("message","Athlete account created");
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

	@GetMapping(path = "/new-coach")
	public String newCoach(Model model) {
		final String mname = "newCoach";
		LOGGER.debug("entering "+mname);
		model.addAttribute("disciplineDTO", new DisciplineDTO());
		model.addAttribute("userDTO", new UserDTO());
		return "new-coach";
	}

	@PostMapping(path = "/new-coach")
	public String createCoach(@Valid UserDTO userDTO,@Valid DisciplineDTO disciplineDTO, Model model) {
		final String mname = "createCoach";
		LOGGER.debug("entering "+mname);
	
		try {
			userDTO.setRoleName("ROLE_COACH");
			userService.createUser(userDTO);
			registryService.createDisciplineRegistry(new DisciplineRegistryDTO(sportService.findDiscipline(disciplineDTO.getId()),userDTO));
			model.addAttribute("message","Coach account created");
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
