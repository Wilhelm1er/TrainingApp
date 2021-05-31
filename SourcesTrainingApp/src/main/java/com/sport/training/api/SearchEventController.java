package com.sport.training.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.service.SportService;

/**
 * These servlets returns the selected item / items.
 */
@Controller
public class SearchEventController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchEventController.class);

	@Autowired
	private SportService cs;

	@PostMapping("/search-events")
	protected String searchEvents(Model model, @RequestParam String keyword) {
		final String mname = "searchEvents";
		LOGGER.debug("entering " + mname);
		final List<EventDTO> eventDTOs;
		try {
			eventDTOs = cs.searchEvents(keyword);
			model.addAttribute("keyword", keyword);
			model.addAttribute("eventDTOs", eventDTOs);
			LOGGER.debug("exiting " + mname);
			return "events";
		} catch (Exception e) {
			LOGGER.error("exception " + mname + " - " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}

}