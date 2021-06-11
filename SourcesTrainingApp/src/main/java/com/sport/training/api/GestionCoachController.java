package com.sport.training.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.training.authentication.domain.dao.RoleRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.Role;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.DiscussionDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.MessageDTO;
import com.sport.training.domain.service.CoachService;
import com.sport.training.domain.service.RegistryService;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

@Controller
public class GestionCoachController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GestionCoachController.class);

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

	@GetMapping(path = "/select-activity/{username}")
	public String showSelectActivity(Model model, @PathVariable String username) {
		final String mname = "showSelectActivity";
		LOGGER.debug("entering " + mname);

		UserDTO userDTO;
		List<DisciplineDTO> disciplineDTOsCoach = null;
		HashMap<String, List<ActivityDTO>> activityByDisciplinesCoach = new HashMap<String, List<ActivityDTO>>();
		try {
			userDTO = userService.findUser(username);
			disciplineDTOsCoach = registryService.findDisciplinesByCoach(username);
			if (disciplineDTOsCoach != null) {
				for (DisciplineDTO disciplineDTO : disciplineDTOsCoach) {
					List<ActivityDTO> list = new ArrayList<ActivityDTO>(
							sportService.findActivities(disciplineDTO.getId()));
					activityByDisciplinesCoach.put(disciplineDTO.getName(), list);
				}
			}
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("activityDTO", new ActivityDTO());
		model.addAttribute("disciplineDTOsCoach", disciplineDTOsCoach);
		model.addAttribute("activityByDisciplinesCoach", activityByDisciplinesCoach);

		return "select-activity";
	}

	@PostMapping("/select-activity")
	public String showCreateEvent(@ModelAttribute("activityDTO") ActivityDTO activityDTO, @RequestParam String userId,
			Model model) {
		final String mname = "showCreateEvent";
		LOGGER.debug("entering " + mname);

		ActivityDTO sActivityDTO;
		UserDTO userDTO;
		Long eventId = null;
		try {
			if (Objects.isNull(eventRepository.findLastId().get().longValue())) {
				eventId = 1L;
			}
			eventId = eventRepository.findLastId().get().longValue() + 1;
			userDTO = userService.findUser(userId);
			sActivityDTO = sportService.findActivity(activityDTO.getId());
			model.addAttribute("eventDate", new String());
			model.addAttribute("eventTime", new String());
			model.addAttribute("eventDTO", new EventDTO());
			model.addAttribute("activityDTO", sActivityDTO);
			model.addAttribute("userDTO", userDTO);
			model.addAttribute("eventId", eventId.toString());
			return "create-event";

		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}

	@PostMapping("/create-event")
	public String createEvent(@Valid @ModelAttribute EventDTO eventDTO, @ModelAttribute UserDTO userDTO,
			@ModelAttribute("eventDate") String eventDate, @ModelAttribute("eventTime") String eventTime,
			@ModelAttribute ActivityDTO activityDTO, Model model) {
		final String mname = "createEvent";
		LOGGER.debug("entering " + mname);

		String dateTime = eventDate + ' ' + eventTime;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime eventDateTime = LocalDateTime.parse(dateTime, formatter);

		try {
			eventDTO.setDateTime(eventDateTime);
			eventDTO.setActivityDTO(activityDTO);
			eventDTO.setCoachDTO(userDTO);
			sportService.createEvent(eventDTO);
			model.addAttribute("eventDTO", eventDTO);
			return "event";
		} catch (CreateException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}

	@GetMapping("/delete-event/{eventId}")
	public String deleteEvent(@PathVariable Long eventId, Model model) {
		final String mname = "deleteEvent";
		LOGGER.debug("entering " + mname);
		try {
			sportService.deleteEvent(eventId);
			model.addAttribute("eventDeleted", eventId);
		} catch (RemoveException | FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "events";
	}

	@PostMapping("/update-event")
	public String updateEvent(@Valid @ModelAttribute EventDTO eventDTO, Model model) {
		final String mname = "updateEvent";
		LOGGER.debug("entering " + mname);
		try {
			sportService.updateEvent(eventDTO);
			model.addAttribute("eventUpdated", eventDTO.getId());
			return "events";
		} catch (UpdateException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
	}

	@GetMapping("/update-event/{eventId}")
	public String showEvent(@PathVariable Long eventId, Model model) {
		final String mname = "showEvent";
		LOGGER.debug("entering " + mname);
		try {
			EventDTO eventDTO = sportService.findEvent(eventId);
			model.addAttribute("eventDTO", eventDTO);
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "update-event";
	}

	@GetMapping("/events/{username}")
	public String showEvents(@PathVariable String username, Model model) {
		final String mname = "manageEvents";
		LOGGER.debug("entering " + mname);
		try {
			List<EventDTO> eventDTOs = sportService.findEvents(username);
			model.addAttribute("eventDTOs", eventDTOs);
		} catch (FinderException e) {
			if (e.getMessage().equals("No Event in the database")) {
				model.addAttribute("message", "No Event for this activity");
				return "events";
			}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "events";
	}

	@GetMapping(path = "/coach-discipline")
	public String showDisciplineChoice(Model model, Authentication authentication) throws FinderException {
		final String mname = "showDisciplineChoice";
		LOGGER.debug("entering " + mname);

		UserDTO coachDTO = null;
		List<DisciplineDTO> disciplineDTOs = null;
		List<DisciplineDTO> disciplineDTOsCoach = null;
		List<DisciplineDTO> disciplineDTOsToCheckCoach = null;

		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			coachDTO = userService.findUser(userDetails.getUsername());

			disciplineDTOs = sportService.findDisciplines();
			disciplineDTOsCoach = registryService.findDisciplinesByCoach(coachDTO.getUsername());
			disciplineDTOsToCheckCoach = registryService.findDisciplineToCheckByCoach(coachDTO.getUsername());

		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}

		model.addAttribute("disciplineDTOsToCheckCoach", disciplineDTOsToCheckCoach);
		model.addAttribute("disciplineDTOsCoach", disciplineDTOsCoach);
		model.addAttribute("disciplineDTOs", disciplineDTOs);
		model.addAttribute("coachDTO", coachDTO);

		return "coach-discipline";
	}

	@PostMapping(path = "/coach-discipline")
	public String disciplineChoice(@Valid UserDTO coachDTO,
			@RequestParam(value = "discipline.id", required = false) String[] disciplineId, BindingResult bindingResult,
			Authentication authentication, Model model) {
		final String mname = "disciplineChoice";
		LOGGER.debug("entering " + mname);
		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			coachDTO = userService.findUser(userDetails.getUsername());
			System.out.println("coach: " + coachDTO);
			for (String disciplineIdChecked : disciplineId) {
				registryService.createDisciplineRegistry(
						new DisciplineRegistryDTO(sportService.findDiscipline(disciplineIdChecked), coachDTO));
			}
			model.addAttribute("message", "Coach discipline(s) added");
			return "coach-discipline";
		} catch (CreateException e) {
			if (e instanceof DuplicateKeyException)
				model.addAttribute("exception", "this id is already assigned");
			else
				model.addAttribute("exception", e.getMessage());
			return "error";
		} catch (Exception exc) {
			model.addAttribute("exception", exc.getMessage());
			return "error";
		}
	}

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

			if (userDTO.getRoleName().equals("ROLE_ADMIN")) {
				discussionDTOs = coachService.findDiscussions();

			} else {

				discussionDTOs = coachService.findDiscussionsByUser(userDTO.getUsername());
			}

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "messagerie";
		}
		model.addAttribute("discussionDTO", new DiscussionDTO());
		model.addAttribute("coachesList", coachesList);
		model.addAttribute("discussionDTOs", discussionDTOs);
		model.addAttribute("coachDTO", new UserDTO());

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
