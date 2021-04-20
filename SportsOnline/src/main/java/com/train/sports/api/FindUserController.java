package com.train.sports.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.train.sports.authentication.domain.dto.UserDTO;
import com.train.sports.authentication.domain.service.RoleService;
import com.train.sports.authentication.domain.service.UserService;
import com.train.sports.exception.FinderException;

/**
 * This servlet displays user according to their role.
 */
@Controller
public class FindUserController  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FindUserController.class);
    
    @Autowired
	private UserService userService;
    
    @Autowired
	private RoleService roleService;
    
    @Secured("ROLE_ADMIN")
    @GetMapping("/display-coachs")
    public String showCoachs(Model model) {
    	final String mname = "showCoachs";
		LOGGER.debug("entering "+mname);
		
		List<UserDTO> usersDTOs = null;
    	try {
    		usersDTOs = userService.findUsersByRole(roleService.findByRoleName("ROLE_COACH"));
		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
    	model.addAttribute("coachDTOs", usersDTOs);
    	return "display-coachs";
    }
    
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/display-athletes")
    public String showAthletes(Model model) {
    	final String mname = "showAthletes";
		LOGGER.debug("entering "+mname);
		
		List<UserDTO> athletesDTOs = null;
    	try {
    		athletesDTOs = userService.findUsersByRole(roleService.findByRoleName("ROLE_ATHLETE"));
		} catch (FinderException e) {
			model.addAttribute("exception", e.getMessage());
			return "error";
		}
        model.addAttribute("customerDTOs", athletesDTOs);
    	return "display-users";
    }
    
}