package play.api.modules.spring;

import java.util.Map;

public class Spring {

	public static Object getBean(String name) {
        if (SpringPlugin.applicationContext == null) {
            throw new SpringException("Application Context not started"); 
        }
        return SpringPlugin.applicationContext.getBean(name);
    }
	
	public static <T> T getBean(String name, Class<T> type) {
        if (SpringPlugin.applicationContext == null) {
            throw new SpringException("Application Context not started"); 
        }
        return (T) SpringPlugin.applicationContext.getBean(name);
    }
    
    
    public static <T> T getBeanOfType(Class<T> type) {
        Map<String, Object> beans = getBeansOfType(type);
        if(beans.isEmpty()) {
            return null;
        }
        return (T)beans.values().iterator().next();
    }
    
    public static Object getBeanOfType(String type) {
        try {
            return getBeanOfType(PlayUtils.classloader().loadClass(type));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static <T> Map<String, T> getBeansOfType(Class type) {
        if (SpringPlugin.applicationContext == null) {
            throw new SpringException("Application Context not started");
        }
        return SpringPlugin.applicationContext.getBeansOfType(type);
    }
    
}
