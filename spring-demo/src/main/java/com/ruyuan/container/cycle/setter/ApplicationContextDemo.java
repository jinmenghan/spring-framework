package com.ruyuan.container.cycle.setter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextDemo {

	public static void main(String[] args) {


		ApplicationContext ctx =
				new ClassPathXmlApplicationContext("applicationContext.xml");
		Student1 student1 = (Student1) ctx.getBean("student1");
		System.out.println(student1);
	}
}
