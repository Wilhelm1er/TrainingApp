package com.sport.training.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sport.training.domain.service.ShoppingCartService;

@Secured("ROLE_ATHLETE")
@Controller
public class ShoppingCartController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	ShoppingCartService shoppingCartService;

	@GetMapping("/view-cart")
	protected String viewCart(Model model) {
		final String mname = "viewCart";
		LOGGER.debug("entering " + mname);
		model.addAttribute("cart", shoppingCartService.getEvents());
		model.addAttribute("cartValue", shoppingCartService.getTotal());
		LOGGER.debug("exiting " + mname);
		return "cart";
	}

	@GetMapping("/add-to-cart")
	protected String addEventToCart(Model model, @RequestParam Long eventId) {
		final String mname = "addEventToCart";
		LOGGER.debug("entering " + mname);
		// Adds the eventId into the Shopping Cart
		shoppingCartService.addEvent(eventId);
		model.addAttribute("cart", shoppingCartService.getEvents());
		model.addAttribute("cartValue", shoppingCartService.getTotal());
		LOGGER.debug("exiting " + mname);
		return "cart";
	}

	@GetMapping("/remove-from-cart")
	protected String removeEventFromCart(Model model, @RequestParam Long eventId) {
		final String mname = "removeEventFromCart";
		LOGGER.debug("entering " + mname);
		shoppingCartService.removeEvent(eventId);
		model.addAttribute("cart", shoppingCartService.getEvents());
		model.addAttribute("cartValue", shoppingCartService.getTotal());
		LOGGER.debug("exiting " + mname);
		return "cart";
	}

}
