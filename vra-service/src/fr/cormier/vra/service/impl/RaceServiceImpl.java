package fr.cormier.vra.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cormier.domain.db.Race;
import fr.cormier.vra.dao.IRaceDao;
import fr.cormier.vra.service.IRaceService;

@Service("serviceRace")
public class RaceServiceImpl implements IRaceService {

	public static final int NO_RACE_FOUND = -1;

	@Autowired
	private IRaceDao daoRace;

	private static List<Race> races = null;

	public int addRace(Race race) {
		races = null;
		race.setStatus("PENDING");
		return daoRace.create(race);
	}

	private List<Race> getRaces() {
		if (races == null) {
			races = daoRace.selectAll();
		}
		return races;
	}

	public int getRaceId(String name) {
		if (name != null) {
			for (Race race : getRaces()) {
				if (name.equals(race.getName())) {
					return race.getRaceId();
				}
			}
		}
		return NO_RACE_FOUND;
	}

	public String getRaceName(int raceId) {
		for (Race race : getRaces()) {
			if (raceId==race.getRaceId()) {
				return race.getName();
			}
		}
		return null;
	}

}
