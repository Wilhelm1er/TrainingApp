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
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.FinderException;

/**
 * These servlets returns the selected event / events.
 */
@Controller
public class FindEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindEventController.class);

	@Autowired
	private SportService sportService;

	@Autowired
	private UserService userService;

	@GetMapping("/find-event")
	protected String findEvent(Model model, @RequestParam Long eventId, Authentication authentication) {
		final String mname = "findEvent";
		LOGGER.debug("entering " + mname);
		try {
			EventDTO eventDTO = sportService.findEvent(eventId);
			if (authentication != null) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				UserDTO userDTO = userService.findUser(userDetails.getUsername());
				if (userDTO.getRoleName().equals("ROLE_ATHLETE"))
					model.addAttribute("username", userDetails.getUsername());
			}
			model.addAttribute("eventDTO", eventDTO);
			return "event";
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}

	@GetMapping(path = "/find-events/{username}")
	public String showEventsByCoach(Model model, @PathVariable String username) throws FinderException {
		final String mname = "showEventsByCoach";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;

		List<EventDTO> oldEventDTOs = null;
		List<EventDTO> eventDTOs = new ArrayList<EventDTO>();

		try {
			userDTO = userService.findUser(username);
			oldEventDTOs = sportService.findEvents(username);
			LocalDateTime dateNow = LocalDateTime.now();
			for (EventDTO e : oldEventDTOs) {
				LocalDateTime dateEvent = e.getDateTime();
				boolean isAfter = dateEvent.isAfter(dateNow);
				if (isAfter == true) {
					eventDTOs.add(e);
				}
			}

		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("eventDTOs", eventDTOs);

		return "events";
	}

	@GetMapping(path = "/find-events")
	public String showEventsByActivity(Model model, @RequestParam String activityId, Authentication authentication)
			throws FinderException {
		final String mname = "showEventsByActivity";
		LOGGER.debug("entering " + mname);

		List<EventDTO> eventDTOs = null;
		if (authentication != null) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			UserDTO userDTO = userService.findUser(userDetails.getUsername());
			if (userDTO.getRoleName().equals("ROLE_ATHLETE"))
				model.addAttribute("username", userDetails.getUsername());
		}
		try {
			eventDTOs = sportService.findEventsByActivity(activityId);
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		model.addAttribute("eventDTOs", eventDTOs);

		return "events";
	}

}
