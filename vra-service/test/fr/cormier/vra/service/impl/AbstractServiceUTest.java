package fr.cormier.vra.service.impl;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractServiceUTest {

	private static final String SPRING_CONF_FILE = "spring-service-test.xml";
	
	private static AbstractApplicationContext context;
	
	static {
		context = new ClassPathXmlApplicationContext(SPRING_CONF_FILE);
	}
	
	public AbstractServiceUTest() {

	}
	
	@After
	public void tearDown() throws Exception {

	}
	
	@Before
	public void setUp() throws Exception {

	}
	
	public static <T> T getBean(Class<T> requiredType) throws BeansException {
		return context.getBean(requiredType);
	}
	
}
