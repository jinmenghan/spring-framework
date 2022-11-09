package com.ruyuan.container.beanpostprocessor;

import com.ruyuan.container.Student;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class StudentPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof Student){
			System.out.println("Student 对应的bean开始实例化。。。。");
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof Student){
			System.out.println("Student 对应的bean实例化完成。。。。");
		}
		return bean;
	}
}
