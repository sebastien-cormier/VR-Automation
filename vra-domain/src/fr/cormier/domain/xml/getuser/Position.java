package fr.cormier.domain.xml.getuser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Position {

	@Attribute
	private String date;
	
	@Element
	private String latitude;
	
	@Element
	private String longitude;
	
	@Element
	private int voile;
	
	@Element
	private int cap;
	
	@Element
	private float distancerestante;
	
	@Element
	private float distanceparcourue;
	
	@Element
	private int classement;
	
	@Element
	private int checkpoint;
	
	@Element(required=false)
	private MessagesSystem MessagesSystem;
	
	@Element
	private int diffClassement;
	
	@Element(required=false)
	private String IsArrived;

	@Element(required=false)
	private String temps_etape;
	
	public String getIsArrived() {
		return IsArrived;
	}

	public String getTemps_etape() {
		return temps_etape;
	}

	public void setTemps_etape(String temps_etape) {
		this.temps_etape = temps_etape;
	}

	public void setIsArrived(String isArrived) {
		this.IsArrived = isArrived;
	}

	public MessagesSystem getMessagesSystem() {
		return MessagesSystem;
	}

	public void setMessagesSystem(MessagesSystem messagesSystem) {
		this.MessagesSystem = messagesSystem;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int getVoile() {
		return voile;
	}

	public void setVoile(int voile) {
		this.voile = voile;
	}

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public float getDistancerestante() {
		return distancerestante;
	}

	public void setDistancerestante(float distancerestante) {
		this.distancerestante = distancerestante;
	}

	public float getDistanceparcourue() {
		return distanceparcourue;
	}

	public void setDistanceparcourue(float distanceparcourue) {
		this.distanceparcourue = distanceparcourue;
	}

	public int getClassement() {
		return classement;
	}

	public void setClassement(int classement) {
		this.classement = classement;
	}

	public int getCheckpoint() {
		return checkpoint;
	}

	public void setCheckpoint(int checkpoint) {
		this.checkpoint = checkpoint;
	}

	public int getDiffClassement() {
		return diffClassement;
	}

	public void setDiffClassement(int diffClassement) {
		this.diffClassement = diffClassement;
	}
	
	
}