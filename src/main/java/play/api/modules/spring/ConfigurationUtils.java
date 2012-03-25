package play.api.modules.spring;

import java.util.Properties;
import java.util.Set;

import play.Play;



/**
 * Helper for accessing the play configuration
 * @author Stephen Enright
 *
 */
class ConfigurationUtils {
	
	public static boolean isTrue(String key) {
		String configValue = Play.application().configuration().getString(key);
		
		if(configValue!=null && configValue.equalsIgnoreCase("true")) {
			return true;
		}
	
		return false;
	}
	
	
	public static Properties createPropertiesFromPlayConfiguration() {
		Properties properties = new Properties();
		Set<String> configurationKeys = Play.application().configuration().keys();
       
		for(String key: configurationKeys) {
			properties.put(key, Play.application().configuration().getString(key));
		}
		
		
		return properties;
	}
	

	public static String getString(String key) {
		String configValue = Play.application().configuration().getString(key);
		
		if(configValue==null) {
			return "";
		}
		
		return configValue;
	}
	
	
	
	
	
}
