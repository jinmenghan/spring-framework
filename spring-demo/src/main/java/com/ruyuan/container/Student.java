package com.ruyuan.container;

import org.springframework.beans.factory.BeanNameAware;

public class Student implements BeanNameAware {

	private String name = "ruyuan";

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}


	@Override
	public void setBeanName(String name) {
		System.out.println("beanName:" + name);
	}
}
