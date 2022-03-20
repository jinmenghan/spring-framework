package com.ruyuan.container;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 自定义 postProcessBeanFactory
 */
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {


	public MyClassPathXmlApplicationContext(ApplicationContext parent) {
		super(parent);
	}

	@Override
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		System.out.println("重写applicationContext的postProcessBeanFactory方法...");

		// 获取容器中，名称为student的BeanDefinition
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("student");
		// 修改beanDefinition中的信息
		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);

	}
}
