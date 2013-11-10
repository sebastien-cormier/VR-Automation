package fr.cormier.vra.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cormier.domain.db.UserRace;
import fr.cormier.vra.dao.IUserRaceDao;
import fr.cormier.vra.service.IUserRaceService;

@Service("serviceUserRace")
public class UserRaceServiceImpl implements IUserRaceService {

	@Autowired
	private IUserRaceDao daoUserRace;

	public String getMode(int vrUserId, int raceId) {
		return daoUserRace.selectByKey(vrUserId, raceId).getMode();
	}
	
	public String getUserService(int vrUserId, int raceId) {
		return daoUserRace.selectByKey(vrUserId, raceId).getUserService();
	}

	public UserRace getUserRace(int vrUserId, int raceId) {
		return daoUserRace.selectByKey(vrUserId, raceId);
	}

	public List<UserRace> retrieveAllScheduleMode() {
		return daoUserRace.selectAllSchedule();
	}

	public List<UserRace> retrieveAllZezoAutoRouting() {
		return daoUserRace.selectAllZezoAutoRouting();
	}

	public List<UserRace> retrieveAll() {
		return daoUserRace.selectAll();
	}
	
}
