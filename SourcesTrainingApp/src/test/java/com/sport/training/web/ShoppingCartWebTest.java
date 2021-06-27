package com.sport.training.web;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ShoppingCartWebTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testViewCartAsUnlogged() {
		try {
			this.mockMvc.perform(get("/view-cart")).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("http://localhost/login"));
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "athlete1", password = "cnam", roles = "ATHLETE")
	public void testViewCartAsAthlete() {
		try {
			this.mockMvc.perform(get("/view-cart")).andExpect(status().isOk())
					.andExpect(content().string(containsString("le panier est vide")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void testViewCartAsCoach() {
		try {
			this.mockMvc.perform(get("/view-cart")).andExpect(status().isOk())
					.andExpect(content().string(containsString("AccessDeniedException")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testViewCartAsAdmin() {
		try {
			this.mockMvc.perform(get("/view-cart")).andExpect(status().isOk())
					.andExpect(content().string(containsString("AccessDeniedException")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "athlete1", password = "cnam", roles = "ATHLETE")
	public void testAddToCartAndCheckOutAsAthlete() {
		try {
			this.mockMvc.perform(get("/add-to-cart?eventId=EST19")).andExpect(status().isOk())
					.andExpect(content().string(containsString("Female Adult")))
					.andExpect(content().string(containsString("Great companion dog")))
					.andExpect(content().string(containsString("retirer du panier")))
					.andExpect(content().string(containsString("mettre à jour")))
					.andExpect(content().string(containsString("TOTAL : 10.0")))
					.andExpect(content().string(containsString("Valider la commande")));

			this.mockMvc.perform(get("/checkout")).andExpect(status().isOk())
					.andExpect(content().string(containsString("Votre commande est finalisée")))
					.andExpect(content().string(containsString("Votre numéro de commande est")))
					.andExpect(content().string(containsString("Nous vous remercions de votre confiance")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	public void testAddToCartAsUnlogged() {
		try {
			this.mockMvc.perform(get("/add-to-cart?eventId=EST19")).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("http://localhost/login"));
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void testAddToCartAsCoach() {
		try {
			this.mockMvc.perform(get("/add-to-cart?eventId=EST19")).andExpect(status().isOk())
					.andExpect(content().string(containsString("AccessDeniedException")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testAddToCartAsAdmin() {
		try {
			this.mockMvc.perform(get("/add-to-cart?eventId=EST19")).andExpect(status().isOk())
					.andExpect(content().string(containsString("AccessDeniedException")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	public void testCheckoutAsUnlogged() {
		try {
			this.mockMvc.perform(get("/checkout")).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("http://localhost/login"));
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void testCheckoutAsCoach() {
		try {
			this.mockMvc.perform(get("/checkout")).andExpect(status().isOk())
					.andExpect(content().string(containsString("AccessDeniedException")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}

	@Test
	@WithMockUser(username = "root", password = "cnam", roles = "ADMIN")
	public void testCheckoutAsAdmin() {
		try {
			this.mockMvc.perform(get("/checkout")).andExpect(status().isOk())
					.andExpect(content().string(containsString("AccessDeniedException")));
			this.mockMvc.perform(logout());
		} catch (Exception e) {
			fail("No exception expected");
		}
	}
}
