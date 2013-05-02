package fr.cormier.vra.service.impl;

import junit.framework.Assert;

import org.junit.Test;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.external.Position;
import fr.cormier.domain.external.RoutingCommand;
import fr.cormier.vra.service.IRoutingService;

public class RoutingCommandServiceImplUTest extends AbstractServiceUTest {
	
	private static RoutingServiceImpl serviceRouting;
	
	static {
		serviceRouting = (RoutingServiceImpl)getBean(IRoutingService.class);
		serviceRouting.setMockHtml("/test_zezo_03.htm");
	}
	
	@Test
	public void retrieveCurrentRoutingCommand() {
		
		//setup
		Position position = new Position();
		position.setLatitude(-41.833988566);
		position.setLongitude(174.645345061);
		
		//action
		RoutingCommand result = serviceRouting.retrieveCurrentRoutingCommand(position);
		
		//assert
		RoutingCommand expected = new RoutingCommand();
		expected.setSail(SailEnum.JIB);
		expected.setHeading(122);
		Assert.assertEquals(expected, result);
	}

	@Test
	public void parseHeading() {
		
		//setup
		String line = " <img src=\"./Sailing Simulator  Austral Pursuit_files/dot.png\" class=\"abs\" style=\"z-index: 1; left :22353px; top:16873px;\" onmouseover=\"updi(event,&#39;2013-02-24 08:10 UTC (T+ 0:00)&lt;br&gt;Distances:�0.0nm/4127.8nm&lt;br&gt;&lt;b&gt;Wind:&lt;/b&gt; 174� 12.4 kt (&lt;b&gt;TWA 52�&lt;/b&gt;)&lt;br&gt;&lt;b&gt;Heading:&lt;/b&gt; 122� &lt;b&gt;Sail:&lt;/b&gt; Jib&lt;br&gt;&lt;b&gt;Boat Speed:&lt;/b&gt; 16.33 kts&#39;,&#39;220px&#39;)\" onmouseout=\"cleari()\" onmousedown=\"show_wind(0);\">";
		
		//action
		int heading = serviceRouting.parseHeading(line);
		
		// assert
		Assert.assertEquals(122, heading);
	}

	@Test
	public void parseSail() {
		
		//setup
		String line = " <img src=\"./Sailing Simulator  Austral Pursuit_files/dot.png\" class=\"abs\" style=\"z-index: 1; left :22353px; top:16873px;\" onmouseover=\"updi(event,&#39;2013-02-24 08:10 UTC (T+ 0:00)&lt;br&gt;Distances:�0.0nm/4127.8nm&lt;br&gt;&lt;b&gt;Wind:&lt;/b&gt; 174� 12.4 kt (&lt;b&gt;TWA 52�&lt;/b&gt;)&lt;br&gt;&lt;b&gt;Heading:&lt;/b&gt; 122� &lt;b&gt;Sail:&lt;/b&gt; Jib&lt;br&gt;&lt;b&gt;Boat Speed:&lt;/b&gt; 16.33 kts&#39;,&#39;220px&#39;)\" onmouseout=\"cleari()\" onmousedown=\"show_wind(0);\">";
		
		//action
		SailEnum sail = serviceRouting.parseSail(line);
		
		// assert
		Assert.assertEquals(SailEnum.JIB, sail);
	}
	
	
}
