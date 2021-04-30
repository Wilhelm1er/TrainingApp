package com.yaps.petstore.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
public class OrderTestWebClient {
	
	@Autowired
    private MockMvc mockMvc;

	@Autowired
	private WebClient webClient;

	private HtmlPage loginPage;
	private HtmlPage fishPage, koiHtmlPage, SpecificKoiHtmlPage, basketHtmlPage, checkOutHtmlPage;

	private HtmlElement getKoisLink, getSpecificKoiLink, addToBasketLink, checkoutLink;

	@Test
	public void testOrderAsUser() throws Exception {

		loginPage = webClient.getPage("/login");
		loginPage.getFormByName("loginForm").getInputByName("username").setValueAttribute("job5");
		loginPage.getFormByName("loginForm").getInputByName("password").setValueAttribute("cnam");
		loginPage.getFormByName("loginForm").getButtonByName("loginButton").click();

		fishPage = webClient.getPage("/find-products?categoryId=FISH");
		assertTrue(fishPage.getBody().asText().contains("Liste de produits appartenant à la catégorie FISH"));

		getKoisLink = fishPage.getBody().getOneHtmlElementByAttribute("a", "href", "/find-items?productId=FIFW01");
		koiHtmlPage = getKoisLink.click();
		assertTrue(koiHtmlPage.getBody().asText().contains("Liste des articles appartenant au produit Koi"));
		getSpecificKoiLink = koiHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/find-item?itemId=EST5");
		SpecificKoiHtmlPage = getSpecificKoiLink.click();
		assertTrue(SpecificKoiHtmlPage.getBody().asText().contains("Koi sélectionné"));
		addToBasketLink = SpecificKoiHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/add-to-cart?itemId=EST5");
		basketHtmlPage = addToBasketLink.click();
		assertTrue(basketHtmlPage.getBody().asText().contains("retirer du panier"));
		assertTrue(basketHtmlPage.getBody().asText().contains("Freshwater fish from Japan"));
		assertTrue(basketHtmlPage.getBody().asText().contains("TOTAL : 12.0"));
		checkoutLink = basketHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/checkout");
		checkOutHtmlPage = checkoutLink.click();
		assertTrue(checkOutHtmlPage.getBody().asText().contains("Votre commande est finalisée"));	
	}
	
	@Test
	@WithMockUser(username = "jeff01", password = "cnam", roles = "FRANCHISEE")
	public void testOrderAsfranchisee() throws Exception {

		  mockMvc.perform(MockMvcRequestBuilders.get("/find-item?itemId=EST5")
		            .accept(MediaType.ALL))
		            .andExpect(status().isOk())
		            .andExpect(content().string(containsString("Koi sélectionné")));
		  
		  mockMvc.perform(MockMvcRequestBuilders.get("/add-to-cart?itemId=EST5")
		            .accept(MediaType.ALL))
		  			.andExpect(status().isOk())
		  			.andExpect(content().string(containsString("AccessDeniedException")));
		}
	
	@Test
	@WithMockUser(username = "stb01", password = "cnam", roles = "FRANCHISEE")
	public void testOrderAsAdmin() throws Exception {

		  mockMvc.perform(MockMvcRequestBuilders.get("/find-item?itemId=EST5")
		            .accept(MediaType.ALL))
		            .andExpect(status().isOk())
		            .andExpect(content().string(containsString("Koi sélectionné")));
		  
		  mockMvc.perform(MockMvcRequestBuilders.get("/add-to-cart?itemId=EST5")
		            .accept(MediaType.ALL))
		  			.andExpect(status().isOk())
		  			.andExpect(content().string(containsString("AccessDeniedException")));
		}
	
}
