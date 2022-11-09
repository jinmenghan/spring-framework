package com.ruyuan.container;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NewClassPathXmlApplicationContext  extends ClassPathXmlApplicationContext {


	public NewClassPathXmlApplicationContext(String configLocation) throws BeansException {
		super(configLocation);
	}

	public NewClassPathXmlApplicationContext(String... configLocations) throws BeansException {
		super(configLocations);
	}

	@Override
	protected void initPropertySources() {
		System.out.println("重写initPropertySources方法....");
		getEnvironment().getSystemProperties().put("username","zhangSan");
	}

}
