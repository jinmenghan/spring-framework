package com.ruyuan.container.factorypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("覆盖BeanFactoryPostProcessor的postProcessBeanFactory方法....");


		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("student");
		beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
	}
}
