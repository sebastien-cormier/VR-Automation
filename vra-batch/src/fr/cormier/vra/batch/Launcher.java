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

public class Launcher {


	private final static Log logger = LogFactory.getLog(Launcher.class);

	@Autowired
	private IUserRaceService userRaceService;

	@Autowired
	private IScheduleService scheduleService;

	@Autowired
	private IRoutingService serviceRouting;

	public Launcher() {

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
		boolean testMode = false;
		
		if (args != null && args.length > 0) {
			if ("-test".equals(args[0])) {
				configurableEnvironment.setActiveProfiles("TEST");
				testMode = true;
			} else {
				configurableEnvironment.setActiveProfiles("PROD");
			}
		} else {
			configurableEnvironment.setActiveProfiles("PROD");
		}
		context.setEnvironment(configurableEnvironment);

		PropertyConfigurator.configure("resources/log4j.properties");

		logger.info("Starting launcher...");

		int iteration = 0;

		Launcher launcher = context.getBean("launcher", Launcher.class);

		while (true) {

			iteration++;

			logger.info("Iteration #" + iteration + " at " + new Date());

			/*
			 * logger.info(" Launching scheduled instructions...");
			 * launcher.runSchedule();
			 */

			logger.info(" Launching zezo automatic routing...");
			launcher.runZezoAutoRouting(testMode);

			logger.info(" Launching Monitoring...");
			launcher.runMonitoring(testMode);

			/*
			 * logger.info(" Launching waypoints..."); launcher.runWaypoints();
			 * 
			 * logger.info(" Launching speed regulation...");
			 * launcher.speedRegulation();
			 */

			Date nextIteration = getNextIteration();
			long msToSleep = getSecondRemainingBefore(nextIteration);
			logger.info("Waiting for next iteration at " + nextIteration
					+ "\n\n\n");
			Thread.sleep(msToSleep);
			
		}
		
	}

	private void runMonitoring(boolean testMode) {
		List<UserRace> userRaces = userRaceService.retrieveAllScheduleMode();

		
	}

	private void runZezoAutoRouting(boolean testMode) {

		List<UserRace> userRaces = userRaceService.retrieveAllZezoAutoRouting();

		// Traitement des schedule traiter
		serviceRouting.setTestMode(testMode);
		serviceRouting.processZezoRouting(userRaces);

	}

	private void speedRegulation() {
		logger.info(" no speed regulation to check");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void runWaypoints() {
		logger.info(" no waypoints to check");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		int minuteByTen = now.get(Calendar.MINUTE) / 10;
		now.set(Calendar.MINUTE, minuteByTen * 10 + 10);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}
}
