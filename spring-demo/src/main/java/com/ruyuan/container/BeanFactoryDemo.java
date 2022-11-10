package com.ruyuan.container;

import com.ruyuan.container.listener.MyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryDemo {

	public static void main(String[] args) {

//		XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
//		Student student = (Student) beanFactory.getBean("student");
//		System.out.println(student.getName());

//		XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
//		BeanNameAwareImpl beanNameAwareImpl = (BeanNameAwareImpl) beanFactory.getBean("beanNameAwareImpl");
//		System.out.println(beanNameAwareImpl.getBeanName());



//		ApplicationContext beanFactory = new MyClassPathXmlApplicationContext("applicationContext.xml");
//		Student student = (Student) beanFactory.getBean("student");
//		System.out.println(student.getName());

		ApplicationContext beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");
		Student student = (Student) beanFactory.getBean("student");
		System.out.println(student.getName());


//		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//		ctx.publishEvent(new MyEvent("myEvent"));

	}
}
