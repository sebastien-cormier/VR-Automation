package fr.cormier.domain.xml.getuser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Message_course {

	@Attribute(required=false)
	private String temps_event;
	
	@Attribute(required=false)
	private String id;
	
	@Element(required=false, data=false)
	private String titre;
	
	@Element(required=false, data=false)
	private String iconUrl;
	
	@Element(required=false, data=false)
	private String url;
	
	@Element(required=false, data=false)
	private String urlTarget;
	
	@Element(required=false, data=false)
	private String texte_short;
	
	@Element(required=false, data=false)
	private String texte_long;

	public String getTemps_event() {
		return temps_event;
	}

	public void setTemps_event(String temps_event) {
		this.temps_event = temps_event;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTexte_long() {
		return texte_long;
	}

	public void setTexte_long(String texte_long) {
		this.texte_long = texte_long;
	}

	public String getTemp_event() {
		return temps_event;
	}

	public void setTemp_event(String temp_event) {
		this.temps_event = temp_event;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlTarget() {
		return urlTarget;
	}

	public void setUrlTarget(String urlTarget) {
		this.urlTarget = urlTarget;
	}

	public String getTexte_short() {
		return texte_short;
	}

	public void setTexte_short(String texte_short) {
		this.texte_short = texte_short;
	}

	public String getText_long() {
		return texte_long;
	}

	public void setText_long(String text_long) {
		this.texte_long = text_long;
	}
	

}
