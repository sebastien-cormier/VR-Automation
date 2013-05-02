package fr.cormier.vra.dao.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.cormier.domain.db.Race;
import fr.cormier.domain.db.RaceStatusEnum;
import fr.cormier.vra.dao.IRaceDao;

public class RaceDaoMock implements IRaceDao {

	@Override
	public int create(Race race) {
		return 1;
	}

	@Override
	public List<Race> selectAll() {
		List<Race> races = new ArrayList<Race>();
		Race race = new Race();
		race.setName("vendeeglobevirtuel.virtualregatta.com");
		race.setRaceId(1);
		race.setStartDate(new Date());
		race.setStatus(RaceStatusEnum.OPEN);
		races.add(race);
		race = race.clone();
		race.setName("Race2");
		races.add(race);
		return races;
	}

}
