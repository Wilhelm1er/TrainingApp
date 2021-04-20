package com.train.sports.catalogRestApi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.train.sports.domain.dto.ActivityDTO;
import com.train.sports.domain.service.SportService;
import com.train.sports.exception.CreateException;
import com.train.sports.exception.FinderException;
import com.train.sports.exception.RemoveException;
import com.train.sports.exception.UpdateException;

/**
 * These servlets returns the selected item / items.
 */
@RestController
public class ActivityRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityRestController.class);
    
    @Autowired
	private SportService sportService;
    
    @GetMapping("/activity")
	public ResponseEntity<List<ActivityDTO>> getAllActivities() {
		final String mname = "getActivities";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(sportService.findActivities());
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
    	
	@GetMapping("/activities/{disciplineId}")
	public ResponseEntity<List<ActivityDTO>> getActivities(@PathVariable String disciplineId) {
		final String mname = "getActivities";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(sportService.findActivities(disciplineId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/activity/{activityId}")
	public ResponseEntity<ActivityDTO> getProduct(@PathVariable String activityId) {
		final String mname = "getActivity";
		LOGGER.debug("entering "+mname);
		try {
			return ResponseEntity.ok(sportService.findActivity(activityId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping(path="/activity", consumes="application/json")
	public ResponseEntity<ActivityDTO> createActivity(@RequestBody ActivityDTO activityDTO) {
		final String mname = "createActivity";
		LOGGER.debug("entering "+mname);
		try {
			return new ResponseEntity<>(sportService.createActivity(activityDTO), HttpStatus.CREATED);
		} catch (CreateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@Secured("ROLE_ADMIN")
	@PutMapping(path="/activity/{activityId}", consumes="application/json")
	public ResponseEntity<ActivityDTO>  updateActivity(@RequestBody ActivityDTO activityDTO, @PathVariable String activityId) {
		final String mname = "updateActivity";
		LOGGER.debug("entering "+mname);
		if(activityDTO.getId()== null || activityDTO.getId().equals("") || ! activityDTO.getId().equals(activityId))
			return ResponseEntity.badRequest().build();
		try {
			sportService.updateActivity(activityDTO);
			return ResponseEntity.ok(activityDTO);
		} catch (UpdateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/activity/{activityId}")
	public ResponseEntity<Void> deleteActivity(@PathVariable String activityId) {
		final String mname = "deleteActivity";
		LOGGER.debug("entering "+mname);
		try {
			sportService.deleteActivity(activityId);
			return ResponseEntity.noContent().build();
		} catch (DataIntegrityViolationException | RemoveException | FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
