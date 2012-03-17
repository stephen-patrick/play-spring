package play.api.modules.spring;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.xml.sax.InputSource;

import play.Application;
import play.Logger;
import play.Play;
import play.Plugin;
import play.api.PlayException;

/**
 * Plugin in for integrating a spring into a play 2.0 application
 * 
 * 
 * @author stephen
 * 
 */
public class SpringPlugin extends Plugin {

	private static final String PLAY_SPRING_COMPONENT_SCAN_FLAG = "play-spring-component-scan";
	private static final String PLAY_SPRING_COMPONENT_SCAN_BASE_PACKAGES = "play-spring-component-scan-base-packages";
	private static final String PLAY_SPRING_ADD_PLAY_PROPERTIES = "play.spring.add-play-properties";
	private static final String PLAY_SPRING_NAMESPACE_AWARE = "play.spring.namespace-aware";

	public static GenericApplicationContext applicationContext;
	private Application application;
	
	
	public SpringPlugin(Application application) {
		this.application = application;
	}
	
	
	
	@Override
	public void onStart() {
		if(application.configuration().getString("play.spring-enabled")==null || !application.configuration().getString("play.spring-enabled").equals("true")) {
			return;
		}
		
		
		
		
		URL url = getCurrentPlayApplication().classloader()
				.getResource(".application-context.xml");
		if (url == null) {
			String confDirectory = getCurrentPlayApplication().path() + "/conf";
			try {
				url = new File(confDirectory, "application-context.xml").toURI()
						.toURL();
			}
			catch(Exception e) {
				throw new RuntimeException("Unable to create spring context " , e);
			}
			
		}

		if (url == null) {
			// TODO CAN I PASSS NULL HERE
			throw new PlayException(
					"No application-context.xml!",
					"Please include an application-context.xml file in your classpath",
					null);
		}

		InputStream is = null;
		try {
			Logger.debug("Starting Spring application context");
			createApplicationContext();
			XmlBeanDefinitionReader xmlReader = createXmlReader();
			ifRequiredAddPlayPropertiesToSpringContext();
			doComponentScanIfRequired(applicationContext, xmlReader);
			is = url.openStream();
			xmlReader.loadBeanDefinitions(new InputSource(is));
			ClassLoader originalClassLoader = Thread.currentThread()
					.getContextClassLoader();
			Thread.currentThread().setContextClassLoader(
					PlayUtils.classloader());
			try {
				applicationContext.refresh();
			} catch (BeanCreationException e) {
				Throwable ex = e.getCause();
				if (ex instanceof PlayException) {
					throw (PlayException) ex;
				} else {
					throw e;
				}
			} finally {
				Thread.currentThread().setContextClassLoader(
						originalClassLoader);
			}
		} catch (IOException e) {
			Logger.error("Can't load spring config file", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Logger.error("Can't close spring config file stream", e);
				}
			}
		}

	}

	private void doComponentScanIfRequired(ApplicationContext context,
			XmlBeanDefinitionReader reader) {
		doComponentScanSpringIfRequired(context, reader);

	}

	private void doComponentScanSpringIfRequired(ApplicationContext context,
			XmlBeanDefinitionReader reader) {
		if (ConfigurationUtils.isTrue(PLAY_SPRING_COMPONENT_SCAN_FLAG)) {

			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(
					reader.getBeanFactory());
			String packagesToScan = ConfigurationUtils
					.getString(PLAY_SPRING_COMPONENT_SCAN_BASE_PACKAGES);
			Logger.debug("Base package for scan: " + packagesToScan);
			Logger.debug("Scanning...");
			scanner.scan(packagesToScan.split(","));
			Logger.debug("... component scanning complete");

		}

	}

	

	private void ifRequiredAddPlayPropertiesToSpringContext() {
		if (ConfigurationUtils.isTrue(PLAY_SPRING_ADD_PLAY_PROPERTIES)) {
			Logger.debug("Adding PropertyPlaceholderConfigurer with Play properties");
			PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
			configurer.setProperties(ConfigurationUtils
					.createPropertiesFromPlayConfiguration());
			applicationContext.addBeanFactoryPostProcessor(configurer);
		} else {
			Logger.debug("PropertyPlaceholderConfigurer with Play properties NOT added");
		}
	}

	private XmlBeanDefinitionReader createXmlReader() {
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(
				applicationContext);

		if (ConfigurationUtils.isTrue(PLAY_SPRING_NAMESPACE_AWARE)) {
			xmlReader.setNamespaceAware(true);
		}

		xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		return xmlReader;
	}

	/**
	 * Create the Spring Application Context which manages the spring beans
	 */
	private void createApplicationContext() {
		applicationContext = new GenericApplicationContext();
		applicationContext.setClassLoader(getCurrentPlayApplication().classloader());
	}

	@Override
	public void onStop() {
		if(application.configuration().getString("play.spring-enabled")==null || !application.configuration().getString("play.spring-enabled").equals("true")) {
			return;
		}
		
		Logger.debug("Spring plugin stopping");
		if (applicationContext != null) {
	         Logger.debug("Closing Spring application context");
	         applicationContext.close();
	    }
		
		Logger.debug("Spring plugin stopped");
	}

	@Override
	public boolean enabled() {
		return true;
	}
	
	

	protected Application getCurrentPlayApplication() {
		return Play.application();
	}
}
