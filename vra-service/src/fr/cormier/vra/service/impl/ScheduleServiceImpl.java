package fr.cormier.vra.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.domain.db.Schedule;
import fr.cormier.domain.db.ScheduleStatusEnum;
import fr.cormier.domain.db.UserRace;
import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;
import fr.cormier.vra.dao.IScheduleDao;
import fr.cormier.vra.service.IScheduleService;

@Service("serviceSchedule")
public class ScheduleServiceImpl implements IScheduleService{

	private final static Log logger = LogFactory.getLog(ScheduleServiceImpl.class);
	
	@Autowired
	private IScheduleDao daoSchedule = null;
	
	@Autowired
	private CommandServiceImpl commandService = null;
	
	@Autowired
	private BoatInfoService serviceBoatInfo = null;
	
	@Autowired
	private RaceServiceImpl serviceRace = null;
	
	
	public ScheduleServiceImpl() {
	}
	
	/**
	 * Update all schedule that expire, and apply new setting to the boats
	 */
	public void processScheduleRotation(List<UserRace> userRaces) {
		
		for (UserRace userRace : userRaces) {
			
			// Retrieve all PENDING schedule that become CURRENT
			List<Schedule> schedules = daoSchedule.selectScheduleToRotate(userRace.getVrUserId(), userRace.getRaceId());
			if( schedules==null || schedules.size()==0 ) {
				logger.info("No schedule rotation to do for user id "+userRace.getVrUserId()+" in race id "+serviceRace.getRaceName(userRace.getRaceId()));
			} else {
				logger.info("Found "+schedules.size()+" to rotate for user id "+userRace.getVrUserId()+" in race id "+serviceRace.getRaceName(userRace.getRaceId()));
				
				for (Schedule schedule : schedules) {
					
					// retrieve old schedule
					Schedule oldSchedule = this.getPreviousSchedule(schedule);
					
					// Rotate schedule status
					if( oldSchedule!=null ) {	
						oldSchedule.setStatus(ScheduleStatusEnum.FINISH);
						daoSchedule.update(oldSchedule);
					}
					schedule.setStatus(ScheduleStatusEnum.CURRENT);
					daoSchedule.update(schedule);

									
					// Apply new HEADING if needed (compare to old schedule)
					if( oldSchedule==null || oldSchedule.getHeading()!=schedule.getHeading() ) {
						if( oldSchedule==null ) {
							logger.info("Changing heading for "+schedule.getHeading());
						} else {
							logger.info("Changing heading from "+oldSchedule.getHeading()+" to "+schedule.getHeading());
						}
						boolean commandExecuted = commandService.runCommand(schedule.getVrUserId(), schedule.getRaceId(), CommandTypeEnum.HEADING, String.valueOf(schedule.getHeading()));
						if( !commandExecuted ) {
							logger.warn("No command found (or can not be executed) to apply heading of schedule id "+schedule.getScheduleId());
						}
					}
					
					// Apply new HEADING and SAIL if needed (compare to old schedule)
					if( oldSchedule==null || oldSchedule.getSail()!=schedule.getSail() ) {
						if( oldSchedule==null ) {
							logger.info("Changing sail for "+schedule.getSail());
						} else {
							logger.info("Changing sail from "+oldSchedule.getSail()+" to "+schedule.getSail());
						}
						boolean commandFound = commandService.runCommand(schedule.getVrUserId(), schedule.getRaceId(), CommandTypeEnum.SAIL, String.valueOf(schedule.getSail()));
						if( !commandFound ) {
							logger.warn("No command found to apply sail of schedule id "+schedule.getScheduleId());
						}
					}

					
				}
				
			}
			
		}
		
	
	}

	private Schedule getPreviousSchedule(Schedule schedule) {
		return daoSchedule.retrieveScheduleBefore(schedule.getVrUserId(), schedule.getRaceId(), schedule.getStartDate());
	}

	/**
	 * Check if all current schedule are applied on vr
	 * @param userRaces 
	 */
	public void checkAllCurrentSchedule(List<UserRace> userRaces) {

		for (UserRace userRace : userRaces) {
			
			// Retrieve all current schedule
			List<Schedule> schedules = daoSchedule.selectCurrentSchedule(userRace.getVrUserId(), userRace.getRaceId());
			
			if( schedules==null || schedules.size()==0 ) {
				logger.info("No schedule to check for user race "+userRace);
				continue;
			}
			
			logger.info("Checking "+schedules.size()+" schedule ...");
			
			// FOR ALL
			for (Schedule schedule : schedules) {
				
				// Check boat data and compare to the schedule
				// If wrong data => re-apply settings and send a warning
				BoatsPositionWrapper boatsPositionWrapper = serviceBoatInfo.retrieveBoatPosition(schedule.getVrUserId(), schedule.getRaceId());
				if( boatsPositionWrapper==null ) {
					logger.warn("No position found, check skipped...");
				} else {
					if( boatsPositionWrapper.getHeading()==schedule.getHeading() &&
							boatsPositionWrapper.getSail()==schedule.getSail().getValue() ) {
						logger.info("Heading and sail for userId is "+schedule.getHeading()+"/"+schedule.getSail()+" as expected :-)");
					} else {
						if( boatsPositionWrapper.getHeading()!=schedule.getHeading() ) {
							logger.warn("Incorrect heading ("+boatsPositionWrapper.getHeading()+") for user id  "+schedule.getVrUserId()+", trying to fix the head to "+schedule.getHeading());
							boolean commandFound = commandService.runCommand(schedule.getVrUserId(), schedule.getRaceId(), CommandTypeEnum.HEADING, String.valueOf(schedule.getHeading()));
							if( !commandFound ) {
								logger.warn("No command found to apply heading of schedule id "+schedule.getScheduleId());
							}
						}
						if( boatsPositionWrapper.getSail()!=schedule.getSail().getValue() ) {
							logger.warn("Incorrect sail ("+SailEnum.getStringValue(boatsPositionWrapper.getSail())+") for user id  "+schedule.getVrUserId()+", trying to fix the head to "+schedule.getSail());
							boolean commandFound = commandService.runCommand(schedule.getVrUserId(), schedule.getRaceId(), CommandTypeEnum.SAIL, String.valueOf(schedule.getSail()));
							if( !commandFound ) {
								logger.warn("No command found to apply sail of schedule id "+schedule.getScheduleId());
							}
						}
						
					}
				}
			}
				
		}

		
		
		
	}

}
