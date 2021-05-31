package com.sport.training.api;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.service.CoachService;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.exception.FinderException;

@Controller
public class GestionAthleteController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionAthleteController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CoachService coachService;

	@Autowired
	private RegistryService registryService;

	@GetMapping(path = "/bookmark/{username}")
	public String showBookmark(Model model, @PathVariable String username) {
		final String mname = "showBookmark";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;
		List<BookmarkDTO> bookmarkDTOs = null;
		try {
			userDTO = userService.findUser(username);
			bookmarkDTOs = coachService.findBookmarksByAthlete(username);
			
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("bookmarkDTOs", bookmarkDTOs);

		return "bookmark";
	}
	
	@GetMapping(path = "/sessions/{username}")
	public String showSessionsByAthlete(Model model, @PathVariable String username) {
		final String mname = "showSessionsByAthlete";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;
		List<EventRegistryDTO> eventregistryDTOs = null;
		try {
			userDTO = userService.findUser(username);
			eventregistryDTOs = registryService.findEventRegistriesByAthlete(username);
			
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("eventRegistryDTOs", eventregistryDTOs);

		return "bookmark";
	}
}
