package fr.cormier.domain.xml.getuser;

public class BoatsPositionWrapper {

	private Result result;

	public BoatsPositionWrapper(Result result) {
		this.result = result;
	}
	
	public int getHeading() {
		if( result!=null && result.getUser()!=null && result.getUser().getPosition()!=null ) {
			return result.getUser().getPosition().getCap();
		} else {
			return -1;
		}
	}

	public int getSail() {
		if( result!=null && result.getUser()!=null && result.getUser().getPosition()!=null ) {
			return result.getUser().getPosition().getVoile();
		} else {
			return -1;
		}
	}

	public double getLatitude() {
		if( result!=null && result.getUser()!=null && result.getUser().getPosition()!=null ) {
			return Double.parseDouble(result.getUser().getPosition().getLatitude());
		} else {
			return -1;
		}
	}

	public double getLongitude() {
		if( result!=null && result.getUser()!=null && result.getUser().getPosition()!=null ) {
			return Double.parseDouble(result.getUser().getPosition().getLongitude());
		} else {
			return -1;
		}
	}


}
