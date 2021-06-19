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
	public void tesRestShowActivities() throws Exception {
		MvcResult result = mockMvc
				.perform(get("/activities").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("ABDOS"));
		assertTrue(result.getResponse().getContentAsString().contains("Amazon Parrot"));
		assertTrue(result.getResponse().getContentAsString().contains("Great companion for up to 75 years"));
		assertTrue(result.getResponse().getContentAsString().contains("BIRDS"));
		assertTrue(result.getResponse().getContentAsString().contains("Birds"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertTrue(result.getResponse().getContentAsString().contains("RPSN01"));
		assertTrue(result.getResponse().getContentAsString().contains("Rattlesnake"));
		assertTrue(result.getResponse().getContentAsString().contains("Doubles as a watch dog"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains("Reptiles"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}

	@Test
	public void tesRestShowActivityByDisciplineId() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/activities/REPTILES").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().contains("RPLI02"));
		assertTrue(result.getResponse().getContentAsString().contains("Iguana"));
		assertTrue(result.getResponse().getContentAsString().contains("Friendly green friend"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains("Reptiles"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
		assertTrue(result.getResponse().getContentAsString().contains("RPSN01"));
		assertTrue(result.getResponse().getContentAsString().contains("Rattlesnake"));
		assertTrue(result.getResponse().getContentAsString().contains("Doubles as a watch dog"));
	}

	@Test
	public void tesRestShowActivityByUnknownDisciplineId() throws Exception {
		mockMvc.perform(
				get("/activitys/DINOS").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void tesRestShowActivityByActivityId() throws Exception {
		MvcResult result = mockMvc.perform(
				get("/activity/RPSN01").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		assertFalse(result.getResponse().getContentAsString().contains("ABDOS"));
		assertFalse(result.getResponse().getContentAsString().contains("Amazon Parrot"));
		assertFalse(result.getResponse().getContentAsString().contains("Great companion for up to 75 years"));
		assertFalse(result.getResponse().getContentAsString().contains("BIRDS"));
		assertFalse(result.getResponse().getContentAsString().contains("Birds"));
		assertFalse(result.getResponse().getContentAsString().contains(
				"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings"));
		assertTrue(result.getResponse().getContentAsString().contains("RPSN01"));
		assertTrue(result.getResponse().getContentAsString().contains("Rattlesnake"));
		assertTrue(result.getResponse().getContentAsString().contains("Doubles as a watch dog"));
		assertTrue(result.getResponse().getContentAsString().contains("REPTILES"));
		assertTrue(result.getResponse().getContentAsString().contains("Reptiles"));
		assertTrue(result.getResponse().getContentAsString().contains(
				"Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle"));
	}

	@Test
	public void tesRestShowActivityByUnknownActivityId() throws Exception {
		mockMvc.perform(
				get("/activity/BODY").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivity() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setName("abdoso");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(put("/activity/ABDOS").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(
				"{\"id\":\"ABDOS\",\"name\":\"Big Amazon Parrot\",\"description\":\"Great companion for up to 75 years\",\"categoryDTO\":{\"id\":\"BIRDS\",\"name\":\"Birds\",\"description\":\"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings\"}}"));
	}

	@Test
	public void testRestUpdateActivityNotAuthenticated() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setName("Abdoos");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(put("/activity/ABDOS").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivityWithWrongId1() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setName("Abdoos");
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(put("/activity/AVSB02").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivityWithWrongId2() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId("Abdoos");
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(put("/activity/AVCB101").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestUpdateActivityWithNullId() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId(null);
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(put("/activity/ABDOS").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestCreateActivity() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId("Abdoos");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(post("/activity").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(
				"{\"id\":\"AVCB001\",\"name\":\"Amazon Parrot\",\"description\":\"Great companion for up to 75 years\",\"categoryDTO\":{\"id\":\"BIRDS\",\"name\":\"Birds\",\"description\":\"Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings\"}}"));
	}

	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void testRestCreateActivityNotAutorized() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId("Abdoos");
		String json = objectMapper.writeValueAsString(abdos);
		MvcResult result = mockMvc.perform(post("/activity").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		assertTrue(result.getResolvedException() instanceof AccessDeniedException);
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestCreateInvalidActivity() throws Exception {
		ActivityDTO abdos = sS.findActivity("ABDOS");
		abdos.setId(null);
		String json = objectMapper.writeValueAsString(abdos);
		mockMvc.perform(post("/activity").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
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
	@WithMockUser(username = "MrRobot", password = "cnam", roles = "ADMIN")
	public void testRestFailDeleteActivity() throws Exception {
		MvcResult result = mockMvc.perform(delete("/activity/AVCB12") // AVCB12 activity does not exist
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
