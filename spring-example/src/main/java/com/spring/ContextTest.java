package com.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * context测试
 * @author jinpeng
 */
public class ContextTest {


	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

		System.out.println(context.getBean(Config.class));
	}

}
