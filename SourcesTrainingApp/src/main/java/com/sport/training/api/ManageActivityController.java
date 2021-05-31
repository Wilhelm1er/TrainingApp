package com.sport.training.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.service.SportService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.domain.dto.ActivityDTO;

import reactor.core.publisher.Mono;

@Secured("ROLE_ADMIN")
@Controller
public class ManageActivityController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageActivityController.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private SportService sportService;

	@GetMapping("/manage-activities/{disciplineId}")
	public String manageActivities(@PathVariable String disciplineId, Model model) {
		final String mname = "manageActivities";
		LOGGER.debug("entering " + mname);
		try {
			List<ActivityDTO> activityDTOs = retrieveActivities(disciplineId);
			model.addAttribute("activityDTOs", activityDTOs);
		} catch (WebClientResponseException e) {
			if (e.getMessage().contains("404 Not Found")) {
				LOGGER.error("exception in " + mname + " : " + e.getMessage());
				model.addAttribute("message", "No product for category " + disciplineId);
				return "index";
			}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "manage-sport";
	}

	@GetMapping("/create-activity/{disciplineId}")
	public String showCreateActivity(@PathVariable String disciplineId, Model model) {
		final String mname = "showCreateActivity";
		LOGGER.debug("entering " + mname);
		try {
			DisciplineDTO disciplineDTO = sportService.findDiscipline(disciplineId);
			model.addAttribute("disciplineDTO", disciplineDTO);
			model.addAttribute("activityDTO", new ActivityDTO());
		} catch (FinderException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		} catch (Exception e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
		return "create-activity";
	}

	@PostMapping("/create-activity")
	public String createActivity(@Valid @ModelAttribute ActivityDTO activityDTO, Model model) {
		final String mname = "createActivity";
		LOGGER.debug("entering " + mname);
		try {
			sportService.createActivity(activityDTO);
			List<ActivityDTO> disciplineDTOs = sportService.findActivities();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("activityDTO", activityDTO);
			return "create-activity";
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

	@GetMapping("/update-activity/{activityId}")
	public String showActivity(@PathVariable String activityId, Model model) {
		final String mname = "showActivity";
		LOGGER.debug("entering " + mname);
		try {
			ActivityDTO activityDTO = retrieveActivity(activityId);
			model.addAttribute("activityDTO", activityDTO);
		} catch (WebClientResponseException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "update-activity";
	}

	@PostMapping("/update-activity")
	public String updateProduct(@Valid @ModelAttribute ActivityDTO activityDTO, Model model, HttpServletRequest request,
			@CookieValue("JSESSIONID") String cookie) {
		final String mname = "updateActivity";
		LOGGER.debug("entering " + mname);
		try {
			String JsonProd = restUpdateActivity(activityDTO, request, cookie);
			LOGGER.debug("updatedActivity : " + JsonProd);
			List<DisciplineDTO> disciplineDTOs = retrieveDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("activityUpdated", activityDTO.getId());
		} catch (WebClientResponseException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "manage-sport";
	}

	@GetMapping("/delete-activity/{activityId}")
	public String deleteActivity(@PathVariable String activityId, Model model, HttpServletRequest request,
			@CookieValue("JSESSIONID") String cookie) {
		final String mname = "deleteActivity";
		LOGGER.debug("entering " + mname);
		try {
			restDeleteActivity(activityId, request, cookie);
			LOGGER.debug("deletedActivity id : " + activityId);
			List<DisciplineDTO> disciplineDTOs = retrieveDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("activityDeleted", activityId);
		} catch (WebClientResponseException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "manage-sport";
	}

	private List<DisciplineDTO> retrieveDisciplines() {
		return this.webClient.get().uri("/disciplines").retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<DisciplineDTO>>() {
				}).block();
	}

	private List<ActivityDTO> retrieveActivities(String disciplineId) {
		return this.webClient.get().uri("/activities/" + disciplineId).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<ActivityDTO>>() {
				}).block();
	}

	private ActivityDTO retrieveActivity(String activityId) {
		return this.webClient.get().uri("/activity/" + activityId).retrieve().bodyToMono(ActivityDTO.class).block();
	}

	private String restUpdateActivity(ActivityDTO activityDTO, HttpServletRequest request, String cookie) {
		final String mname = "restUpdateActivity";
		LOGGER.debug("entering " + mname);

		final String DEFAULT_CSRF_TOKEN_ATTR_NAME = HttpSessionCsrfTokenRepository.class.getName()
				.concat(".CSRF_TOKEN");
		CsrfToken sessionToken = (CsrfToken) request.getSession().getAttribute(DEFAULT_CSRF_TOKEN_ATTR_NAME);
		return this.webClient.put().uri("/activity/" + activityDTO.getId())
				.header("X-CSRF-TOKEN", sessionToken.getToken()) // oubli => 403 FORBIDDEN
				.cookie("JSESSIONID", cookie) // oubli => 302 FOUND
				.body(Mono.just(activityDTO), ActivityDTO.class).retrieve()
				// Facilitateur de deboggage
				.onStatus(HttpStatus::isError,
						clientResponse -> Mono.error(new Exception("error : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is3xxRedirection,
						clientResponse -> Mono
								.error(new Exception("is3xxRedirection : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is4xxClientError,
						clientResponse -> Mono
								.error(new Exception("is4xxClientError : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is5xxServerError,
						clientResponse -> Mono
								.error(new Exception("is5xxServerError : " + clientResponse.statusCode())))
				.bodyToMono(String.class).block();
	}

	private void restDeleteActivity(String activityId, HttpServletRequest request, String cookie) {
		final String mname = "restDeleteActivity";
		LOGGER.debug("entering " + mname);
//		LOGGER.debug("productId to delete : " + productId);
		CsrfToken sessionToken = (CsrfToken) request.getAttribute("_csrf");
		this.webClient.delete().uri("/activity/" + activityId).header("X-CSRF-TOKEN", sessionToken.getToken())
				.cookie("JSESSIONID", cookie).retrieve()
				// Facilitateur de deboggage
				.onStatus(HttpStatus::isError,
						clientResponse -> Mono.error(new Exception("error : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is3xxRedirection,
						clientResponse -> Mono
								.error(new Exception("is3xxRedirection : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is4xxClientError,
						clientResponse -> Mono
								.error(new Exception("is4xxClientError : " + clientResponse.statusCode())))
				.onStatus(HttpStatus::is5xxServerError,
						clientResponse -> Mono
								.error(new Exception("is5xxServerError : " + clientResponse.statusCode())))
				.bodyToMono(Void.class).block();
	}
}
