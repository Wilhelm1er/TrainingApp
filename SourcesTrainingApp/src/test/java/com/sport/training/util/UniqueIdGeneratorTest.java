package com.sport.training.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.domain.dao.CounterRepository;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.ObjectNotFoundException;

/**
 * This class tests the Unique id generator class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UniqueIdGeneratorTest {

	@Autowired
	private CounterRepository counterRepository;

	@Autowired
	private CounterService counterService;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(UniqueIdGeneratorTest.class);

	// ==================================
	// = Test cases =
	// ==================================
	@Test
	public void testUtilUniqueId() throws Exception {
		// Creates an unknown name
		final String name = "name" + getUniqueId();

		// Make sure this name doesn't exist
		try {

			findName(name);
			fail();
		} catch (ObjectNotFoundException e) {
		}

		// Gets the id
		final int firstValue = Integer.parseInt(counterService.getUniqueId(name));
		assertEquals("The value must be equal to 1", firstValue, 1);

		// Make sure this name exists
		try {
			findName(name);
		} catch (ObjectNotFoundException e) {
			fail();
		}

		// Calls the method twice
		counterService.getUniqueId(name);
		final int lastValue = Integer.parseInt(counterService.getUniqueId(name));

		assertEquals("The value must be equal to 3", lastValue, 3);

		counterService.deleteById(name);

		// Make sure the name doesn't exist anymore
		try {
			findName(name);
			fail();
		} catch (ObjectNotFoundException e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================

	private int getUniqueId() {
		return (int) (Math.random() * 100000);
	}

	public void findName(String name) throws ObjectNotFoundException {
		if (!counterRepository.findById(name).isPresent())
			throw new ObjectNotFoundException();
	}

}
