package fr.cormier.vra.dao.impl;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.BoatPositionHistory;

public class BoatPositionHistoryDaoJdbcImplUTest extends AbstractDaoJdbcUTest {

	private static BoatPositionHistoryDaoJdbcImpl boatPositionHistoryDao;
	
	/** TEST DATA */
	private static final int TEST_VR_USER_ID = 1;
	private static final int TEST_RACE_ID = 1;
	private static final Date TEST_DATE_CREATION = new Date();
	private static final SailEnum TEST_SAIL = SailEnum.JIB;
	private static final int TEST_HEADING = 90;
	private static final double TEST_LATITUDE = -145.96435775;
	private static final double TEST_LONGITUDE = -145.96435775;
	private static final boolean TEST_HASCHANGECASE = true;
	

	static {
		boatPositionHistoryDao = getBean(BoatPositionHistoryDaoJdbcImpl.class);
	}
	
	private BoatPositionHistory boatPositionHistoryTest;

	public BoatPositionHistoryDaoJdbcImplUTest() {
		super(BoatPositionHistoryDaoJdbcImpl.TABLENAME);
	}
	
	public void initTestDate() {

		int pk = getTestUtilDao().createBoatPositionHistory(TEST_VR_USER_ID, TEST_RACE_ID, TEST_DATE_CREATION, TEST_HEADING, TEST_SAIL, TEST_LATITUDE, TEST_LONGITUDE, TEST_HASCHANGECASE);

		boatPositionHistoryTest = new BoatPositionHistory();
		boatPositionHistoryTest.setId(pk);
		boatPositionHistoryTest.setVrUserId(TEST_VR_USER_ID);
		boatPositionHistoryTest.setRaceId(TEST_RACE_ID);
		boatPositionHistoryTest.setCreationDate(TEST_DATE_CREATION);
		boatPositionHistoryTest.setHeading(TEST_HEADING);
		boatPositionHistoryTest.setSail(TEST_SAIL);
		boatPositionHistoryTest.setLatitude(TEST_LATITUDE);
		boatPositionHistoryTest.setLongitude(TEST_LONGITUDE);
		boatPositionHistoryTest.setHasChangedCase(TEST_HASCHANGECASE);
	}
	
	@Test
	public void create_ShouldReturnNewPKValue() {

		// set up
		BoatPositionHistory myBoatPosHist =new BoatPositionHistory();
		myBoatPosHist.setVrUserId(TEST_VR_USER_ID);
		myBoatPosHist.setRaceId(TEST_RACE_ID);
		myBoatPosHist.setCreationDate(TEST_DATE_CREATION);
		myBoatPosHist.setHeading(TEST_HEADING);
		myBoatPosHist.setSail(TEST_SAIL);
		myBoatPosHist.setLatitude(TEST_LATITUDE);
		myBoatPosHist.setLongitude(TEST_LONGITUDE);
		myBoatPosHist.setHasChangedCase(TEST_HASCHANGECASE);
		BoatPositionHistory myBoatPosHist2 = myBoatPosHist.clone();

		// action
		int pk = boatPositionHistoryDao.create(myBoatPosHist);
		int pk2 = boatPositionHistoryDao.create(myBoatPosHist2);
		
		// assert
		Assert.assertEquals(pk+1, pk2);
	}

	@Test
	public void selectLastPosition() {

		// set up
		initTestDate();
		
		// action
		BoatPositionHistory boatPositionHistory = boatPositionHistoryDao.selectLastPosition(TEST_VR_USER_ID,TEST_RACE_ID);
		
		// assert
		Assert.assertEquals(boatPositionHistoryTest, boatPositionHistory);
	}

}
