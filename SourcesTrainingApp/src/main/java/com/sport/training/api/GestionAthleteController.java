package com.sport.training.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.service.CoachService;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.exception.CreateException;
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

	@GetMapping(path = "/bookmark")
	public String showBookmark(Model model, Authentication authentication) {
		final String mname = "showBookmark";
		LOGGER.debug("entering " + mname);

		List<BookmarkDTO> bookmarkDTOs = null;
		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			bookmarkDTOs = coachService.findBookmarksByAthlete(userDetails.getUsername());

		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("bookmarkDTOs", bookmarkDTOs);

		return "bookmark";
	}

	@PostMapping(path = "/bookmark")
	public String addBookmark(Model model, @Valid @ModelAttribute("coachDTO") UserDTO coachDTO, Authentication authentication)
			throws CreateException {
		final String mname = "addBookmark";
		LOGGER.debug("entering " + mname);

		List<BookmarkDTO> bookmarkDTOs = null;
		UserDTO userDTO;
		
		System.out.println("coach: "+coachDTO);

		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userDTO = userService.findUser(userDetails.getUsername());
			bookmarkDTOs = coachService.findBookmarksByAthlete(userDetails.getUsername());
			for (BookmarkDTO bookmark : bookmarkDTOs) {
				if (bookmark.getCoachDTO() == null) {
					coachService.createBookmark(new BookmarkDTO(userDTO, coachDTO));
				}
			}

		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("bookmarkDTOs", bookmarkDTOs);

		return "bookmark";
	}

	@GetMapping(path = "/athlete-events/{username}")
	public String showEventsByAthlete(Model model, @PathVariable String username) {
		final String mname = "showEventsByAthlete";
		LOGGER.debug("entering " + mname);

		List<EventRegistryDTO> eventregistryDTOs;
		List<EventDTO> eventDTOs = new ArrayList<EventDTO>();
		try {
			eventregistryDTOs = registryService.findEventRegistriesByAthlete(username);
			if(eventregistryDTOs!=null) {
			for (EventRegistryDTO eventReg : eventregistryDTOs) {
				eventDTOs.add(eventReg.getEventDTO());
				System.out.println("event: "+eventReg.getEventDTO());
			}}

		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("eventDTOs", eventDTOs);

		return "athlete-events";
	}
}
