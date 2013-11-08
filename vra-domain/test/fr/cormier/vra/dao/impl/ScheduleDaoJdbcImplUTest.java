package fr.cormier.vra.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import org.junit.Test;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.Schedule;
import fr.cormier.domain.db.ScheduleStatusEnum;
import fr.cormier.domain.db.ScheduleTestStatusEnum;
import fr.cormier.vra.dao.impl.ScheduleDaoJdbcImpl;

public class ScheduleDaoJdbcImplUTest extends AbstractDaoJdbcUTest {

	private static ScheduleDaoJdbcImpl scheduleDao;
		
	/** TEST DATA */
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	private static final int TEST_VR_USER_ID = 1;
	private static final int TEST_RACE_ID = 1;
	private static final int TEST_HEADING = 190;
	private static final SailEnum TEST_SAIL = SailEnum.JIB;
	private static Date TEST_STARTDATE;
	private static final ScheduleStatusEnum TEST_STATUS = ScheduleStatusEnum.CURRENT;
	private static final ScheduleTestStatusEnum TEST_TEST_STATUS = ScheduleTestStatusEnum.CHECKED;
	
	static {
		try {
			TEST_STARTDATE = DATEFORMAT.parse("01/01/2013 11:11:11");
		} catch (ParseException e) {
			
		}
		scheduleDao = getBean(ScheduleDaoJdbcImpl.class);
	}
	
	private Schedule scheduleTest;

	public ScheduleDaoJdbcImplUTest() {
		super(ScheduleDaoJdbcImpl.TABLENAME);
	}
	
	public int initScheduleDate() {

		int pk = getTestUtilDao().createSchedule(TEST_VR_USER_ID, TEST_RACE_ID, TEST_HEADING, TEST_SAIL, TEST_STARTDATE, TEST_STATUS, TEST_TEST_STATUS);
		scheduleTest = new Schedule();
		scheduleTest.setVrUserId(TEST_VR_USER_ID);
		scheduleTest.setRaceId(TEST_RACE_ID);
		scheduleTest.setHeading(TEST_HEADING);
		scheduleTest.setSail(TEST_SAIL);
		scheduleTest.setStartDate(TEST_STARTDATE);
		scheduleTest.setStatus(TEST_STATUS);
		scheduleTest.setTestStatus(TEST_TEST_STATUS);
		scheduleTest.setScheduleId(pk);
		return pk;
	}
	
	@Test
	public void create_ShouldReturnNewPKValue() {

		// set up
		Schedule mySchedule = new Schedule();
		mySchedule.setVrUserId(TEST_VR_USER_ID);
		mySchedule.setRaceId(TEST_RACE_ID);
		mySchedule.setHeading(TEST_HEADING);
		mySchedule.setSail(TEST_SAIL);
		mySchedule.setStartDate(TEST_STARTDATE);
		mySchedule.setStatus(TEST_STATUS);
		mySchedule.setTestStatus(TEST_TEST_STATUS);
		Schedule mySchedule2 = mySchedule.clone();
		mySchedule2.setHeading(180);
		mySchedule2.setStartDate(new Date());

		// action
		int pk = scheduleDao.create(mySchedule);
		int pk2 = scheduleDao.create(mySchedule2);
		
		// assert
		Assert.assertEquals(pk+1, pk2);
	}
	

	@Test
	public void selectScheduleToRotate_WithOneToRotate_ShouldReturnHisPK() throws ParseException {
		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		int pk = getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, TEST_STARTDATE, ScheduleStatusEnum.PENDING, ScheduleTestStatusEnum.TO_TEST);
		
		//action
		List<Schedule> schedules = scheduleDao.selectScheduleToRotate(vruserId, raceId);
		
		//assert
		Assert.assertEquals("Should return one result only",1,schedules.size());
		Assert.assertEquals("Should return schedule id : "+pk, pk,schedules.get(0).getScheduleId());
		
	}

	@Test
	public void selectScheduleToRotate_WithNoPendingSchedule_ShouldReturnEmptyList() throws ParseException {
		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, TEST_STARTDATE, ScheduleStatusEnum.CURRENT, ScheduleTestStatusEnum.TO_TEST);
		
		//action
		List<Schedule> schedules = scheduleDao.selectScheduleToRotate(vruserId, raceId);
		
		//assert
		Assert.assertEquals("Should return no result",0,schedules.size());
		
	}

	@Test
	public void selectScheduleToRotate_WithFututPendingSchedule_ShouldReturnEmptyList() throws ParseException {
		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, DATEFORMAT.parse("01/01/2050 11:11:11"), ScheduleStatusEnum.PENDING, ScheduleTestStatusEnum.TO_TEST);
		
		//action
		List<Schedule> schedules = scheduleDao.selectScheduleToRotate(vruserId, raceId);
		
		//assert
		Assert.assertEquals("Should return no result",0,schedules.size());
		
	}

	@Test
	public void selectScheduleToRotate_WithAnotherRace_ShouldReturnEmptyList() throws ParseException {
		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, TEST_STARTDATE, ScheduleStatusEnum.PENDING, ScheduleTestStatusEnum.TO_TEST);
		
		//action
		List<Schedule> schedules = scheduleDao.selectScheduleToRotate(vruserId, raceId+1);
		
		//assert
		Assert.assertEquals("Should return no result",0,schedules.size());
		
	}

	@Test
	public void selectScheduleToRotate_WithAnotherUser_ShouldReturnEmptyList() throws ParseException {
		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, TEST_STARTDATE, ScheduleStatusEnum.PENDING, ScheduleTestStatusEnum.TO_TEST);
		
		//action
		List<Schedule> schedules = scheduleDao.selectScheduleToRotate(vruserId+1, raceId);
		
		//assert
		Assert.assertEquals("Should return no result",0,schedules.size());
		
	}

	@Test
	public void setFinishStatus_With3Ids_ShouldReturnTwo() throws ParseException {
		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		Set<Integer> ids = new HashSet<Integer>();
		ids.add(getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, DATEFORMAT.parse("01/01/2013 11:11:11"), ScheduleStatusEnum.CURRENT, ScheduleTestStatusEnum.TO_TEST));
		ids.add(getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, DATEFORMAT.parse("02/01/2013 11:11:11"), ScheduleStatusEnum.CURRENT, ScheduleTestStatusEnum.TO_TEST));
		ids.add(getTestUtilDao().randId());
		
		//action
		int result = scheduleDao.setFinishStatus(ids);
		
		//assert
		Assert.assertEquals("Should update only 2 first schedules",2,result);

	}

	@Test
	public void update_WithOneUpdate_ShouldReturnOne() {
		
		//setup
		initScheduleDate();
		scheduleTest.setHeading(100);
		
		//action
		int result = scheduleDao.update(scheduleTest);
		
		// assert
		Assert.assertEquals(1, result);
	}

	@Test
	public void retrieveScheduleBefore_With2Schedule_ShouldReturnFirstOne() throws ParseException {

		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		int pk1 = getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, DATEFORMAT.parse("01/01/2013 11:11:11"), ScheduleStatusEnum.CURRENT, ScheduleTestStatusEnum.CHECKED);
		getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, DATEFORMAT.parse("02/01/2013 11:11:11"), ScheduleStatusEnum.PENDING, ScheduleTestStatusEnum.TO_TEST);

		//action
		Schedule schedule = scheduleDao.retrieveScheduleBefore(vruserId, raceId, DATEFORMAT.parse("02/01/2013 11:11:11"));
		
		//assert
		Assert.assertEquals(pk1,schedule.getScheduleId());
	}

	@Test
	public void retrieveScheduleBefore_ShouldRetrieveRightHourMinutesSecond() throws ParseException {

		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		Date dateToTest = DATEFORMAT.parse("01/01/2013 11:11:11");
		getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, dateToTest, ScheduleStatusEnum.CURRENT, ScheduleTestStatusEnum.CHECKED);

		//action
		Schedule schedule = scheduleDao.retrieveScheduleBefore(vruserId, raceId, DATEFORMAT.parse("02/01/2013 11:11:11"));
		
		//assert
		Assert.assertEquals(dateToTest,schedule.getStartDate());
	}

	@Test
	public void selectCurrentSchedule_WithOneCurrent_ShouldReturnOne() throws ParseException {
		
		//setup
		int vruserId = getTestUtilDao().randId();
		int raceId = getTestUtilDao().randId();
		int pk1 = getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, DATEFORMAT.parse("01/01/2013 11:11:11"), ScheduleStatusEnum.CURRENT, ScheduleTestStatusEnum.CHECKED);
		getTestUtilDao().createSchedule(vruserId, raceId, 1, SailEnum.JIB, DATEFORMAT.parse("02/01/2013 11:11:11"), ScheduleStatusEnum.PENDING, ScheduleTestStatusEnum.TO_TEST);

		//action
		List<Schedule> schedules = scheduleDao.selectCurrentSchedule(vruserId, raceId);
		
		//assert
		Assert.assertEquals(1,schedules.size());
		Assert.assertEquals(pk1,schedules.get(0).getScheduleId());

		
	}
	
}
