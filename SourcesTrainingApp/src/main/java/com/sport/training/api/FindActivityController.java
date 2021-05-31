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

import com.sport.training.domain.dto.ActivityDTO;

/**
 * This servlet returns the list of all products for a specific category.
 */
@Controller
public class FindActivityController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindActivityController.class);

	@Autowired
	private WebClient webClient;

	@GetMapping("/find-activities")
	protected String findActivities(Model model, @RequestParam String disciplineId) {
		final String mname = "findActivities";
		LOGGER.debug("entering " + mname);
		try {
			List<ActivityDTO> activityDTOs = retrieveActivities(disciplineId);
			model.addAttribute("disciplineId", disciplineId);
			model.addAttribute("activityDTOs", activityDTOs);
		} catch (WebClientResponseException e) {
			if (e.getMessage().contains("404 Not Found")) {
				LOGGER.error("exception in " + mname + " : " + e.getMessage());
				model.addAttribute("message", "No activity for discipline " + disciplineId);
				return "index";
			}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "activities";
	}

	private List<ActivityDTO> retrieveActivities(String disciplineId) {
		return this.webClient.get().uri("/activities/" + disciplineId).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<ActivityDTO>>() {
				}).block();
	}

}
