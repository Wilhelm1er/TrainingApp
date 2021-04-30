package com.sport.training.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.FinderException;
import com.sport.training.domain.dto.ActivityDTO;

/**
 * These servlets returns the selected item / items.
 */
@Controller
public class FindEventController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FindEventController.class);
    
    @Autowired
	private SportService catalogService;
    
    @Autowired
	private UserService userService;

    @GetMapping("/find-event")
    protected String findEvent(Model model, @RequestParam String eventId, Authentication authentication) {
        final String mname = "findEvent";
        LOGGER.debug("entering "+mname);
        try {
        	EventDTO eventDTO = catalogService.findEvent(eventId);
        	if(authentication != null) {
    			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    			UserDTO userDTO = userService.findUser(userDetails.getUsername());
    			if(userDTO.getRoleName().equals("ROLE_USER"))
    				model.addAttribute("username", userDetails.getUsername());
    		}
            model.addAttribute("eventDTO", eventDTO);
            return "event";
        } catch (FinderException e) {
        	LOGGER.error("exception in "+mname+" : "+e.getMessage());
            model.addAttribute("exception", e.getClass().getName());
			return "error";
        } catch (Exception e) {
        	LOGGER.error("exception in "+mname+" : "+e.getMessage());
            model.addAttribute("exception", e.getMessage());
			return "error";
        }     
    }
    
    @GetMapping("/find-events")
    protected String findEvents(Model model, @RequestParam String activityId, Authentication authentication) {
        final String mname = "findEvents";
        LOGGER.debug("entering "+mname);
        try {
        	List<EventDTO> eventDTOs = catalogService.findEvents(activityId);         
        	ActivityDTO activityDTO = catalogService.findActivity(activityId);
            if(authentication != null) {
    			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    			UserDTO userDTO = userService.findUser(userDetails.getUsername());
    			if(userDTO.getRoleName().equals("ROLE_ATHLETE"))
    				model.addAttribute("username", userDetails.getUsername());
    		}
            model.addAttribute("activityDTO", activityDTO);
            model.addAttribute("eventDTOs", eventDTOs);
            return "events";
        } catch (FinderException e) {
        	if(e.getMessage().equals("No Event in the database")) {
        		model.addAttribute("message", "No Event for this activity");
        		return "index";
        	}
        	LOGGER.error("exception in "+mname+" : "+e.getMessage());
            model.addAttribute("exception", e.getClass().getName());
			return "error";
        } catch (Exception e) {
        	LOGGER.error("exception in "+mname+" : "+e.getMessage());
            model.addAttribute("exception", e.getMessage());
			return "error";
        }
    }
}
