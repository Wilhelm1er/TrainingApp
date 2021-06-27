package com.sport.training.web;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.service.CounterService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;

/**
 * This class tests the HTML Pages and controllers
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebTestMockMvc {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private CounterService counterService;
	@Autowired
	private UserService userService;
	@Autowired
	private SportService sportService;
	
	private final Double _defaultCreditCost = 1.0;
	private final LocalDateTime _defaultDate = LocalDateTime.of(2021, 7, 12, 20, 15, 50, 345678900);
	

	/**
	 * Checks that all pages are deployed
	 */
	@Test
	public void testWebCheckPages() {
		try {
			this.mockMvc.perform(get("/dummy.html")).andExpect(status().is3xxRedirection());
		} catch (Exception e) {
		}

		try {
			this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(
					containsString("Centre d'entraînements")));
		} catch (Exception e) {
			fail("Root / hasn't been found");
		}

		try {
			this.mockMvc.perform(get("/createcustomer.html")).andExpect(status().is3xxRedirection());
		} catch (Exception e) {
		}

	}

	// Ce test nécessite que TP11 soit lancé
	@Test
	public void testWebCheckDeployedFindActivities() {
		try {
			this.mockMvc.perform(get("/find-activities?disciplineId=BOXE")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("appartenant à la discipline BOXE")));
		} catch (Exception e) {
			fail("The FindProducts Controller hasn't been found");
		}
	}

	@Test
	public void testWebCheckServlets() {

		try {
			this.mockMvc.perform(get("/new-athlete")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("Nouveau compte athlète")));
		} catch (Exception e) {
			fail("The new-account Controller hasn't been found");
		}
	}

	/**
	 * Checks athlete and user listing
	 */
	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testWebCheckCoachDisplay() {
		try {
			this.mockMvc.perform(get("/display-coaches")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("Liste des entraineurs")));
		} catch (Exception e) {
			fail("displayCoaches has failed");
		}
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testWebCheckAthleteDisplay() {
		try {
			this.mockMvc.perform(get("/display-athletes")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("Liste des athlètes")));
		} catch (Exception e) {
			fail("displayAthletes has failed");
		}
	}

	/**
	 * Checks franchisee and customer listing without proper authority level
	 */
	@Test
	@WithMockUser(username = "athlete1", password = "cnam", roles = "ATHLETE")
	public void testWebInvalidCheckCoachDisplay() {
		try {
			this.mockMvc.perform(get("/display-coaches")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("security.access.AccessDeniedException")));
		} catch (Exception e) {
		}
	}

	@Test
	@WithMockUser(username = "athlete1", password = "cnam", roles = "ATHLETE")
	public void testWebInvalidCheckAthleteDisplay() {
		try {
			this.mockMvc.perform(get("/display-athletes")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("security.access.AccessDeniedException")));
		} catch (Exception e) {
		}
	}

	/**
	 * Checks franchisee and customer listing logged as a franchisee
	 */
	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void testWebCheckAthleteDisplayAsCoach() {
		try {
			this.mockMvc.perform(get("/display-coaches")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("security.access.AccessDeniedException")));
		} catch (Exception e) {
		}

		try {
			this.mockMvc.perform(get("/display-athletes")).andDo(print()).andExpect(status().isOk())
					.andExpect(content().string(containsString("security.access.AccessDeniedException")));
		} catch (Exception e) {
		}
	}

}
