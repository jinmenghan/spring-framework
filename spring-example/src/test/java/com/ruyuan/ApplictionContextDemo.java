package com.ruyuan;

import com.ruyuan.container.Student;
import com.ruyuan.container.Student3;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * xml加载demo
 * @author jinpeng
 */
@SuppressWarnings("all")
public class ApplictionContextDemo {


	public static void main(String[] args) {
		ApplicationContext xmlBeanFactory = new ClassPathXmlApplicationContext(("applicationContext.xml"));

		Student student = (Student) xmlBeanFactory.getBean("student");


		System.out.println(student.getName());
	}

}
