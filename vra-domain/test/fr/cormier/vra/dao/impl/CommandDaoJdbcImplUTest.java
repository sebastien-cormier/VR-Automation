package fr.cormier.vra.dao.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.vra.dao.impl.CommandDaoJdbcImpl;

public class CommandDaoJdbcImplUTest extends AbstractDaoJdbcUTest {

	private static CommandDaoJdbcImpl commandDao;
	
	/** TEST DATA */
	private static final int TEST_VR_USER_ID = 1;
	private static final int TEST_RACE_ID = 1;
	private static final CommandTypeEnum TEST_COMMAND_TYPE = CommandTypeEnum.HEADING;
	private static final String TEST_CHECKSUM = "---";
	private static final String TEST_R = "---";
	private static final String TEST_VALUE = "54";
	
	static {
		commandDao = getBean(CommandDaoJdbcImpl.class);
	}
	
	private Command commandTest;

	public CommandDaoJdbcImplUTest() {
		super(CommandDaoJdbcImpl.TABLENAME);
	}
	
	public void initTestDate() {

		int pk = getTestUtilDao().createCommand(TEST_COMMAND_TYPE, TEST_VR_USER_ID, TEST_RACE_ID, TEST_VALUE, TEST_R, TEST_CHECKSUM);

		commandTest = new Command();
		commandTest.setVrUserId(TEST_VR_USER_ID);
		commandTest.setRaceId(TEST_RACE_ID);
		commandTest.setValue(TEST_VALUE);
		commandTest.setCommandType(TEST_COMMAND_TYPE);
		commandTest.setR(TEST_R);
		commandTest.setChecksum(TEST_CHECKSUM);
		commandTest.setCommandId( pk );
	}
	
	@Test
	public void create_ShouldReturnNewPKValue() {

		// set up
		Command myCommand = new Command();
		myCommand.setVrUserId(1);
		myCommand.setRaceId(2);
		myCommand.setValue("100");
		myCommand.setCommandType(CommandTypeEnum.HEADING);
		myCommand.setR("---");
		myCommand.setChecksum("---");
		myCommand.setCommandId( commandDao.create(myCommand) );
		Command myCommand2 = (Command) myCommand.clone();
		myCommand2.setValue("101");

		// action
		int pk = commandDao.create(myCommand);
		int pk2 = commandDao.create(myCommand2);
		
		// assert
		Assert.assertEquals(pk+1, pk2);
	}
	
	@Test
	public void select_ShouldReturnCompleteObjectWhenUsingValue() {
		
		// setup
		initTestDate();
		
		// action
		Command commandRead = commandDao.select(commandTest.getVrUserId(), commandTest.getVrUserId(), commandTest.getCommandType(), commandTest.getValue());

		// assert
		Assert.assertEquals(commandRead, commandTest);
		
	}

	@Test
	public void select_ShouldReturnCompleteObjectWhenNotUsingValue() {
		
		// setup
		initTestDate();

		// action
		List<Command> commands = commandDao.select(commandTest.getVrUserId(), commandTest.getVrUserId());

		// assert
		Assert.assertEquals(1, commands.size());
		Assert.assertEquals(commands.get(0), commandTest);
	}

	@Test
	public void selectByKey_ShouldReturnOneResult() {
	
		// setup
		initTestDate();

		// action
		Command commandRead = commandDao.selectByKey(commandTest.getCommandId());

		// assert
		Assert.assertEquals(commandRead, commandTest);

	}
	
	@Test
	public void updateChecksumAndR_SHouldRunWithouException() {
		
		// setup
		initTestDate();

		// setuo
		commandTest.setValue("50");
		
		// action
		commandDao.updateChecksumAndR(commandTest);
		
		
	}

	@Test
	public void findByValues_ShouldReturnOneResult() {

		// setup
		initTestDate();
		
		// action
		Command myCommand = commandDao.findByValues(TEST_VR_USER_ID, TEST_RACE_ID, String.valueOf(TEST_COMMAND_TYPE), TEST_VALUE);
		
		// assert
		Assert.assertEquals(commandTest, myCommand);
	}

}
