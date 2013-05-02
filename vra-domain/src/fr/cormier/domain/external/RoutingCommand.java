package fr.cormier.domain.external;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.Command;
import fr.cormier.utils.EqualsUtils;

public class RoutingCommand {

	private SailEnum sail;
	
	private int heading;

	public SailEnum getSail() {
		return sail;
	}

	public void setSail(SailEnum sail) {
		this.sail = sail;
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}
	
	public boolean equals(Object object) {
		if( this==object ) return true;
		if( !(object instanceof RoutingCommand) ) return false;
		RoutingCommand command = (RoutingCommand)object;
		return 
				EqualsUtils.areEqual(this.heading, command.heading) &&
				EqualsUtils.areEqual(this.sail, command.sail);
	}
	
}
