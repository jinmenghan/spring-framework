package com.ruyuan.demo;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class BeanFactoryDemo {

	public static void main(String[] args) {

		XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
		Student student = (Student) beanFactory.getBean("student");
		System.out.println(student.getName());
	}
}
