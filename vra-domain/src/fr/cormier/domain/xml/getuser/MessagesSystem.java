package fr.cormier.domain.xml.getuser;

import org.simpleframework.xml.Element;

public class MessagesSystem {

	@Element(required=false)
	private Message_course message_course;

	@Element(required=false)
	private String event;
	
	@Element(required=false)
	private String iconUrl;
	
	@Element(required=false)
	private String titre;
	
	@Element(required=false)
	private String texte_short;
	
	@Element(required=false)
	private String texte_long;
	
	@Element(required=false)
	private String param1;
	
	@Element(required=false)
	private String param2;
	
	
	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getTexte_long() {
		return texte_long;
	}

	public void setTexte_long(String texte_long) {
		this.texte_long = texte_long;
	}

	public String getTexte_short() {
		return texte_short;
	}

	public void setTexte_short(String texte_short) {
		this.texte_short = texte_short;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Message_course getMessage_course() {
		return message_course;
	}

	public void setMessage_course(Message_course message_course) {
		this.message_course = message_course;
	}
	
	

}
