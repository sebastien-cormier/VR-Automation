package fr.cormier.vra.service.impl;

import org.junit.Assert;

import org.junit.Test;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.external.Position;
import fr.cormier.domain.external.RoutingCommand;
import fr.cormier.vra.service.IRoutingService;

public class RoutingCommandServiceImplUTest extends AbstractServiceUTest {
	
	private static RoutingServiceImpl serviceRouting;
	
	private static String ZEZO_ROUTING_LINE = "<img src=\"img/dot.png\" class=\"abs\" style=\"z-index: 1; left :-140px; top:1305px;\" \n"+
	"onmouseover=\"updi(event,'2013-11-08 14:30 UTC (T+ 0:00)<br>Distances:&nbsp;0.0nm/2434.1nm<br><b>Wind:</b> 223&deg; 14.0 kt (<b>TWA 64&deg;</b>)<br><b>Heading:</b> 287&deg; <b>Sail:</b> Jib<br><b>Boat Speed:</b> 11.32 kts','220px')\" onmouseout=\"cleari()\" \n"+ 
	"onmousedown=\"show_wind(0);\">\";";
	
	static {
		serviceRouting = (RoutingServiceImpl)getBean(IRoutingService.class);
		serviceRouting.setMockHtml("/test_zezo.htm");
	}
	
	@Test
	public void retrieveCurrentRoutingCommand() {
		
		//setup
		Position position = new Position();
		position.setLatitude(-41.833988566);
		position.setLongitude(174.645345061);
		
		//action
		//serviceRouting.setMockHtml("test_zezo_01.htm");
		RoutingCommand result = serviceRouting.retrieveCurrentRoutingCommand(position);
		
		//assert
		RoutingCommand expected = new RoutingCommand();
		expected.setSail(SailEnum.JIB);
		expected.setHeading(287);
		Assert.assertEquals(expected, result);
	}

	@Test
	public void parseHeading() {
		
		//setup
		String line = ZEZO_ROUTING_LINE;

		//action
		int heading = serviceRouting.parseHeading(line);
		
		// assert
		Assert.assertEquals(287, heading);
	}

	@Test
	public void parseSail() {
		
		//setup
		String line = ZEZO_ROUTING_LINE;
		
		//action
		SailEnum sail = serviceRouting.parseSail(line);
		
		// assert
		Assert.assertEquals(SailEnum.JIB, sail);
	}
	
	
}
