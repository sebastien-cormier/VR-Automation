package fr.cormier.domain.xml.getuser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Boat {

	@Attribute(required=false)
	private int id_user;
	
	@Element(required=false)
	private int full_option;

	@Element(required=false)
	private String longitude;

	@Element(required=false)
	private String latitude;

	@Element(required=false)
	private int voile;

	@Element(required=false)
	private float vitesse;

	@Element(required=false)
	private float wind_speed;

	@Element(required=false)
	private float wind_angle;

	@Element(required=false)
	private int cap;

	@Element(required=false)
	private float distancerestante;

	@Element(required=false)
	private float distanceparcourue;

	@Element(required=false)
	private int classement;
	
	@Element(required=false)
	private String IsArrived;
	
	@Element(required=false)
	private String temps_etape;
	
	public String getTemps_etape() {
		return temps_etape;
	}
	public void setTemps_etape(String temps_etape) {
		this.temps_etape = temps_etape;
	}
	public String getIsArrived() {
		return IsArrived;
	}
	public void setIsArrived(String isArrived) {
		IsArrived = isArrived;
	}
	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public int getFull_option() {
		return full_option;
	}
	public void setFull_option(int full_option) {
		this.full_option = full_option;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public int getVoile() {
		return voile;
	}
	public void setVoile(int voile) {
		this.voile = voile;
	}
	public float getVitesse() {
		return vitesse;
	}
	public void setVitesse(float vitesse) {
		this.vitesse = vitesse;
	}
	public float getWind_speed() {
		return wind_speed;
	}
	public void setWind_speed(float wind_speed) {
		this.wind_speed = wind_speed;
	}
	public float getWind_angle() {
		return wind_angle;
	}
	public void setWind_angle(int wind_angle) {
		this.wind_angle = wind_angle;
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
	
	

}
