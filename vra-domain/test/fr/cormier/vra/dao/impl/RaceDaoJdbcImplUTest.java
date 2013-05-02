package fr.cormier.vra.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.cormier.domain.db.Race;
import fr.cormier.domain.db.RaceStatusEnum;
import fr.cormier.vra.dao.impl.RaceDaoJdbcImpl;

public class RaceDaoJdbcImplUTest extends AbstractDaoJdbcUTest {

	private static RaceDaoJdbcImpl raceDao;
		
	/** TEST DATA */
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final String TEST_NAME = "Test Race";
	private static Date TEST_STARTDATE;
	private static final RaceStatusEnum TEST_STATUS = RaceStatusEnum.OPEN;
	
	static {
		try {
			TEST_STARTDATE = DATEFORMAT.parse("01/01/2013");
		} catch (ParseException e) {
			
		}
		raceDao = getBean(RaceDaoJdbcImpl.class);
	}
	
	private Race raceTest;

	public RaceDaoJdbcImplUTest() {
		super(RaceDaoJdbcImpl.TABLENAME);
	}
	
	public void initRaceDate() {

		int pk = getTestUtilDao().createRace(TEST_NAME, TEST_STARTDATE, TEST_STATUS);
		raceTest = new Race();
		raceTest.setName(TEST_NAME);
		raceTest.setStartDate(TEST_STARTDATE);
		raceTest.setStatus(TEST_STATUS);
		raceTest.setRaceId(pk);
	}
	
	@Test
	public void create_ShouldReturnNewPKValue() {

		// set up
		Race myRace = new Race();
		myRace.setName("MyRace");
		myRace.setStartDate(new Date());
		myRace.setStatus(RaceStatusEnum.OPEN);
		Race myRace2 = myRace.clone();
		myRace2.setName("MyRace2");

		// action
		int pk = raceDao.create(myRace);
		int pk2 = raceDao.create(myRace2);
		
		// assert
		Assert.assertEquals(pk+1, pk2);
	}
	
	@Test
	public void selectAll_ShouldReturnOneResult() {
		
		//setup
		initRaceDate();
		
		//action
		List<Race> races = raceDao.selectAll();
		
		//assert
		Assert.assertNotNull("list of races should'nt be null",races);
		Assert.assertEquals("List of races should contain 1 result",1,races.size());
	}

}
