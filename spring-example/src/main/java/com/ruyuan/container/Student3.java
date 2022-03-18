package com.ruyuan.container;

public class Student3 {


	private String name;
	private Integer age;

	public Student3(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
