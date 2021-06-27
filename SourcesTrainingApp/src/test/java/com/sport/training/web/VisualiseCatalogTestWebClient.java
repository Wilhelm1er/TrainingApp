package com.sport.training.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class tests the HTML Pages and servlets
 */
// Ces tests nécessitent que TP11 soit lancé
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VisualiseCatalogTestWebClient {

	@Autowired
	private WebClient webClient;

	private HtmlPage index, activitiesHtmlPage, eventsHtmlPage, eventHtmlPage;

	private HtmlElement activitiesLink, eventsLink, eventLink;

	/**
	 * This tests starts at index page and click on the products, items and item
	 * links
	 */
	@Test
	public void testWebVisualiseActivities() throws Exception {

		// The test starts at the index page
		index = webClient.getPage("/");
		assertTrue(index.getBody().asText()
				.contains("Centre d'entraînements"));

		// We click on the first link of the index page
		activitiesLink = index.getBody().getOneHtmlElementByAttribute("area", "href", "/find-activities?disciplineId=BOXE");
		activitiesHtmlPage = activitiesLink.click();
		assertTrue(activitiesHtmlPage.getBody().asText().contains("Liste d'activités appartenant à la discipline BOXE"));
	}

	@Test
	public void testWebVisualiseEvents() throws Exception {

		// The test starts at the index page
				index = webClient.getPage("/");
				assertTrue(index.getBody().asText()
						.contains("Centre d'entraînements"));

		// We click on the first link of the index page
		activitiesLink = index.getBody().getOneHtmlElementByAttribute("area", "href", "/find-activities?disciplineId=BOXE");
		activitiesHtmlPage = activitiesLink.click();
		assertTrue(activitiesHtmlPage.getBody().asText().contains("Liste d'activités appartenant à la discipline BOXE"));

		// We click on the first link of the products page
		eventsLink = activitiesHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href",
				"/find-events?activityId=BODY");
		eventsHtmlPage = eventsLink.click();
		assertTrue(
				eventsHtmlPage.getBody().asText().contains("No Event in the database"));
	}
}
