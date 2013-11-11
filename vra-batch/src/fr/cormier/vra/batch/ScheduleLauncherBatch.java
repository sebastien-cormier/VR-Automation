package fr.cormier.vra.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import fr.cormier.domain.db.UserRace;
import fr.cormier.vra.service.IRoutingService;
import fr.cormier.vra.service.IScheduleService;
import fr.cormier.vra.service.IUserRaceService;

public class ScheduleLauncherBatch {


	private final static Log logger = LogFactory.getLog(ScheduleLauncherBatch.class);

	@Autowired
	private IUserRaceService userRaceService;

	@Autowired
	private IScheduleService scheduleService;

	@Autowired
	private IRoutingService serviceRouting;

	public ScheduleLauncherBatch() {

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		String conf = "spring.xml";
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				conf);
		ConfigurableEnvironment configurableEnvironment = new StandardEnvironment();
		
		if (args != null && args.length > 0) {
			if ("-test".equals(args[0])) {
				configurableEnvironment.setActiveProfiles("TEST");

			} else {
				configurableEnvironment.setActiveProfiles("PROD");
			}
		} else {
			configurableEnvironment.setActiveProfiles("PROD");
		}
		context.setEnvironment(configurableEnvironment);

		PropertyConfigurator.configure("resources/log4j.properties");

		logger.info("Starting VRA schedule batch...");

		int iteration = 0;

		ScheduleLauncherBatch launcher = context.getBean("scheduleLauncherBatch", ScheduleLauncherBatch.class);

		while (true) {

			iteration++;

			logger.info("Iteration #" + iteration + " at " + new Date());

			logger.info(" Launching scheduled instructions...");
			launcher.runSchedule();
			
			Date nextIteration = getNextIteration();
			long msToSleep = getSecondRemainingBefore(nextIteration);
			logger.info("Waiting for next iteration at " + nextIteration
					+ "\n\n\n");
			Thread.sleep(msToSleep);
			
		}
		
	}
	
	private void runSchedule() {

		List<UserRace> userRaces = userRaceService.retrieveAllScheduleMode();

		// Traitement des schedule a traiter
		scheduleService.processScheduleRotation(userRaces);

		// Verification des schedule en cours
		scheduleService.checkAllCurrentSchedule(userRaces);

	}

	private static long getSecondRemainingBefore(Date theDate) {
		Date now = new Date();
		long timeToSeepMS = theDate.getTime() - now.getTime();
		return timeToSeepMS;
	}

	private static Date getNextIteration() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.MINUTE, now.get(Calendar.MINUTE)+1);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}
}
