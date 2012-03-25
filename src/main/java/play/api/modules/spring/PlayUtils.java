package play.api.modules.spring;

import play.api.Play;

class PlayUtils {
	
	public static ClassLoader classloader() {
		return Play.current().classloader();
	}
	

	
}
