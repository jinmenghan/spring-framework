package com.ruyuan.container.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class StudentFactoryBean implements FactoryBean {
	@Override
	public Object getObject() throws Exception {
		System.out.println("通过FactoryBean创建Student，开始。。。。");
		Student student = new Student();
		student.setName("tom");
		student.setAge(17);
		return student;
	}

	@Override
	public Class<?> getObjectType() {
		return Student.class;
	}
}
