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
public class ShoppingCartServiceImpl implements ShoppingCartService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

	// ======================================
	// = Attributes =
	// ======================================
	private Map<Long, Integer> _shoppingCart;

	@Autowired
	private SportService sportService;

	// ======================================
	// = Constructor =
	// ======================================
	public ShoppingCartServiceImpl() {
		_shoppingCart = new HashMap<>();
	}

	// ======================================
	// = Lifecycle Callback methods =
	// ======================================

	public void clear() {
		final String mname = "clear";
		LOGGER.debug("entering " + mname);
		_shoppingCart = null;
	}

	@Override
	public Map<Long, Integer> getCart() {
		final String mname = "getCart";
		LOGGER.debug("entering " + mname);
		return _shoppingCart;
	}

	@Override
	public Collection<ShoppingCartEventDTO> getEvents() {
		final String mname = "getEvents";
		LOGGER.debug("entering " + mname);

		final Collection<ShoppingCartEventDTO> events = new ArrayList<>();

		Iterator<Map.Entry<Long, Integer>> it = _shoppingCart.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Integer> keyValue = it.next();
			Long eventId = keyValue.getKey();
			final EventDTO eventDTO;
			try {
				eventDTO = sportService.findEvent(eventId);
				ShoppingCartEventDTO ShoppingCartEventDTO = new ShoppingCartEventDTO(eventId, eventDTO.getName(),
						eventDTO.getDate(), eventDTO.getDescription(), eventDTO.getCreditCost());
				events.add(ShoppingCartEventDTO);
			} catch (FinderException e) {
				LOGGER.error(mname + " - eventId : " + eventId + " => " + e.getMessage());
			}
		}
		return events;
	}

	@Override
	public void addEvent(Long eventId) {
		_shoppingCart.put(eventId, 1);
	}

	@Override
	public void removeEvent(Long eventId) {
		_shoppingCart.remove(eventId);
	}

	@Override
	public Double getTotal() {
		double total = 0.0;
		Collection<ShoppingCartEventDTO> cartEvents = getEvents();
		Iterator<ShoppingCartEventDTO> it = cartEvents.iterator();
		while (it.hasNext()) {
			ShoppingCartEventDTO ShoppingCartEventDTO = it.next();
			total += ShoppingCartEventDTO.getCreditCost();
		}
		return total;
	}

	@Override
	public void empty() {
		_shoppingCart.clear();
	}

}
