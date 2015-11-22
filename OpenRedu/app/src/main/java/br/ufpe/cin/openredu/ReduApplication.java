package br.ufpe.cin.openredu;

import org.scribe.exceptions.OAuthConnectionException;
import org.scribe.exceptions.OAuthException;

import android.app.Application;
import android.content.Context;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.User;
import br.ufpe.cin.openredu.util.PinCodeHelper;

public class ReduApplication extends Application {

	static private DefaultReduClient reduClientInitialized;
	static private User user;

	static public DefaultReduClient getReduClient(Context context) throws OAuthConnectionException, OAuthException {
		if(reduClientInitialized != null) {
			return reduClientInitialized;
		}
		
		String consumerKey = context.getString(R.string.CONSUMER_KEY);
		String consumerSecretKey = context.getString(R.string.CONSUMER_SECRET_KEY);
		
		DefaultReduClient reduClient = new DefaultReduClient(consumerKey, consumerSecretKey);
			
		String pinCode = PinCodeHelper.get(context);
		if(pinCode != null) {
			reduClient.initClient(pinCode);
			reduClientInitialized = reduClient;
		}
		
		return reduClient;
	}

	static public User getUser(Context context) throws OAuthConnectionException {
		if(user == null) {
			user = getReduClient(context).getMe();
		}
		return user;
	}
	
	static public void clear(Context context) {
		PinCodeHelper.clear(context);
		reduClientInitialized = null;
		user = null;
	}
}
