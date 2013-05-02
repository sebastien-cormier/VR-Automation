package fr.cormier.domain.xml.getuser;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class User {

	@Attribute
	private int id_user;
	
	@Attribute
	private String date;
	
	@Attribute
	private String dateGMT;

	@Attribute
	private String clientVersion;
	
	@Attribute
	private String windDate;

	@Attribute
	private String isCustomer;

	@Element
	private Position position;
	
	@Element
	private int nb_creditsMP;
	
	@Element(required=false)
	private int canFashion;
	
	@ElementList
	private List<Boat> amis;
	
	@ElementList(required=false, data=false)
	private List<Ami> nouveaux_amis;

	@Element(required=false, data=false)
	private String waypoints;
	
	@Element(required=false, data=false)
	private String messages;

	@Element(required=false, data=false)
	private String message;
	
	@Element
	private String opponents_file;
	
	@Element(required=false)
	private String sender;
	
	@Element(required=false)
	private String text;
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Ami> getNouveaux_amis() {
		return nouveaux_amis;
	}

	public void setNouveaux_amis(List<Ami> nouveaux_amis) {
		this.nouveaux_amis = nouveaux_amis;
	}

	public String getOpponents_file() {
		return opponents_file;
	}

	public void setOpponents_file(String opponents_file) {
		this.opponents_file = opponents_file;
	}

	public String getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(String waypoints) {
		this.waypoints = waypoints;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public List<Boat> getAmis() {
		return amis;
	}

	public void setAmis(List<Boat> amis) {
		this.amis = amis;
	}

	public int getCanFashion() {
		return canFashion;
	}

	public void setCanFashion(int canFashion) {
		this.canFashion = canFashion;
	}

	public int getNb_creditsMP() {
		return nb_creditsMP;
	}

	public void setNb_creditsMP(int nb_creditsMP) {
		this.nb_creditsMP = nb_creditsMP;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateGMT() {
		return dateGMT;
	}

	public void setDateGMT(String dateGMT) {
		this.dateGMT = dateGMT;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getWindDate() {
		return windDate;
	}

	public void setWindDate(String windDate) {
		this.windDate = windDate;
	}

	public String getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}
	
	
}
