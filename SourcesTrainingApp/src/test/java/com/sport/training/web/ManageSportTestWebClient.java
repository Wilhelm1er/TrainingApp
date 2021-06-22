package com.sport.training.web;

import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class tests the HTML Pages and servlets
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ManageSportTestWebClient {

	@Autowired
	private WebClient webClient;

	private HtmlPage index, loginPage;
	private HtmlPage eventCreatePage, itemUpdateHtmlPage, itemCreateHtmlPage;

	private HtmlElement itemUpdateLink;

	/**
	 * This test starts at login page the moves around catalog update pages
	 */
	@Test
	public void testWebCreateEvent() throws Exception {

		// 1st, we log in as a coach
		loginPage = webClient.getPage("/login");
		loginPage.getFormByName("loginForm").getInputByName("username").setValueAttribute("coach1");
		loginPage.getFormByName("loginForm").getInputByName("password").setValueAttribute("cnam");
		index = loginPage.getFormByName("loginForm").getButtonByName("loginButton").click();

		/* events */
		try {
			webClient.getPage("/select-activity/coach1").getWebResponse();
		} catch (Exception e) {
			assertTrue(e instanceof FailingHttpStatusCodeException);
		}
		eventCreatePage = webClient.getPage("/select-activity?userId=coach1");
		assertTrue(eventCreatePage.getBody().asText().contains("créer un evenement"));

		itemUpdateLink = eventCreatePage.getBody().getOneHtmlElementByAttribute("a", "href", "/create-item/AVCB01");
		itemCreateHtmlPage = itemUpdateLink.click();
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("Création d'un article"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("id"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("nom"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("date de naissance de l'animal (yyyy-mm-dd)"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("prix unitaire"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("0.0"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("url de l'icone associée"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("Nom du produit parent"));
		assertTrue(itemCreateHtmlPage.getBody().asText().contains("Amazon Parrot"));
		itemCreateHtmlPage.getElementById("id").setAttribute("value", "EST12554");
		itemCreateHtmlPage.getElementById("name").setAttribute("value", "Amazing Bird");
		itemCreateHtmlPage.getElementById("birthDate").setAttribute("value", "2020-10-10");
		itemCreateHtmlPage.getElementById("unitCost").setAttribute("value", "156.0");
		itemCreateHtmlPage.getElementById("imagePath").setAttribute("value", "bird5.jpg");
		itemManagePage = itemCreateHtmlPage.getFormByName("itemCreateForm").getButtonByName("itemCreateButton").click();
		assertTrue(itemManagePage.getBody().asText().contains("EST12554 a bien été créé"));
	}

	/**
	 * This test starts at login page the moves around catalog update pages
	 */
	@Test
	public void testWebUpdateEvent() throws Exception {

		// 1st, we log in as admin
		loginPage = webClient.getPage("/login");
		loginPage.getFormByName("loginForm").getInputByName("username").setValueAttribute("coach1");
		loginPage.getFormByName("loginForm").getInputByName("password").setValueAttribute("cnam");
		index = loginPage.getFormByName("loginForm").getButtonByName("loginButton").click();

		/* items */
		try {
			webClient.getPage("/manage-items").getWebResponse();
		} catch (Exception e) {
			assertTrue(e instanceof FailingHttpStatusCodeException);
		}
		itemManagePage = webClient.getPage("/manage-items/AVCB01");
		assertTrue(itemManagePage.getBody().asText().contains("Gestion du catalogue"));
		assertTrue(itemManagePage.getBody().asText().contains("Articles"));

		itemUpdateLink = itemManagePage.getBody().getOneHtmlElementByAttribute("a", "href", "/update-item/EST25");
		itemUpdateHtmlPage = itemUpdateLink.click();
		assertTrue(itemUpdateHtmlPage.getBody().asText().contains("Modification d'un article"));
		assertTrue(itemUpdateHtmlPage.getBody().asText().contains("date de naissance de l'animal (yyyy-mm-dd)"));
		assertTrue(itemUpdateHtmlPage.getBody().asText().contains("2020-03-15"));
		itemUpdateHtmlPage.getElementById("birthDate").setAttribute("value", "2020-10-10");
		itemUpdateHtmlPage.getElementById("unitCost").setAttribute("value", "156.0");
		itemManagePage = itemUpdateHtmlPage.getFormByName("itemUpdateForm").getButtonByName("itemUpdateButton").click();
		assertTrue(itemManagePage.getBody().asText().contains("EST25 a bien été modifié"));
	}

	/**
	 * A user should not be able to manage the catalog
	 */
	@Test
	@WithMockUser(username = "athlete1", password = "cnam", roles = "ATHLETE")
	public void testWebManageSportAsUser() throws Exception {
		index = webClient.getPage("/manage-sports");
		assertTrue(index.getBody().asText().contains("Page d'erreur"));
		assertTrue(index.getBody().asText().contains("AccessDeniedException"));

		index = webClient.getPage("/manage-activities/BOXE");
		assertTrue(index.getBody().asText().contains("Page d'erreur"));

		index = webClient.getPage("/select-activity/athlete1");
		assertTrue(index.getBody().asText().contains("Page d'erreur"));

		index = webClient.getPage("/select-activity?userId=athlete1");
		assertTrue(index.getBody().asText().contains("Page d'erreur"));
	}
}
