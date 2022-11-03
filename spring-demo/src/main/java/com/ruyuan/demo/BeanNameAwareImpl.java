package com.ruyuan.demo;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class BeanNameAwareImpl implements BeanNameAware {

	private String beanName;

	public String getBeanName() {
		return beanName;
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}
