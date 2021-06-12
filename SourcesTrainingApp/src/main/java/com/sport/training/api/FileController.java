package com.sport.training.api;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.FileDTO;
import com.sport.training.domain.message.ResponseFile;
import com.sport.training.domain.message.ResponseMessage;
import com.sport.training.domain.model.File;
import com.sport.training.domain.service.FileStorageService;
import com.sport.training.exception.FinderException;

@Controller
public class FileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private FileStorageService storageService;

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file,  
			Authentication authentication, Model model) {
		String message = "";
		UserDTO userDTO;
		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userDTO = userService.findUser(userDetails.getUsername());
			storageService.store(file, userDTO);
			
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			model.addAttribute("message", message);
			
			return "upload";
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "upload";
		}
	}
	
	@GetMapping(path = "/upload")
	public String showUploadConfirmation(@ModelAttribute String message, Model model) throws FinderException {
		final String mname = "showUploadConfirmation";
		LOGGER.debug("entering " + mname);

		model.addAttribute("message", message);

		return "upload";
	}

	@GetMapping("/documents/{userId}")
	public String getListFiles(@PathVariable String userId, Model model) throws FinderException {
		final String mname = "showUploadConfirmation";
		LOGGER.debug("entering " + mname);
		
		Set<String> fileNames;
		try {
			fileNames = storageService.getAllFileNamesByUser(userId);
			
		} catch (Exception e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}

		model.addAttribute("userId", userId);
		model.addAttribute("fileNames", fileNames);
		
		return "documents";
	}

	@GetMapping("/files/{name}")
	public ResponseEntity<byte[]> getFile(@PathVariable String name) throws FinderException {
		File file = storageService.getFile(name);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.body(file.getData());
	}
	
	@GetMapping("/document-users")
	public String getListUsers(Model model) throws FinderException {
		final String mname = "getListUsers";
		LOGGER.debug("entering " + mname);
		
		Set<UserDTO> userDTOs;
		try {
			userDTOs = storageService.getAllUsers();
			
		} catch (Exception e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}

		model.addAttribute("userDTOs", userDTOs);
		
		return "document-users";
	}
}
