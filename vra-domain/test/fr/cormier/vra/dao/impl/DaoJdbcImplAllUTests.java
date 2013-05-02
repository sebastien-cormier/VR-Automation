package fr.cormier.vra.dao.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CommandDaoJdbcImplUTest.class, RaceDaoJdbcImplUTest.class, ScheduleDaoJdbcImplUTest.class,
		UserRaceDaoJdbcImplUTest.class })
public class DaoJdbcImplAllUTests {

}
