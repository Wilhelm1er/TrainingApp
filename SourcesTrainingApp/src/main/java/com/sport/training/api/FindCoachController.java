package com.sport.training.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.exception.FinderException;

/**
 * This servlet returns the list of all products for a specific category.
 */
@Controller
public class FindCoachController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindCoachController.class);

	@Autowired
	private RegistryService registryService;

	@GetMapping("/find-coachs")
	protected String findCoachs(Model model, @RequestParam String disciplineId) {
		final String mname = "findCoachs";
		LOGGER.debug("entering " + mname);

		List<UserDTO> coachDTOs = null;
		try {
			coachDTOs = registryService.findCoachsByDiscipline(disciplineId);
			
		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("coachDTOs", coachDTOs);

		return "coachs";
	}

}
