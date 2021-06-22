package com.sport.training.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sport.training.authentication.domain.service.RoleService;
import com.sport.training.authentication.domain.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FindUsersControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	@MockBean
	private RoleService roleService;

	@Test
	@WithMockUser(roles = "ADMIN")
	public void accesDisplayCoachesWithRoleAdmin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/display-coaches").accept(MediaType.ALL))
				.andExpect(status().isOk()).andExpect(content().string(containsString("username")));
	}

	@Test
	@WithMockUser(roles = "COACH")
	public void accesDisplayCoachesWithRoleCoach() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/display-coaches").accept(MediaType.ALL))
				.andExpect(status().isOk()).andExpect(content().string(containsString("Exception")));
	}
	
	@Test
	@WithMockUser(roles = "ATHLETE")
	public void accesDisplayCoachesWithRoleAthlete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/display-coaches").accept(MediaType.ALL))
				.andExpect(status().isOk()).andExpect(content().string(containsString("Exception")));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void accesDisplayAthletesWithRoleAdmin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/display-athletes").accept(MediaType.ALL))
				.andExpect(status().isOk()).andExpect(content().string(containsString("username")));
	}

	@Test
	@WithMockUser(roles = "COACH")
	public void accesDisplayAthletesWithRoleCoach() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/display-athletes").accept(MediaType.ALL))
				.andExpect(status().isOk()).andExpect(content().string(containsString("Exception")));
	}

	@Test
	@WithMockUser(roles = "ATHLETE")
	public void accesDisplayAthletesWithRoleAthlete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/display-athletes").accept(MediaType.ALL))
				.andExpect(status().isOk()).andExpect(content().string(containsString("Exception")));
	}

}
