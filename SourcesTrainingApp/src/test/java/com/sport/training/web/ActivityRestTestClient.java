package com.sport.training.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.service.SportService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ActivityRestTestClient {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SportService sS;

	@Autowired
	private ObjectMapper objectMapper;



	@Test
	public void tesRestShowActivityByDisciplineId() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/find-activities?disciplineId=BOXE").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("Bodysculpt"));
		assertTrue(result.getResponse().getContentAsString().contains("Tonifier sa silhouette, decharger son stress et perdre du poids"));
		assertTrue(result.getResponse().getContentAsString().contains("Cardio boxe"));
		assertTrue(result.getResponse().getContentAsString().contains("Combat training"));
		assertTrue(result.getResponse().getContentAsString().contains("Mouvement de boxe, corde ?? sauter et travail de la silhouette "));
	}

	@Test
	public void tesRestShowActivityByUnknownDisciplineId() throws Exception {
		mockMvc.perform(
				get("/activity/BOXXE").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void tesRestShowActivityByActivityId() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/activity/ABDOS").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertFalse(result.getResponse().getContentAsString().contains("BODY"));
		assertFalse(result.getResponse().getContentAsString().contains("Bodysculpt"));
		assertFalse(result.getResponse().getContentAsString().contains("Tonifier sa silhouette, decharger son stress et perdre du poids"));
		assertFalse(result.getResponse().getContentAsString().contains("BOXE"));
		assertFalse(result.getResponse().getContentAsString().contains("Boxe"));
		assertFalse(result.getResponse().getContentAsString().contains(
				"Renforcement du corps, canaliser son ??nergie et se surpasser"));
		assertTrue(result.getResponse().getContentAsString().contains("ABDOS"));
		assertTrue(result.getResponse().getContentAsString().contains("Abdominaux"));
		assertTrue(result.getResponse().getContentAsString().contains("Permet de travailler l ensemble des muscles de la sangle abdominale sous la forme d exercices cibles"));
		assertTrue(result.getResponse().getContentAsString().contains("GYM"));
		assertTrue(result.getResponse().getContentAsString().contains("Gym"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Entretenez votre forme physique par des assouplissements, des etirements et des exercices de tonicite musculaire"));
	}

	@Test
	public void tesRestShowActivityByUnknownActivityId() throws Exception {
		mockMvc.perform(
				get("/activity/BOODY").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivity() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setName("Big Abdominaux");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(put("/activity/ABDOS").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(
				"{\"id\":\"ABDOS\",\"name\":\"Big Abdominaux\",\"description\":\"Permet de travailler\",\"creditcostmin\":0.0,\"creditcostmax\":2.0,\"disciplineDTO\":{\"id\":\"GYM\",\"name\":\"Gym\",\"description\":\"Entretenez votre forme physique par des assouplissements, des etirements et des exercices de tonicite musculaire\",\"documents\":\"Brevet d Etat AGFF - Activites Gymniques de la Forme et de la Force\"}}"));
	}

	@Test
	public void testRestUpdateActivityNotAuthenticated() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setName("Abdominaux");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(put("/activity/ABDOS").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivityWithWrongId1() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setName("Abdominaux");
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(put("/activity/ABDOS2").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivityWithWrongId2() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId("ABDOS1");
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(put("/activity/ABDOS1").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivityWithNullId() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId(null);
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(put("/activity/ABDOS").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestCreateActivity() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId("ABDOOS");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(post("/activity").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains(
				"{\"id\":\"ABDOOS\",\"name\":\"Abdominaux\",\"description\":\"Permet de travailler l ensemble des muscles de la sangle abdominale sous la forme d exercices cibles\","
				+ "\"creditcostmin\":0.0,\"creditcostmax\":2.0,\"disciplineDTO\":{\"id\":\"GYM\",\"name\":\"Gym\",\"description\":\"Entretenez votre forme physique par des assouplissements, des etirements et des exercices de tonicite musculaire\",\"documents\":\"Brevet d Etat AGFF - Activites Gymniques de la Forme et de la Force\"}}"));
		}

	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void testRestCreateActivityNotAutorized() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId("ABDOOS");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(post("/activity").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestCreateInvalidActivity() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId(null);
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(post("/activity").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestDeleteActivity() throws Exception {
		MvcResult result = mockMvc.perform(delete("/activity/ABDOS").with(csrf())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andReturn();
		assertTrue(result.getResponse().getStatus() == 204); // successful DELETE should imply no content Http Status

		mockMvc.perform(
				get("/activity/ABDOS").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()); // deleted activity should not be found
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestFailDeleteActivity() throws Exception {
		MvcResult result = mockMvc.perform(delete("/activity/ABDOMS") // AVCB12 activity does not exist
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		assertTrue(result.getResponse().getStatus() == 404);
	}

	@Test
	@WithMockUser(username = "athlete1", password = "cnam", roles = "ATHLETE")
	public void testRestDeleteDisciplineNotAuthorized() throws Exception {
		MvcResult result = mockMvc.perform(delete("/activity/ABDOS").with(csrf())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}
}
