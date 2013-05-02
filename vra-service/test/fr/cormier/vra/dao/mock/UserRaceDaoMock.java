package fr.cormier.vra.dao.mock;

import java.util.List;

import fr.cormier.domain.db.UserRace;
import fr.cormier.vra.dao.IUserRaceDao;

public class UserRaceDaoMock implements IUserRaceDao {

	@Override
	public int create(UserRace userRace) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<UserRace> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRace selectByKey(int vrUserId, int raceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRace> selectAllSchedule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRace> selectAllZezoAutoRouting() {
		// TODO Auto-generated method stub
		return null;
	}

}
