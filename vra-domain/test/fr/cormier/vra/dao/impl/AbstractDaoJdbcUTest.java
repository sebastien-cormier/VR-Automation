package fr.cormier.vra.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractDaoJdbcUTest {
		
	private String tableName;
	
	private static TestUtilDaoJdbc testUtilDao;
	
	private static final String SPRING_CONF_FILE = "spring-domain-test.xml";
	
	private static AbstractApplicationContext context;
	
	static {
		context = new ClassPathXmlApplicationContext(SPRING_CONF_FILE);
        testUtilDao = context.getBean(TestUtilDaoJdbc.class);
	}
	
	public AbstractDaoJdbcUTest(String tableName) {
		this.tableName = tableName;
	}
	
	@After
	public void tearDown() throws Exception {
		
		testUtilDao.dropTable(tableName);
	}
	
	@Before
	public void setUp() throws Exception {
		testUtilDao.dropTable(tableName);
		testUtilDao.createTable(tableName);
	}
	
	public static <T> T getBean(Class<T> requiredType) throws BeansException {
		return context.getBean(requiredType);
	}
	
	public TestUtilDaoJdbc getTestUtilDao() {
		return testUtilDao;
	}
}
