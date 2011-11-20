package cat.uvic.android;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author ANNA
 * 
 */
public class Messages {
	private static final String BUNDLE_NAME = "jjil.android.messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	/**
	 * @param key
	 * @return name of Resource
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}