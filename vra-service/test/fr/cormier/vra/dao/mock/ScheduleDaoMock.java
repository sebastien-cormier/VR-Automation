package fr.cormier.vra.dao.mock;

import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.cormier.domain.db.Schedule;
import fr.cormier.vra.dao.IScheduleDao;

public class ScheduleDaoMock implements IScheduleDao {

	@Override
	public int create(Schedule schedule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Schedule> selectScheduleToRotate(int vrUserId, int raceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int setFinishStatus(Set<Integer> scheduleIds) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Schedule schedule) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Schedule retrieveScheduleBefore(int vrUserId, int raceId,
			Date startDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Schedule> selectCurrentSchedule(int vrUserId, int raceId) {
		// TODO Auto-generated method stub
		return null;
	}

}
