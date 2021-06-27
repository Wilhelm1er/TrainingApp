
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.sport.training.authentication.domain.dao.UserDAOTest;
import com.sport.training.authentication.domain.service.UserServiceTest;
import com.sport.training.configuration.TestConfig;
import com.sport.training.domain.dao.DisciplineDAOTest;
import com.sport.training.domain.dao.DisciplineUserDAOTest;
import com.sport.training.domain.dao.DiscussionDAOTest;
import com.sport.training.domain.dao.EventDAOTest;
import com.sport.training.domain.dao.EventUserDAOTest;
import com.sport.training.domain.dao.MessageDAOTest;
import com.sport.training.domain.dao.ActivityDAOTest;
import com.sport.training.domain.dao.BookmarkDAOTest;
import com.sport.training.domain.service.SportServiceTest;
import com.sport.training.domain.service.CreditCardServiceTest;
import com.sport.training.domain.service.ShoppingCartTest;
import com.sport.training.util.UniqueIdGeneratorTest;
import com.sport.training.web.DisciplineRestTestClient;
import com.sport.training.web.CreateUserTestWebClient;
import com.sport.training.web.FindUsersControllerTest;
import com.sport.training.web.ManageSportTestWebClient;
import com.sport.training.web.ActivityRestTestClient;
import com.sport.training.web.ShoppingCartWebTest;
import com.sport.training.web.UpdateAccountTestWebClient;
import com.sport.training.web.VisualiseCatalogTestWebClient;
import com.sport.training.web.WebTestMockMvc;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)
@Suite.SuiteClasses({ UserDAOTest.class, UserServiceTest.class, TestConfig.class, 
		ActivityDAOTest.class, BookmarkDAOTest.class, DisciplineDAOTest.class, DisciplineUserDAOTest.class,
		DiscussionDAOTest.class, EventDAOTest.class, EventUserDAOTest.class, MessageDAOTest.class,
		CreditCardServiceTest.class, ShoppingCartTest.class, SportServiceTest.class,
		UniqueIdGeneratorTest.class, 
		ActivityRestTestClient.class, CreateUserTestWebClient.class, DisciplineRestTestClient.class,
		FindUsersControllerTest.class, ManageSportTestWebClient.class, ShoppingCartWebTest.class, 
		UpdateAccountTestWebClient.class, VisualiseCatalogTestWebClient.class, WebTestMockMvc.class })
public class AllTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
		// org.junit.runner.JUnitCore.main("AllTests");
	}

	public static junit.framework.Test suite() {
		JUnit4TestAdapter adapter = new JUnit4TestAdapter(AllTests.class);
		return adapter;
	}

}
