package com.sport.training.api;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;
import com.sport.training.domain.dto.ActivityDTO;

@Secured("ROLE_ADMIN")
@Controller
public class ManageEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageEventController.class);

	@Autowired
	private SportService sportService;

	@GetMapping("/manage-events/{activityId}")
	public String manageEvents(@PathVariable String activityId, Model model) {
		final String mname = "manageEvents";
		LOGGER.debug("entering " + mname);
		try {
			List<EventDTO> eventDTOs = sportService.findEvents(activityId);
			model.addAttribute("eventDTOs", eventDTOs);
		} catch (FinderException e) {
			if(e.getMessage().equals("No Event in the database")) {
        		model.addAttribute("message", "No Event for this activity");
        		return "index";
        	}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "manage-sport";
	}

	@GetMapping("/update-event/{eventId}")
	public String showEvent(@PathVariable String eventId, Model model) {
		final String mname = "showEvent";
		LOGGER.debug("entering " + mname);
		try {
			EventDTO eventDTO = sportService.findEvent(eventId);
			model.addAttribute("eventDTO", eventDTO);
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "update-event";
	}

	@PostMapping("/update-event")
	public String updateEvent(@Valid @ModelAttribute EventDTO eventDTO, Model model) {
		final String mname = "updateEvent";
		LOGGER.debug("entering " + mname);
		try {
			sportService.updateEvent(eventDTO);
			List<DisciplineDTO> disciplineDTOs = sportService.findDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("eventUpdated", eventDTO.getId());
			return "manage-sport";
		} catch (FinderException | UpdateException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}
	
	@GetMapping("/create-event/{activityId}")
	public String showCreateEvent(@PathVariable String activityId, Model model) {
		final String mname = "showCreateEvent";
		LOGGER.debug("entering " + mname);
		try {
			ActivityDTO activityDTO = sportService.findActivity(activityId);
			model.addAttribute("activityDTO", activityDTO);
			model.addAttribute("eventDTO", new EventDTO());
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "create-event";
	}

	@PostMapping("/create-event")
	public String createItem(@Valid @ModelAttribute EventDTO eventDTO, Model model) {
		final String mname = "createEvent";
		LOGGER.debug("entering " + mname);
		try {
			sportService.createEvent(eventDTO);
			List<DisciplineDTO> disciplineDTOs = sportService.findDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("eventDTO", eventDTO);
			return "create-event";
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


	@GetMapping("/delete-event/{eventId}")
	public String deleteEvent(@PathVariable String eventId, Model model) {
		final String mname = "deleteEvent";
		LOGGER.debug("entering " + mname);
		try {
			sportService.deleteEvent(eventId);
			List<DisciplineDTO> disciplineDTOs = sportService.findDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("eventDeleted", eventId);
		} catch (RemoveException | FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "manage-sport";
	}


}
