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
				"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertTrue(result.getResponse().getContentAsString().contains("CATS"));
		assertTrue(result.getResponse().getContentAsString().contains("DOGS"));
		assertTrue(result.getResponse().getContentAsString().contains("FISH"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}

	@Test
	public void tesRestShowDisciplineById() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/discipline/BOXE").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("BOXE"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertFalse(result.getResponse().getContentAsString().contains("CATS"));
		assertFalse(result.getResponse().getContentAsString().contains("DOGS"));
		assertFalse(result.getResponse().getContentAsString().contains("FISH"));
		assertFalse(result.getResponse().getContentAsString().contains("REPTILES"));
		assertFalse(result.getResponse().getContentAsString().contains(
				"Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}

	@Test
	public void tesRestShowCategoryByUnknownCategoryId() throws Exception {
		mockMvc.perform(
				get("/discipline/BOXE").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDiscipline() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setName("Boxe");
		String json = objectMapper.writeValueAsString(boxe);
		MvcResult result = mockMvc.perform(put("/discipline/BOXE").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(
				"{\"id\":\"BOXE\",\"name\":\"Boxe\",\"description\":\"Renforcement du corps, canaliser son énergie et se surpasser\"}"));
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
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDisciplineWithWrongId1() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setName("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(put("/discipline/GYM").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDisciplineWithWrongId2() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(put("/discipline/BOXE").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateDisciplineWithNullId() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId(null);
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(put("/discipline/BOXE").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestCreateCategory() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId("BOXXE");
		String json = objectMapper.writeValueAsString(boxe);
		MvcResult result = mockMvc.perform(post("/category").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(
				"{\"id\":\"BOXE\",\"name\":\"Boxe\",\"description\":\"Renforcement du corps, canaliser son énergie et se surpasser\"}"));
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
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestCreateInvalidDiscipline() throws Exception {
		DisciplineDTO boxe = sp.findDiscipline("BOXE");
		boxe.setId(null);
		String json = objectMapper.writeValueAsString(boxe);
		mockMvc.perform(post("/discipline").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
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
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestFailedDeleteDiscipline() throws Exception {
		MvcResult result = mockMvc.perform(delete("/discipline/BOXE") // No such category
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
