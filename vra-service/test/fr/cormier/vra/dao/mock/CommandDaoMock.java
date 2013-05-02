package fr.cormier.vra.dao.mock;

import java.util.List;

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
		// TODO Auto-generated method stub
		return null;
	}

}
