package com.sport.training.api;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.exception.FinderException;

/**
 * This servlet returns the list of all products for a specific category.
 */
@Controller
public class FindCoachController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindCoachController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/find-coachs")
	protected String findCoachs(Model model, @RequestParam String disciplineId) {
		final String mname = "findCoachs";
		LOGGER.debug("entering " + mname);

		Set<UserDTO> coachDTOs = null;
		try {
			coachDTOs = userService.findCoachsByDiscipline(disciplineId);

		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("coachDTOs", coachDTOs);

		return "coachs";
	}

}
