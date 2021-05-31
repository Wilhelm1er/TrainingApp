package com.sport.training.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.RoleService;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.exception.FinderException;

/**
 * This servlet displays user according to their role.
 */
@Controller
public class FindUserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindUserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Secured("ROLE_ADMIN")
	@GetMapping("/display-coachs")
	public String showCoachs(Model model) {
		final String mname = "showCoachs";
		LOGGER.debug("entering " + mname);

		List<UserDTO> coachDTOs = null;
		try {
			coachDTOs = userService.findUsersByRole(roleService.findByRoleName("ROLE_COACH"));
		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("coachDTOs", coachDTOs);
		return "display-users";
	}

	@Secured({ "ROLE_ADMIN" })
	@GetMapping("/display-athletes")
	public String showAthletes(Model model) {
		final String mname = "showAthletes";
		LOGGER.debug("entering " + mname);

		List<UserDTO> athleteDTOs = null;
		try {
			athleteDTOs = userService.findUsersByRole(roleService.findByRoleName("ROLE_ATHLETE"));
		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("athleteDTOs", athleteDTOs);
		return "display-users";
	}

}