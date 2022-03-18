package com.ruyuan;

import com.ruyuan.container.BeanNameAwareImpl;
import com.ruyuan.container.Student3;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * xml加载demo
 * @author jinpeng
 */
@SuppressWarnings("all")
public class ConstructorDemo {


	public static void main(String[] args) {
		XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(
				new ClassPathResource("constructorContext.xml"));

		Student3 student3 = (Student3) xmlBeanFactory.getBean("student");

		System.out.println(student3);

	}

}
