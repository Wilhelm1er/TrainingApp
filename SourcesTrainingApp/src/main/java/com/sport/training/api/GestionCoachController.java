package com.sport.training.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

@Controller
public class GestionCoachController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GestionCoachController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private RegistryService registryService;
	
	@Autowired
	private SportService sportService;
	
	@GetMapping(path = "/create-event/{username}")
	public String showGestionCoach(Model model, @PathVariable String username) {
		final String mname = "showGestionCoach";
		LOGGER.debug("entering "+mname);
		
		UserDTO userDTO;
		Set<DisciplineDTO> disciplineDTOsCoach = null;
		HashMap<String,List<ActivityDTO>> activityByDisciplinesCoach=new HashMap<String,List<ActivityDTO>>();
		try {
			userDTO = userService.findUser(username);
			disciplineDTOsCoach = registryService.findDisciplinesByCoach(username);
			if(disciplineDTOsCoach!=null) {
			for(DisciplineDTO disciplineDTO: disciplineDTOsCoach) {
				List<ActivityDTO>list=new ArrayList<ActivityDTO>(sportService.findActivities(disciplineDTO.getId()));
				activityByDisciplinesCoach.put(disciplineDTO.getName(),list);
			}}
		} catch (FinderException e) {
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("eventDTO", new EventDTO());
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("disciplineDTOsCoach", disciplineDTOsCoach);
		model.addAttribute("activityByDisciplinesCoach", activityByDisciplinesCoach);

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
	public String deleteEvent(@PathVariable Long eventId, Model model) {
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
	
	@GetMapping("/update-event/{eventId}")
	public String showEvent(@PathVariable Long eventId, Model model) {
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
	
	@GetMapping("/events/{username}")
	public String showEvents(@PathVariable String username, Model model) {
		final String mname = "manageEvents";
		LOGGER.debug("entering " + mname);
		try {
			List<EventDTO> eventDTOs = sportService.findEvents(username);
			model.addAttribute("eventDTOs", eventDTOs);
		} catch (FinderException e) {
			if(e.getMessage().equals("No Event in the database")) {
        		model.addAttribute("message", "No Event for this activity");
        		return "events";
        	}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "events";
	}
	

}
