package com.yaps.petstore.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.Collection;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.domain.dao.DisciplineRepository;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dao.ActivityRepository;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;
import com.sport.training.domain.model.Activity;

/**
 * This class tests the ItemDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemDAOTest {

	private static final String COUNTER_NAME = "Item";
	
	private static Logger logger = LogManager.getLogger(ItemDAOTest.class);
	
	@Autowired
	private EventRepository ItemRepository;
	@Autowired
	private DisciplineRepository categoryRepository;
	@Autowired
	private ActivityRepository productRepository;
	@Autowired
    private CounterService counterService;
	
    private final double _defaultUnitCost = 1.25;
    private final Date _defaultDate = new Date(1601000000000L);

    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This test tries to find an object with a invalid identifier.
     */
    @Test
    public void testDomainFindItemWithInvalidValues() throws Exception {

        // Finds an object with a unknown identifier
        final String id = counterService.getUniqueId(COUNTER_NAME);
        try {
            findItem(id);
            fail("Object with unknonw id should not be found");
        } catch (NoSuchElementException e) {
        }

        // Finds an object with an empty identifier
        try {
            ItemRepository.findById(new String()).get();
            fail("Object with empty id should not be found");
        } catch (Exception e) {
        }

        // Finds an object with a null identifier
        try {
            ItemRepository.findById(null).get();
            fail("Object with null id should not be found");
        } catch (Exception e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    @Test
    public void testDomainFindAllItems() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // First findAll
        final int firstSize = findAllItems();
        // Create an object
        createItem(id);

        // Ensures that the object exists
        try {
            findItem(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllItems();

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Cleans the test environment
        removeItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This test ensures that the method findAll works. It does a first findAll, creates
     * a new object and does a second findAll.
     */
    @Test
    public void testDomainFindAllItemsForAProduct() throws Exception {

    	Activity newProduct = createNewProduct();
    	final String productId = newProduct.getId();

        // First findAll
        final int firstSize = findAllItems(productId);

        // Checks that the collection is empty
        if (firstSize != 0) fail("The collection should be empty");

        // Create an object
        Event item1 = createItemForProduct(newProduct);

        // Ensures that the object exists
        try {
            findItem(item1.getId());
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // second findAll
        final int secondSize = findAllItems(productId);

        // Checks that the collection size has increase of one
        if (firstSize + 1 != secondSize) fail("The collection size should have increased by 1");

        // Create an new object with a different id
        Event item2 = createItemForProduct(newProduct);

        // Ensures that the new object exists
        try {
            findItem(item2.getId());
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // third findAll
        final int thirdSize = findAllItems(productId);

        // Checks that the collection size has increase of one
        if (thirdSize != secondSize + 1) fail("The collection should have the same size");

        // Cleans the test environment
        ItemRepository.delete(item1);
        ItemRepository.delete(item2);
        removeProduct(newProduct);
    }

    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it and checks it then exists.
     */
    @Test
    public void testDomainCreateItem() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);
        Event item = null;

        // Ensures that the object doesn't exist
        try {
            item = findItem(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (NoSuchElementException e) {
        }

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        try {
            item = findItem(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkItem(item, id);

        // Cleans the test environment
        removeItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * This test make sure that updating an object success
     */
    @Test
    public void testDomainUpdateItem() throws Exception {
        final String id = counterService.getUniqueId(COUNTER_NAME);

        // Creates an object
        createItem(id);

        // Ensures that the object exists
        Event item = null;
        try {
            item = findItem(id);
        } catch (NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkItem(item, id);

        // Updates the object with new values
        updateItem(item, id + 1);

        // Ensures that the object still exists
        Event itemUpdated = null;
        try {
            itemUpdated = findItem(id);
        } catch (NoSuchElementException e) {
            fail("Object should be found");
        }

        // Checks that the object values have been updated
        checkItem(itemUpdated, id + 1);

        // Cleans the test environment
        removeItem(id);

        try {
            findItem(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (NoSuchElementException e) {
        }
    }

    //==================================
    //=         Private Methods        =
    //==================================
    private Event findItem(final String id) throws NoSuchElementException {
        final Event item = (Event)ItemRepository.findById(id).get();
        return item;
    }

    private int findAllItems() throws FinderException {
        try {
            return ((Collection<Event>) ItemRepository.findAll()).size();
        } catch (Exception e) {
        	logger.info("Exception is ... "+e.getMessage());
            return 0;
        }
    }

    private int findAllItems(String productId) throws FinderException {
        try {
        	Activity product = productRepository.findById(productId).get();
            return ((Collection<Event>)ItemRepository.findAllByProduct(product)).size();
        } catch (Exception e) {
            return 0;
        }
    }

    // Creates a category first, then a product and then an item linked to this product
    private void createItem(final String id) throws CreateException {
        // Create Category
        final String newCategoryId = counterService.getUniqueId("Category");
        final Discipline category = new Discipline("cat" + newCategoryId, "name" + newCategoryId, "description" + newCategoryId);
        categoryRepository.save(category);
        // Create Product
        final String newProductId = counterService.getUniqueId("Product");
        final Activity product = new Activity("prod" + newProductId, "name" + newProductId, "description" + newProductId, category);
        productRepository.save(product);
        // Create Item
        final Event item = new Event(id, "name" + id,_defaultDate, _defaultUnitCost, product);
        item.setImagePath("imagePath" + id);
        ItemRepository.save(item);
    }

    // Creates a category, a product and updates the item with this new product
    private void updateItem(final Event item, final String id) throws UpdateException, CreateException, ObjectNotFoundException, RemoveException {
     // get old  product
        Activity prod = item.getProduct();
    	
    	// Create Category
        final String newCategoryId = counterService.getUniqueId("Category");
        final Discipline category = new Discipline("cat" + newCategoryId, "name" + newCategoryId, "description" + newCategoryId);
        categoryRepository.save(category);
        // Create Product
        final String newProductId = counterService.getUniqueId("Product");
        final Activity product = new Activity("prod" + newProductId, "name" + newProductId, "description" + newProductId, category);
        productRepository.save(product);        
        
        // Updates the item
        item.setName("name" + id);
        item.setUnitCost(_defaultUnitCost);
        item.setImagePath("imagePath" + id);
        item.setProduct(product);
        ItemRepository.save(item);
     // delete old category &  product
        removeProduct(prod);
    }

    private void checkItem(final Event item, final String id) {
        assertEquals("name", "name" + id, item.getName());
        assertTrue("unitCost", _defaultUnitCost==item.getUnitCost());
        assertNotNull("product", item.getProduct());
        assertEquals("imagePath", "imagePath" + id, item.getImagePath());
        }

    private void removeItem(final String id) throws RemoveException, ObjectNotFoundException {
        Event item = ItemRepository.findById(id).get();
        final String productId = item.getProduct().getId();
        Activity product = productRepository.findById(productId).get();
        final String categoryId = product.getCategory().getId();
        Discipline category = categoryRepository.findById(categoryId).get();
        ItemRepository.deleteById(id);
        productRepository.delete(product);
        categoryRepository.delete(category);
    }

    // Creates a category first, then a product and return it
    private Activity createNewProduct() throws CreateException {
        // Create Category
        final String newCategoryId = counterService.getUniqueId("Category");
        final Discipline category = new Discipline("cat" + newCategoryId, "name" + newCategoryId, "description" + newCategoryId);
        categoryRepository.save(category);
        // Create Product
        final String newProductId = counterService.getUniqueId("Product");
        final Activity product = new Activity("prod" + newProductId, "name" + newProductId, "description" + newProductId, category);
        productRepository.save(product);
        return product;
    }

    // Creates an item linked to an existing product
    private Event createItemForProduct(final Activity product) throws CreateException {
        final String id = counterService.getUniqueId(COUNTER_NAME);
        final Event item = new Event(id, "name" + id,_defaultDate, _defaultUnitCost, product);
        item.setImagePath("imagePath" + id);
        ItemRepository.save(item);
        return item;
    }

    private void removeProduct(final Activity product) throws RemoveException, ObjectNotFoundException {
        final String categoryId = product.getCategory().getId();
        Discipline category = categoryRepository.findById(categoryId).get();
        productRepository.delete(product);
        categoryRepository.delete(category);
    }
}
