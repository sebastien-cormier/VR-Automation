package fr.cormier.vra.service.external;

import fr.cormier.domain.external.SmsMessage;
import fr.cormier.utils.SmsUtils;

public class SmsService {
	
	private boolean fakeMode = true;
	
	public SmsService(boolean fakeMode) {
		this.fakeMode = fakeMode;
	}

	public void sensSms(SmsMessage smsMessage) {
		if(fakeMode) {
			System.out.println("Sending SMS to "+smsMessage.getRecipient());
			System.out.println(" > "+smsMessage.getMessage());
		} else {
			SmsUtils.send("", "", "", "", "");
		}
	}

    

}
