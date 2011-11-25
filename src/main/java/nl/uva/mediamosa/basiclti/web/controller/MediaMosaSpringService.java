package nl.uva.mediamosa.basiclti.web.controller;

import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.util.ServiceException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.stereotype.Service;

@Service("mediamosaSpringService")
public class MediaMosaSpringService extends PropertiesLoaderSupport {
	
	protected static final Logger logger = Logger.getLogger("MediaMosaSpringService");

	private MediaMosaService service;
	
	@Value("#{mediamosaConfig.mediamosa_hostname}")
	private String hostname;
	
	@Value("#{mediamosaConfig.mediamosa_username}")
	private String username;
	
	@Value("#{mediamosaConfig.mediamosa_password}")
	private String password;
	
	public MediaMosaService getService() {
		
		if (service != null) {
			return service;
		} else {
			logger.debug("Initializing MediaMosaService using hostname [" + hostname + "] and username [" + username + "]");
			try {
	            service = new MediaMosaService(hostname);
	            service.setCredentials(username, password);
	        } catch (ServiceException e) {
	            logger.error("Error connecting to host [" + hostname + "] using username [" + username + "] and password [" + password + "]." + e.getMessage());
	        }
		}
		return service;
	}
	
}