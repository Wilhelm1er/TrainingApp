package com.sport.training.domain.service;

import java.util.Collection;
import java.util.Map;

import com.sport.training.domain.dto.ShoppingCartEventDTO;

/**
 * This interface gives a remote view of the ShoppingCartBean. Any distant
 * client that wants to call a method on the ShoppingCartBean has to use this
 * interface.
 */
public interface ShoppingCartService {

	// ======================================
	// = Business methods =
	// ======================================

	/**
	 * This method returns the shopping cart. The shopping cart is represented as a
	 * Map (key, value) where event ids and quantities are stored.
	 *
	 * @return the shopping cart
	 */
	public Map<Long, Integer> getCart();

	/**
	 * This method returns a collection of ShoppingCartDTO. It uses the item id that
	 * is stored in the shopping cart to get all item information (id, name,
	 * activity, creditcost).
	 *
	 * @return a collection of ShoppingCartDTO
	 */
	public Collection<ShoppingCartEventDTO> getEvents();

	/**
	 * This method adds an item to the shopping cart with a quantity equals to one.
	 *
	 * @param eventId
	 * @throws RemoteException
	 */
	void addEvent(Long eventId);

	/**
	 * This method removes an item from the shopping cart.
	 *
	 * @param eventId
	 */
	public void removeEvent(Long eventId);

	/**
	 * This method empties the shopping cart.
	 *
	 */
	public void empty();

	public int getTotal();

}
