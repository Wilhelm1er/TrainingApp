package com.sport.training.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sport.training.domain.service.ShoppingCartService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;

@Secured("ROLE_USER")
@Controller
public class ShoppingCartCheckoutController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartCheckoutController.class);

	@Autowired
	ShoppingCartService shoppingCartService;
	
	
	@GetMapping("/checkout")
	protected String checkout(Model model, Authentication authentication) {
		final String mname = "checkout";
		LOGGER.debug("entering "+mname);
		
		LOGGER.debug("exiting "+mname);
		return "checkout";
	}

}
