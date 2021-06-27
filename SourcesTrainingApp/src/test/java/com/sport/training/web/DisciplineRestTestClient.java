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
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.service.SportService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DisciplineRestTestClient {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SportService sp;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void tesRestShowDisciplines() throws Exception {
		MvcResult result = mockMvc
				.perform(get("/disciplines").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("BOXE"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Renforcement du corps, canaliser son energie et se surpasser"));
		assertTrue(result.getResponse().getContentAsString().contains("CARDIO"));
		assertTrue(result.getResponse().getContentAsString().contains("DANSE"));
		assertTrue(result.getResponse().getContentAsString().contains("YOGA"));
		assertTrue(result.getResponse().getContentAsString().contains("GYM"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Entretenez votre forme physique par des assouplissements, des etirements et des exercices de tonicite musculaire"));
	}

	@Test
	public void tesRestShowDisciplineById() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/discipline/BOXE").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("BOXE"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Renforcement du corps, canaliser son energie et se surpasser"));
	}

	@Test
	public void tesRestShowDisciplineByUnknownDisciplineId() throws Exception {
		mockMvc.perform(
				get("/discipline/BOXXE").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDiscipline() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setName("Boxe");
		String json = objectMapper.writeValueAsString(boxe);
		MvcResult result = mockMvc.perform(put("/discipline/BOXE").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(
				"{\"id\":\"BOXE\",\"name\":\"Boxe\",\"description\":\"Renforcement du corps, canaliser son énergie et se surpasser\",\"documents\":\"Brevet d Etat AGFF - Activites Gymniques de la Forme et de la Force\"}"));
		}

	@Test
	public void testRestUpdateDisciplineNotAuthenticated() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setName("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		MvcResult result = mockMvc.perform(put("/discipline/BOXE").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDisciplineWithWrongId1() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setName("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(put("/discipline/GYM").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDisciplineWithWrongId2() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(put("/discipline/BOXE").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDisciplineWithNullId() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId(null);
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(put("/discipline/BOXE").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestCreateDiscipline() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		MvcResult result = mockMvc.perform(post("/discipline").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(
				"{\"id\":\"BOXXE\",\"name\":\"Boxe\",\"description\":\"Renforcement du corps, canaliser son énergie et se surpasser\",\"documents\":\"Brevet d Etat AGFF - Activites Gymniques de la Forme et de la Force\"}"));
	}

	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void testRestCreateDisciplineNotAuthorized() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		MvcResult result = mockMvc.perform(post("/discipline").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestCreateInvalidDiscipline() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId(null);
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(post("/discipline").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestDeleteDiscipline() throws Exception {
		MvcResult result = mockMvc.perform(delete("/discipline/BOXE").with(csrf())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent()).andReturn();
		assertTrue(result.getResponse().getStatus() == 204); // successful DELETE should imply no content Http Status

		mockMvc.perform(
				get("/discipline/BOXE").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()); // deleted category should not be found
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testRestFailedDeleteDiscipline() throws Exception {
		MvcResult result = mockMvc.perform(delete("/discipline/BOXXXE") // No such category
				.with(csrf()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();
		assertTrue(result.getResponse().getStatus() == 404);
	}

	@Test
	@WithMockUser(username = "athlete1", password = "cnam", roles = "ATHLETE")
	public void testRestDeleteDisciplineNotAuthorized() throws Exception {
		MvcResult result = mockMvc.perform(delete("/discipline/BOXE").with(csrf())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}

}
