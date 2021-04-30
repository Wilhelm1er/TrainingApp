package com.yaps.petstore.web;

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

	private HtmlPage index, productsHtmlPage, itemsHtmlPage, itemHtmlPage;


	private HtmlElement productsLink, itemsLink, itemLink;

    /**
     * This tests starts at index page and click on the products, items and item links
     */
	@Test
    public void testWebVisualiseProducts() throws Exception {
    	
        // The test starts at the index page
        index = webClient.getPage("/");
        assertTrue(index.getBody().asText().contains("The YAPS Pet Store Demo for Spring Boot is a fictional sample application"));

        // We click on the first link of the index page
        productsLink = index.getBody().getOneHtmlElementByAttribute("area", "href", "/find-products?categoryId=BIRDS");
        productsHtmlPage = productsLink.click();
        assertTrue(productsHtmlPage.getBody().asText().contains("Liste de produits appartenant à la catégorie BIRDS"));
    }

	@Test
    public void testWebVisualiseItems() throws Exception {
    	
        // The test starts at the index page
        index = webClient.getPage("/");
        assertTrue(index.getBody().asText().contains("The YAPS Pet Store Demo for Spring Boot is a fictional sample application"));

        // We click on the first link of the index page
        productsLink = index.getBody().getOneHtmlElementByAttribute("area", "href", "/find-products?categoryId=BIRDS");
        productsHtmlPage = productsLink.click();
        assertTrue(productsHtmlPage.getBody().asText().contains("Liste de produits appartenant à la catégorie BIRDS"));

        // We click on the first link of the products page
        itemsLink = productsHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/find-items?productId=AVCB01");
        itemsHtmlPage = itemsLink.click();
        assertTrue(itemsHtmlPage.getBody().asText().contains("Liste des articles appartenant au produit Amazon Parrot"));
    }


	@Test
    public void testWebVisualiseOneItem() throws Exception {
    	
        // The test starts at the index page
        index = webClient.getPage("/");
        assertTrue(index.getBody().asText().contains("The YAPS Pet Store Demo for Spring Boot is a fictional sample application"));

        // We click on the first link of the index page
        productsLink = index.getBody().getOneHtmlElementByAttribute("area", "href", "/find-products?categoryId=BIRDS");
        productsHtmlPage = productsLink.click();
        assertTrue(productsHtmlPage.getBody().asText().contains("Liste de produits appartenant à la catégorie BIRDS"));

        // We click on the first link of the products page
        itemsLink = productsHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/find-items?productId=AVCB01");
        itemsHtmlPage = itemsLink.click();
        assertTrue(itemsHtmlPage.getBody().asText().contains("Liste des articles appartenant au produit Amazon Parrot"));

        // We click on the first link of the items page
        itemLink = itemsHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href", "/find-item?itemId=EST25");
        itemHtmlPage= itemLink.click();
        assertTrue(itemHtmlPage.getBody().asText().contains("Amazon Parrot sélectionné"));
        assertTrue(itemHtmlPage.getBody().asText().contains("prix unitaire : 120.0 euros"));
        // ATTENTION : TEMPORAIRE. TESTE UN VARIABLE QUI EVOLUE AVEC LA DATE DU JOUR. VALABLE JUSQU'AU 14 FEVRIER 2021
        assertFalse(itemHtmlPage.getBody().asText().contains("an(s)"));
        assertTrue(itemHtmlPage.getBody().asText().contains("Male Adult - 10 mois"));
    }

}
