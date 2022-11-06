package com.ruyuan.demo;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class BeanFactoryDemo {

	public static void main(String[] args) {

//		XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
//		Student student = (Student) beanFactory.getBean("student");
//		System.out.println(student.getName());

//		XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
//		BeanNameAwareImpl beanNameAwareImpl = (BeanNameAwareImpl) beanFactory.getBean("beanNameAwareImpl");
//		System.out.println(beanNameAwareImpl.getBeanName());



		ApplicationContext beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
		Student student = (Student) beanFactory.getBean("student");
		System.out.println(student.getName());

	}
}
