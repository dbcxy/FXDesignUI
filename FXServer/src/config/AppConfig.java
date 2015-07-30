package config;

import org.apache.log4j.Logger;

import application.ServerUIController;

public class AppConfig {
	
	private static final Logger logger = Logger.getLogger(AppConfig.class);
	private static AppConfig instance = null;
	
	protected AppConfig() {
		// Exists only to defeat instantiation.
	}
	
	public static AppConfig getInstance() {
	      if(instance == null) {
	         instance = new AppConfig();
	         logger.info("App Config Instantiated");
	      }
	      return instance;
	}
	
	ServerUIController controller;

	public ServerUIController getController() {
		return controller;
	}

	public void setController(ServerUIController controller) {
		this.controller = controller;
	}
	

}
