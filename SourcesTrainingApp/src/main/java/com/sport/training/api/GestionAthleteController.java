package com.sport.training.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.CustomUserDetails;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.CreditRegistryDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.service.CoachService;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;

@Controller
public class GestionAthleteController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionAthleteController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CoachService coachService;

	@Autowired
	private SportService sportService;

	@Autowired
	private RegistryService registryService;

	@GetMapping(path = "/bookmark")
	public String showBookmark(Model model, Authentication authentication) throws CreateException {
		final String mname = "showBookmark";
		LOGGER.debug("entering " + mname);

		UserDTO athleteDTO = null;
		List<BookmarkDTO> bookmarkDTOs = new ArrayList<BookmarkDTO>();

		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			athleteDTO = userService.findUser(userDetails.getUsername());
			bookmarkDTOs = coachService.findBookmarksByAthlete(athleteDTO.getUsername());

			model.addAttribute("bookmarkDTOs", bookmarkDTOs);
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}

		return "bookmark";
	}

	@GetMapping(path = "/add-bookmark/{coachId}")
	public String addBookmark(Model model, @PathVariable(value = "coachId") String coachId,
			Authentication authentication) throws CreateException {
		final String mname = "addBookmark";
		LOGGER.debug("entering " + mname);

		UserDTO athleteDTO = null;
		UserDTO coachDTO;
		System.out.println("coachID: " + coachId);

		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			athleteDTO = userService.findUser(userDetails.getUsername());

			coachDTO = userService.findUser(coachId);

			coachService.createBookmark(new BookmarkDTO(athleteDTO, coachDTO));

		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}

		return "bookmark";
	}

	@GetMapping(path = "/athlete-events/{username}")
	public String showEventsByAthlete(Model model, @PathVariable String username) {
		final String mname = "showEventsByAthlete";
		LOGGER.debug("entering " + mname);

		List<EventDTO> eventDTOs = new ArrayList<EventDTO>();
		LocalDateTime dateNow = LocalDateTime.now();

		try {
			eventDTOs = registryService.findEventsByAthlete(username);
			for (EventDTO eventDTO : eventDTOs) {
				// checke si les 30minutes avant event sont depassé pour annuler
				if (eventDTO.getDateTime().minusMinutes(30).isBefore(dateNow)) {
					eventDTO.setVoidable(1);
				}
			}
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("eventDTOs", eventDTOs);

		return "athlete-events";
	}

	@GetMapping("/cancel-event/{eventId}")
	protected String cancelEvent(Model model, @PathVariable Long eventId, Authentication authentication)
			throws FinderException, RemoveException, CreateException {
		final String mname = "cancelEvent";
		LOGGER.debug("entering " + mname);

		// A user must be authenticated
		if (authentication == null) {
			model.addAttribute("exception", "no authenticated user");
			return "error";
		}
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		UserDTO athleteDTO;
		EventRegistryDTO eventReg;
		EventDTO eventDTO = null;
		Double newSolde;
		try {
			athleteDTO = userService.findUser(username);
			eventReg = registryService.findEventRegistryByAthleteAndEvent(userDetails.getUsername(), eventId);
			eventDTO = sportService.findEvent(eventId);
			newSolde = userDetails.getCredit() + eventDTO.getCreditCost();
			userDetails.setCredit(newSolde);
			registryService.createCreditRegistry(new CreditRegistryDTO(athleteDTO, eventDTO.getCreditCost()));
			registryService.deleteEventRegistry(eventReg.getId());

		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		LOGGER.debug("exiting " + mname);
		return "index";
	}

	@GetMapping("/display-event/{eventId}")
	protected String displayEvent(Model model, @PathVariable Long eventId, Authentication authentication)
			throws FinderException, RemoveException, CreateException {
		final String mname = "displayEvent";
		LOGGER.debug("entering " + mname);

		// A user must be authenticated
		if (authentication == null) {
			model.addAttribute("exception", "no authenticated user");
			return "error";
		}
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		UserDTO athleteDTO;
		List<UserDTO> userDTOs;
		EventDTO eventDTO;
		try {
			athleteDTO = userService.findUser(username);
			userDTOs = registryService.findAthleteByEvent(eventId);
			eventDTO = sportService.findEvent(eventId);

		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("athleteDTO", athleteDTO);
		model.addAttribute("eventDTO", eventDTO);
		model.addAttribute("userDTOs", userDTOs);

		LOGGER.debug("exiting " + mname);
		return "display-event";
	}

}
