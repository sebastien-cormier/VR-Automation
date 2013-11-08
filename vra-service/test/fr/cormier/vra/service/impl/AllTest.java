package fr.cormier.vra.service.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ BoatInfoServiceImplUTest.class, CommandServiceImplUTest.class,
		RoutingCommandServiceImplUTest.class })
public class AllTest {

}
