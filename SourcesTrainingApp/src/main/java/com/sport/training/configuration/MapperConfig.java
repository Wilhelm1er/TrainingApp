package com.sport.training.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.DisciplineRegistry;
import com.sport.training.domain.model.EventRegistry;

@Configuration
public class MapperConfig {

	@Bean
	public ModelMapper commonModelMapper() {		// Discipline, Address, CrediCard
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	@Bean
	public ModelMapper userModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<User, UserDTO> typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		typeMap.addMapping(src -> src.getRole().getName(), UserDTO::setRoleName);
		return modelMapper;
	}
	
	@Bean
	public ModelMapper userDTOModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<User, UserDTO> typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
		modelMapper.getConfiguration().setSkipNullEnabled(true);
		// on ne retransmet pas le password vers la vue
		typeMap.addMappings(mapper -> mapper.skip(UserDTO::setPassword));
		typeMap.addMapping(src -> src.getRole().getName(), UserDTO::setRoleName);
		return modelMapper;
	}
	
	@Bean
	public ModelMapper activityModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Activity, ActivityDTO> typeMap = modelMapper.createTypeMap(Activity.class, ActivityDTO.class);
		typeMap.addMappings(mapper -> mapper.map(Activity::getDiscipline, ActivityDTO::setDisciplineDTO));
		return modelMapper;
	}
	
	@Bean
	public ModelMapper eventModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Event, EventDTO> typeMap = modelMapper.createTypeMap(Event.class, EventDTO.class);
		typeMap.addMappings(mapper -> mapper.map(Event::getActivity, EventDTO::setActivityDTO));
		return modelMapper;
	}

}
