package com.sport.training.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.dto.MessageDTO;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.DiscussionDTO;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.EventRegistry;
import com.sport.training.domain.model.Message;
import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.Bookmark;
import com.sport.training.domain.model.DisciplineRegistry;
import com.sport.training.domain.model.Discussion;

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
		typeMap.addMappings(mapper -> mapper.map(Event::getCoach, EventDTO::setCoachDTO));
		typeMap.addMappings(mapper -> mapper.map(Event::getActivity, EventDTO::setActivityDTO));
		return modelMapper;
	}
	
	@Bean
	public ModelMapper eventRegistryModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<EventRegistry, EventRegistryDTO> typeMap = modelMapper.createTypeMap(EventRegistry.class, EventRegistryDTO.class);
		typeMap.addMappings(mapper -> mapper.map(EventRegistry::getUser, EventRegistryDTO::setUserDTO));
		typeMap.addMappings(mapper -> mapper.map(EventRegistry::getEvent, EventRegistryDTO::setEventDTO));
		return modelMapper;
	}
	@Bean
	public ModelMapper disciplineRegistryModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<DisciplineRegistry, DisciplineRegistryDTO> typeMap = modelMapper.createTypeMap(DisciplineRegistry.class, DisciplineRegistryDTO.class);
		typeMap.addMappings(mapper -> mapper.map(DisciplineRegistry::getCoach, DisciplineRegistryDTO::setCoachDTO));
		typeMap.addMappings(mapper -> mapper.map(DisciplineRegistry::getDiscipline, DisciplineRegistryDTO::setDisciplineDTO));
		return modelMapper;
	}

	
	@Bean
	public ModelMapper bookmarkModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Bookmark, BookmarkDTO> typeMap = modelMapper.createTypeMap(Bookmark.class, BookmarkDTO.class);
		typeMap.addMappings(mapper -> mapper.map(Bookmark::getAthlete, BookmarkDTO::setAthleteDTO));
		typeMap.addMappings(mapper -> mapper.map(Bookmark::getCoach, BookmarkDTO::setCoachDTO));
		return modelMapper;
	}
	
	@Bean
	public ModelMapper messageModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Message, MessageDTO> typeMap = modelMapper.createTypeMap(Message.class, MessageDTO.class);
		typeMap.addMappings(mapper -> mapper.map(Message::getRecipient, MessageDTO::setRecipientDTO));
		typeMap.addMappings(mapper -> mapper.map(Message::getSender, MessageDTO::setSenderDTO));
		typeMap.addMappings(mapper -> mapper.map(Message::getDiscussion, MessageDTO::setDiscussionDTO));
		return modelMapper;
	}
	
	@Bean
	public ModelMapper discussionModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		TypeMap<Discussion, DiscussionDTO> typeMap = modelMapper.createTypeMap(Discussion.class, DiscussionDTO.class);
		typeMap.addMappings(mapper -> mapper.map(Discussion::getUser, DiscussionDTO::setUserDTO));
		return modelMapper;
	}
}
