package fr.cormier.vra.dao.impl;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import fr.cormier.domain.db.UserRace;

public class UserRaceDaoJdbcImplUTest extends AbstractDaoJdbcUTest {

	private static UserRaceDaoJdbcImpl userRaceDao;
		
	/** TEST DATA */
	private static final int TEST_VR_USER_ID = 1;
	private static final int TEST_RACE_ID = 1;
	private static final String TEST_MODE = "SCHEDULE";
	private static final String TEST_USER_SERVICE = "http://vendeeglobevirtuel.virtualregatta.com/core/Service/ServiceCaller.php?service=GetUser&id_user=387602&lang=FR&light=1&auto=1&checksum=c2b5eac7f8914ae98648a8d6f4cac7c15c174176";
	private static final String TEST_ZEZO_URL_TEMPLATE = "http://sail.zezo.org/fr/chart.pl?lat=@LATITUDE&lon=@LONGITUDE&o=0&wind=0clon=6.71875&clat=43.28125";
	static {
		userRaceDao = getBean(UserRaceDaoJdbcImpl.class);
	}
	
	private UserRace userRaceTest;

	public UserRaceDaoJdbcImplUTest() {
		super(UserRaceDaoJdbcImpl.TABLENAME);
	}
	
	public void initUserRaceDate() {

		getTestUtilDao().createUserRace(TEST_VR_USER_ID, TEST_RACE_ID, TEST_MODE, TEST_USER_SERVICE, TEST_ZEZO_URL_TEMPLATE);
		userRaceTest = new UserRace();
		userRaceTest.setVrUserId(TEST_VR_USER_ID);
		userRaceTest.setRaceId(TEST_RACE_ID);
		userRaceTest.setMode(TEST_MODE);
		userRaceTest.setUserService(TEST_USER_SERVICE);
		userRaceTest.setZezoUrlTemplate(TEST_ZEZO_URL_TEMPLATE);
	}
	
	@Test
	public void create_ShouldReturnOne() {

		// set up
		UserRace myUserRace = new UserRace();
		myUserRace.setVrUserId(TEST_VR_USER_ID);
		myUserRace.setRaceId(TEST_RACE_ID);
		myUserRace.setMode(TEST_MODE);
		myUserRace.setUserService(TEST_USER_SERVICE);
		myUserRace.setZezoUrlTemplate(TEST_ZEZO_URL_TEMPLATE);
		UserRace myUserRace2 = myUserRace.clone();
		myUserRace2.setRaceId(2);

		// action
		int result = userRaceDao.create(myUserRace);
		
		// assert
		Assert.assertEquals(1, result);
	}
	
	@Test
	public void selectAll_ShouldReturnOneResult() {
		
		//setup
		initUserRaceDate();
		
		//action
		List<UserRace> UserRaces = userRaceDao.selectAll();
		
		//assert
		Assert.assertNotNull("list of UserRaces should'nt be null",UserRaces);
		Assert.assertEquals("List of UserRaces should contain 1 result",1,UserRaces.size());
	}

}
