package com.ruyuan.container;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {


		System.out.println("覆盖BeanFactoryPostProcessor的postProcessBeanFactory方法..");

		// 获取容器中，名称为student的BeanDefinition
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("student");
		// 修改beanDefinition中的信息
		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
	}
}
