package com.sport.training.api;

import java.util.List;
import java.util.Random;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.sport.training.authentication.domain.dao.RoleRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.Role;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dto.DiscussionDTO;
import com.sport.training.domain.dto.MessageDTO;
import com.sport.training.domain.service.CoachService;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;

@Controller
public class MessagerieController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagerieController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RegistryService registryService;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SportService sportService;

	@Autowired
	private CoachService coachService;

	@GetMapping(path = "/messagerie")
	public String getMessagerie(Authentication authentication, Model model) {
		final String mname = "getMessagerie";
		LOGGER.debug("entering " + mname);

		List<DiscussionDTO> discussionDTOs = null;
		UserDTO userDTO;

		List<UserDTO> coachesList;
		Role role;
		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userDTO = userService.findUser(userDetails.getUsername());

			role = roleRepository.findByName("ROLE_COACH");

			coachesList = userService.findUsersByRole(role);
			model.addAttribute("coachDTO", new UserDTO());
			model.addAttribute("coachesList", coachesList);

			if (userDTO.getRoleName().equals("ROLE_ADMIN")) {
				discussionDTOs = coachService.findDiscussions();

			} else {

				discussionDTOs = coachService.findDiscussionsByUser(userDTO.getUsername());
			}

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "messagerie";
		}
		model.addAttribute("discussionDTO", new DiscussionDTO());
		model.addAttribute("coachesList", coachesList);
		model.addAttribute("discussionDTOs", discussionDTOs);

		return "messagerie";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(path = "/discussion/{username}")
	public String getDiscussion(@PathVariable String username, Model model) {
		final String mname = "getDiscussion";
		LOGGER.debug("entering " + mname);

		List<DiscussionDTO> discussionDTOs = null;
		// UserDTO adminDTO;
		UserDTO userDTO;
		try {
			userDTO = userService.findUser(username);
			System.out.println("userDTO : " + userDTO);
			discussionDTOs = coachService.findDiscussionsByUser(username);
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "discussion";
		}
		model.addAttribute("username", userDTO.getUsername());
		model.addAttribute("discussionDTOs", discussionDTOs);

		return "discussion";
	}

	@PostMapping("/discussion")
	public String newDiscussion(@Valid @ModelAttribute DiscussionDTO discussionDTO,
			@ModelAttribute("coachDTO") UserDTO coachDTO, @ModelAttribute("subject") String subject, Model model)
			throws CreateException {
		final String mname = "newDiscussion";
		LOGGER.debug("entering " + mname);

		try {
			discussionDTO.setSubject(subject);
			discussionDTO = coachService.createDiscussion(new DiscussionDTO(coachDTO, subject));
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "redirect:/messagerie";
		}
		model.addAttribute("username", coachDTO.getUsername());
		model.addAttribute("discussionDTO", discussionDTO);

		return "redirect:/messagerie";
	}

	@GetMapping("/messages/{discussionId}")
	public String showMessages(@PathVariable String discussionId, Model model) throws CreateException {
		final String mname = "showMessages";
		LOGGER.debug("entering " + mname);

		List<MessageDTO> messageDTOs = null;
		DiscussionDTO discussionDTO;
		try {
			discussionDTO = coachService.findDiscussion(Long.valueOf(discussionId));
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "messages";
		}
		try {
			messageDTOs = coachService.findMessagesByDiscussion(Long.valueOf(discussionId));
		} catch (Exception exc) {
			model.addAttribute("subject", discussionDTO.getSubject());
			model.addAttribute("error", exc.getMessage());
			return "messages";
		}
		model.addAttribute("subject", discussionDTO.getSubject());
		model.addAttribute("discussionDTO", discussionDTO);
		model.addAttribute("messageDTO", new MessageDTO());
		model.addAttribute("messageDTOs", messageDTOs);
		model.addAttribute("texte", new String());

		return "messages";
	}

	@GetMapping("/new-message/{discussionId}")
	public String newMessage(@PathVariable String discussionId, Model model) throws CreateException {
		final String mname = "newMessage";
		LOGGER.debug("entering " + mname);

		model.addAttribute("messageDTO", new MessageDTO());
		model.addAttribute("texte", new String());

		return "new-message";
	}

	@PostMapping("/new-message/{discussionId}")
	public String sendMessage(@PathVariable String discussionId, @Valid @ModelAttribute("texte") String texte,
			@Valid @ModelAttribute MessageDTO messageDTO, Authentication authentication, Model model)
			throws CreateException {
		final String mname = "sendMessage";
		LOGGER.debug("entering " + mname);

		UserDTO senderDTO;
		UserDTO adminDTO;

		Role role;
		List<UserDTO> adminsList;
		Random rand = new Random();

		try {
			// Choix admin random
			role = roleRepository.findByName("ROLE_ADMIN");
			adminsList = userService.findUsersByRole(role);
			adminDTO = adminsList.get(rand.nextInt(adminsList.size()));

			// Determination du sender
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			senderDTO = userService.findUser(userDetails.getUsername());

			// set dans le message
			messageDTO.setDiscussionDTO(coachService.findDiscussion(Long.valueOf(discussionId)));
			messageDTO.setSenderDTO(senderDTO);
			if (senderDTO.getRoleName().equals("ROLE_ADMIN")) {
				messageDTO.setRecipientDTO(messageDTO.getDiscussionDTO().getUserDTO());
			}
			if (senderDTO.getRoleName().equals("ROLE_COACH")) {
				messageDTO.setRecipientDTO(adminDTO);
			}
			if (texte.isEmpty()) {
				model.addAttribute("error", "message vide");
			} else {
				coachService.createMessage(messageDTO);
			}

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "redirect:/messages/{discussionId}";
		}

		return "redirect:/messages/{discussionId}";
	}
}
