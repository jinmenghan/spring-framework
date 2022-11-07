package com.ruyuan.container;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

	public MyClassPathXmlApplicationContext(String location) {
		super(location);
	}




	@Override
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		System.out.println("重写ApplicationContext的postProcessBeanFactory方法...");

		// 1.获取容器中，名称为student的BeanDefinition
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("student");
		// 2. 修改beanDefinition中的信息
		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
	}
}
