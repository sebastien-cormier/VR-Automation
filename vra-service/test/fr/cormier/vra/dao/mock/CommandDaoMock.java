package fr.cormier.vra.dao.mock;

import java.util.ArrayList;
import java.util.List;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.vra.dao.ICommandDao;

public class CommandDaoMock implements ICommandDao {

	@Override
	public int create(Command command) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Command select(int userId, int raceId, CommandTypeEnum commandType,
			String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Command selectByKey(int commandId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateChecksumAndR(Command command) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Command findByValues(int vrUserId, int raceId,
			String commandTypeHeading, String heading) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Command> select(int vrUserId, int raceId) {
		List<Command> results = new ArrayList<Command>();
		for(int cap=0; cap<60; cap++) {
			Command command = new Command();
			command.setCommandId(cap+100);
			command.setCommandType(CommandTypeEnum.HEADING);
			command.setValue(String.valueOf(cap));
			command.setChecksum("blablabla");
			command.setR("plop");
			command.setRaceId(raceId);
			command.setVrUserId(vrUserId);
			results.add(command);	
		}
		Command command = new Command();
		command.setCommandId(98);
		command.setCommandType(CommandTypeEnum.SAIL);
		command.setValue(String.valueOf(1));
		command.setChecksum("blablabla");
		command.setR("plop");
		command.setRaceId(raceId);
		command.setVrUserId(vrUserId);
		results.add(command);	
		command = new Command();
		command.setCommandId(99);
		command.setCommandType(CommandTypeEnum.SAIL);
		command.setValue(String.valueOf(2));
		command.setChecksum("blablabla");
		command.setR("plop");
		command.setRaceId(raceId);
		command.setVrUserId(vrUserId);
		results.add(command);	
		return results;
	}

}
