package com.sport.training.web;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

import javax.transaction.Transactional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class tests the HTML Pages and servlets
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class UpdateAccountTestWebClient {

	@Autowired
	private WebClient webClient;

	@Autowired
	private MockMvc mockMvc;

	private HtmlPage updateAccountPage, loginPage, displayCustomersPage;

	@Test
	@WithMockUser(username = "coach1", password = "cnam", roles = "COACH")
	public void a_testWebUpdateAccount() throws Exception {
		updateAccountPage = webClient.getPage("/update-account/coach1");
		assertTrue(updateAccountPage.getBody().asText().contains("Modifiez votre compte"));

		updateAccountPage.getFormByName("update-account-form").getInputByName("password").setValueAttribute("cnam");
		updateAccountPage = updateAccountPage.getFormByName("update-account-form")
				.getButtonByName("update-account-button").click();
		assertTrue(updateAccountPage.getBody().asText().contains("Vous trouverez ici"));

		mockMvc.perform(logout());

	}

	@Test
	public void b_testWebCheckAccountAfterUpdate() throws Exception {
		loginPage = webClient.getPage("/login");
		loginPage.getFormByName("loginForm").getInputByName("username").setValueAttribute("coach1");
		loginPage.getFormByName("loginForm").getInputByName("password").setValueAttribute("cnam");
		loginPage.getFormByName("loginForm").getButtonByName("loginButton").click();
		displayCustomersPage = webClient.getPage("/account/coach1");
		assertTrue("could not login after updating account. Something broken ?",
				displayCustomersPage.getBody().asText().contains("Votre compte"));
	}
}