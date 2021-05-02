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
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.exception.FinderException;

/**
 * This servlet returns the list of all products for a specific category.
 */
@Controller
public class FindCoachController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FindCoachController.class);
  
	@GetMapping("/find-coachs")
    protected String findCoachs(Model model,@RequestParam String disciplineId) {
        final String mname = "findCoachs";
        LOGGER.debug("entering "+mname);
        try {
        List<UserDTO> coachDTOs = null;
        model.addAttribute("disciplineId", disciplineId);
        model.addAttribute("coachDTOs", coachDTOs);
        } catch (WebClientResponseException e) {
			if(e.getMessage().contains("404 Not Found")) {
				LOGGER.error("exception in "+mname+" : "+e.getMessage());
        		model.addAttribute("message", "No coach for discipline "+disciplineId);
        		return "index";
			}
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}	
        return "coachs";
    }
	
	
	/*
	 * List<UserDTO> coachDTOs = null; try { coachDTOs =
	 * userService.findUsersByDiscipline(disciplineService.findByDisciplineName(
	 * disciplineId)); } catch (FinderException e) { model.addAttribute("exception",
	 * e.getMessage()); return "error"; } model.addAttribute("coachDTOs",
	 * coachDTOs); return "display-users";
	 */
	

}
