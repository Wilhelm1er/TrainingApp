package com.sport.training.domain.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.ShoppingCartEventDTO;
import com.sport.training.exception.FinderException;


//@Scope("session")
//@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);
	
	// ======================================
    // =             Attributes             =
    // ======================================
    private Map<String,Integer> _shoppingCart;

    @Autowired
    private SportService sportService;
    
    // ======================================
    // =            Constructor            =
    // ======================================
    public ShoppingCartServiceImpl() {
    	_shoppingCart =  new HashMap<>();
    }

    // ======================================
    // =     Lifecycle Callback methods     =
    // ======================================

    
    public void clear() {
    	final String mname = "clear";
    	LOGGER.debug("entering "+mname);
        _shoppingCart = null;
    }

    public Map<String, Integer> getCart() {
    	final String mname = "getCart";
    	LOGGER.debug("entering "+mname);
        return _shoppingCart;
    }

    public Collection<ShoppingCartEventDTO> getEvents() {
    	final String mname = "getEvents";
    	LOGGER.debug("entering "+mname);
    	
        final Collection<ShoppingCartEventDTO>  events = new ArrayList<>();

        Iterator<Map.Entry<String, Integer>> it = _shoppingCart.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> keyValue = it.next();
            String eventId = (String)keyValue.getKey();
            final EventDTO eventDTO;
            try {
            	eventDTO = sportService.findEvent(eventId);
                ShoppingCartEventDTO ShoppingCartEventDTO = new ShoppingCartEventDTO(eventId, eventDTO.getDate(), eventDTO.getActivityDTO().getDescription(), eventDTO.getCreditCost());
                 events.add(ShoppingCartEventDTO);
            } catch (FinderException e) {
            	LOGGER.error(mname+" - eventId : "+ eventId+" => "+e.getMessage());
            }
        }
        return  events;
    }

    public void addEvent(String eventId) {
        _shoppingCart.put(eventId, 1);
    }

    public void removeEvent(String eventId) {
        _shoppingCart.remove(eventId);
    }

    public void empty() {
        _shoppingCart.clear();
    }

}
