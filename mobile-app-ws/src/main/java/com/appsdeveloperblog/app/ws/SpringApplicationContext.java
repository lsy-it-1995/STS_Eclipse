package com.appsdeveloperblog.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.*;

public class SpringApplicationContext implements ApplicationContextAware{

	private static ApplicationContext Context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.Context = applicationContext;
	}
	
	public static Object getBean(String beanName) {
		return Context.getBean(beanName);
	}
}
