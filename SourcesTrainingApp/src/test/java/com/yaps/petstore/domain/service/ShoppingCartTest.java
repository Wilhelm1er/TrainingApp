package com.yaps.petstore.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.service.SportService;
import com.sport.training.domain.service.ShoppingCartService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.domain.dto.ActivityDTO;
import com.yaps.petstore.configuration.TestConfig;

@Transactional
@Import(TestConfig.class) 
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartTest {
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	SportService catalogService;
	
	@Test
    public void testshoppingCartService() throws Exception {

        String id = getPossibleUniqueStringId();
        double total;
        EventDTO itemDTO = null;
        EventDTO newItemDTO = null;

        // Creates an item
        createItem(id);

        // Gets the item
        try {
        	itemDTO = findItem(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Adds the item into the shopping cart [1 item]
        shoppingCartService.addEvent(itemDTO.getId());

        // Checks the amount of the shopping cart
        total = itemDTO.getUnitCost() * 1;
        assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(),(Double)total);

        // updates the quantity of the item [10 items]
        shoppingCartService.updateEventQuantity(itemDTO.getId(), 10);

        // Checks the amount of the shopping cart
        total = itemDTO.getUnitCost() * 10;
        assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double)total);

        // Creates a new item
        id = getPossibleUniqueStringId();
        createItem(id);
        try {
            newItemDTO = findItem(id);
        } catch (FinderException e) {
            fail("Object has been created it should be found");
        }

        // Adds the new item into the shopping cart [10 items, 1 new item]]
        shoppingCartService.addEvent(newItemDTO.getId());

        // Checks the amount of the shopping cart
        total = (itemDTO.getUnitCost() * 10) + newItemDTO.getUnitCost();
        assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double)total);

        // Removes the new item from the shopping cart [10 items]
        shoppingCartService.removeEvent(newItemDTO.getId());

        // Checks the amount of the shopping cart
        total = itemDTO.getUnitCost() * 10;
        assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double)total);

        // Empties the shopping cart [0]
        shoppingCartService.empty();

        // Checks the amount of the shopping cart
        total = 0;
        assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double)total);

        // Cleans the test environment
        deleteItem(id);
    }

    //==================================
    //=    Private Methods for Item    =
    //==================================
    private EventDTO findItem(final String id) throws FinderException {
        final EventDTO itemDTO = catalogService.findEvent("item" + id);
        return itemDTO;
    }

    // Creates a category first, then a product and then an item linked to this product
    private void createItem(final String id) throws CreateException {
        // Create Category
        final DisciplineDTO categoryDTO = new DisciplineDTO("cat" + id, "name" + id, "description" + id);
        catalogService.createDiscipline(categoryDTO);
        // Create Product
        final ActivityDTO productDTO = new ActivityDTO("prod" + id, "name" + id, "description" + id);
        productDTO.setCategoryDTO(categoryDTO);
        catalogService.createActivity(productDTO);
        // Create Item
        final EventDTO itemDTO = new EventDTO("item" + id, "name" + id, Double.parseDouble(id));
        itemDTO.setProductDTO(productDTO);
        itemDTO.setImagePath("imagePath" + id);
        catalogService.createEvent(itemDTO);
    }

    private void deleteItem(final String id) throws RemoveException, FinderException {
        catalogService.deleteEvent("item" + id);
        catalogService.deleteActivity("prod" + id);
        catalogService.deleteDiscipline("cat" + id);
    }
    
    protected int getPossibleUniqueIntId() {
        return (int) (Math.random() * 100000);
    }

    protected String getPossibleUniqueStringId() {
        int id = (int) (Math.random() * 100000);
        return "" + id;
    }

}
