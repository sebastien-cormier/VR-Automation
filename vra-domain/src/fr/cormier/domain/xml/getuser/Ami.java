package fr.cormier.domain.xml.getuser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Ami {

	@Attribute(required=false)
	private int id_user;

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	
	
	

}
