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

import com.train.sports.domain.dto.DisciplineDTO;
import com.train.sports.domain.service.SportService;
import com.train.sports.exception.CreateException;
import com.train.sports.exception.FinderException;
import com.train.sports.exception.RemoveException;
import com.train.sports.exception.UpdateException;

@RestController
public class DisciplineRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DisciplineRestController.class);

	@Autowired
	private SportService sportService;
	
	@GetMapping("/categories")
	public ResponseEntity<List<DisciplineDTO>> getDisciplines() {
		final String mname = "getDisciplines";
		LOGGER.debug("entering "+mname);

		try {
			return ResponseEntity.ok( sportService.findDisciplines());
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/discipline/{disciplineId}")
	public ResponseEntity<DisciplineDTO> getDiscipline(@PathVariable String disciplineId) {
		final String mname = "getDiscipline";
		LOGGER.debug("entering "+mname);

		try {
			return ResponseEntity.ok(sportService.findDiscipline(disciplineId));
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/discipline")
	public ResponseEntity<DisciplineDTO> createDiscipline(@RequestBody DisciplineDTO disciplineDTO) {
		final String mname = "createDiscipline";
		LOGGER.debug("entering "+mname);
		try {
			return new ResponseEntity<>(sportService.createDiscipline(disciplineDTO), HttpStatus.CREATED);
		} catch (CreateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@Secured("ROLE_ADMIN")
	@PutMapping("/discipline/{disciplineId}")
	public ResponseEntity<DisciplineDTO> updateDiscipline(@RequestBody DisciplineDTO disciplineDTO, @PathVariable String disciplineId) {
		final String mname = "updateDiscipline";
		LOGGER.debug("entering "+mname);
		if(disciplineDTO.getId()== null || disciplineDTO.getId().equals("") || ! disciplineDTO.getId().equals(disciplineId))
			return ResponseEntity.badRequest().build();
		try {
			sportService.updateDiscipline(disciplineDTO);
			return ResponseEntity.ok(disciplineDTO);
		} catch (UpdateException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/discipline/{disciplineId}")
	public ResponseEntity<Void> deleteDiscipline(@PathVariable String disciplineId) {
		final String mname = "deleteDiscipline";
		LOGGER.debug("entering "+mname);
		try {
			sportService.deleteDiscipline(disciplineId);
			return ResponseEntity.noContent().build();
		} catch (DataIntegrityViolationException | RemoveException | FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
