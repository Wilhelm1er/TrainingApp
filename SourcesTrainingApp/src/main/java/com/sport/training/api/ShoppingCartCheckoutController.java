package com.sport.training.api;

import java.util.Collection;
import java.util.Iterator;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.CustomUserDetails;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.CreditUserDTO;
import com.sport.training.domain.dto.EventUserDTO;
import com.sport.training.domain.dto.ShoppingCartEventDTO;
import com.sport.training.domain.service.ShoppingCartService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;

@Secured("ROLE_ATHLETE")
@Controller
public class ShoppingCartCheckoutController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartCheckoutController.class);

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	private SportService sportService;

	@Autowired
	private UserService userService;

	@GetMapping("/checkout")
	protected String checkout(Model model, @Valid @ModelAttribute EventUserDTO eventRegistryDTO,
			Authentication authentication) throws CreateException {
		final String mname = "checkout";
		LOGGER.debug("entering " + mname);

		// A user must be authenticated
		if (authentication == null) {
			model.addAttribute("exception", "no authenticated user");
			return "error";
		}
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		UserDTO athleteDTO;
		CreditUserDTO creditRegistryDTO;
		Double newSolde;
		try {

			athleteDTO = userService.findUser(username);
			if (athleteDTO.getCredit() < shoppingCartService.getTotal()) {
				model.addAttribute("exception", "Not enough credit");
				return "error";
			}
			if(shoppingCartService.getCart().isEmpty()) {
		
	            model.addAttribute("error", "Order object is empty");
				return "checkout";
				}

			Collection<ShoppingCartEventDTO> cartEvents = shoppingCartService.getEvents();
			Iterator<ShoppingCartEventDTO> it = cartEvents.iterator();
			while (it.hasNext()) {
				ShoppingCartEventDTO ShoppingCartEventDTO = it.next();
				eventRegistryDTO = new EventUserDTO(athleteDTO,
						sportService.findEvent(ShoppingCartEventDTO.getEventId()));
				try {
					userService.createEventUser(eventRegistryDTO);

				} catch (CreateException e) {
					LOGGER.error(mname + " - " + e.getMessage());
					model.addAttribute("exception",
							"Already registered for this event: " + eventRegistryDTO.getEventDTO().getName());
					return "error";
				}
			}
			creditRegistryDTO = new CreditUserDTO(athleteDTO, shoppingCartService.getTotal());
			newSolde = userDetails.getCredit() - shoppingCartService.getTotal();
			userDetails.setCredit(newSolde);
			userService.createCreditUser(creditRegistryDTO);
			shoppingCartService.empty();
		} catch (FinderException e) {
			LOGGER.error(mname + " - " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName() + " : " + e.getMessage());
			return "error";
		}
		model.addAttribute("athleteCredit", userDetails.getCredit());

		LOGGER.debug("exiting " + mname);
		return "checkout";
	}

}
