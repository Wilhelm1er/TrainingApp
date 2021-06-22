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

import reactor.core.publisher.Mono;

@Secured("ROLE_ADMIN")
@Controller
public class ManageDisciplineController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageDisciplineController.class);

	@Autowired
	private WebClient webClient;

	@GetMapping("/manage-sports")
	public String manageDisciplines(Model model) {
		final String mname = "manageDisciplines";
		LOGGER.debug("entering " + mname);
		try {
			List<DisciplineDTO> disciplineDTOs = retrieveDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
		} catch (WebClientResponseException e) {
			if (e.getMessage().contains("404 Not Found")) {
				LOGGER.error("exception in " + mname + " : " + e.getMessage());
				model.addAttribute("message", "No category found");
				return "index";
			}
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "manage-sport";
	}

	@GetMapping("/update-discipline/{disciplineId}")
	public String showDiscipline(@PathVariable String disciplineId, Model model) {
		final String mname = "showDiscipline";
		LOGGER.debug("entering " + mname);
		try {
			DisciplineDTO disciplineDTO = retrieveDiscipline(disciplineId);
			model.addAttribute("disciplineDTO", disciplineDTO);
		} catch (WebClientResponseException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "update-discipline";
	}

	@PostMapping("/update-discipline")
	public String updateDiscipline(@Valid @ModelAttribute DisciplineDTO disciplineDTO, Model model,
			HttpServletRequest request, @CookieValue("JSESSIONID") String cookie) {
		final String mname = "updateDiscipline";
		LOGGER.debug("entering " + mname);
		try {
			String JsonDis = restUpdateDiscipline(disciplineDTO, request, cookie);
			LOGGER.debug("updateddiscipline : " + JsonDis);
			List<DisciplineDTO> disciplineDTOs = retrieveDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("disciplineUpdated", disciplineDTO.getId());
		} catch (WebClientResponseException e) {
			LOGGER.error("exception in " + mname + " : " + e.getMessage());
			model.addAttribute("exception", e.getClass().getName());
			return "error";
		}
		return "manage-sport";
	}

	@GetMapping("/delete-discipline/{discplineId}")
	public String deleteDiscipline(@PathVariable String disciplineId, Model model, HttpServletRequest request,
			@CookieValue("JSESSIONID") String cookie) {
		final String mname = "deleteDiscipline";
		LOGGER.debug("entering " + mname);
		try {
			restDeleteDiscipline(disciplineId, request, cookie);
			List<DisciplineDTO> disciplineDTOs = retrieveDisciplines();
			model.addAttribute("disciplineDTOs", disciplineDTOs);
			model.addAttribute("disciplineDeleted", disciplineId);
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

	private DisciplineDTO retrieveDiscipline(String disciplineId) {
		return this.webClient.get().uri("/discipline/" + disciplineId).retrieve().bodyToMono(DisciplineDTO.class)
				.block();
	}

	private String restUpdateDiscipline(DisciplineDTO disciplineDTO, HttpServletRequest request, String cookie) {
		final String mname = "restUpdateDiscipline";
		LOGGER.debug("entering " + mname);
		CsrfToken sessionToken = (CsrfToken) request.getAttribute("_csrf");
		LOGGER.debug("JSESSIONID : " + cookie);
		return this.webClient.put().uri("/discipline/" + disciplineDTO.getId())
				.header("X-CSRF-TOKEN", sessionToken.getToken()) // oubli => 403 FORBIDDEN
				.cookie("JSESSIONID", cookie) // oubli => 302 FOUND
				.body(Mono.just(disciplineDTO), DisciplineDTO.class).retrieve()
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

	private void restDeleteDiscipline(String disciplineId, HttpServletRequest request, String cookie) {
		final String mname = "restDeleteDiscipline";
		LOGGER.debug("entering " + mname);
		CsrfToken sessionToken = (CsrfToken) request.getAttribute("_csrf");
		this.webClient.delete().uri("/discipline/" + disciplineId).header("X-CSRF-TOKEN", sessionToken.getToken())
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
