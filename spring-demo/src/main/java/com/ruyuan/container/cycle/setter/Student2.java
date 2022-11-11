package com.ruyuan.container.cycle.setter;

public class Student2 {

	public Student2(Student1 student1) {
		this.student1 = student1;
	}

	private Student1 student1;

	public Student1 getStudent1() {
		return student1;
	}

	public void setStudent1(Student1 student1) {
		this.student1 = student1;
	}

	@Override
	public String toString() {
		return "Student2{" +
				"student1=" + student1 +
				'}';
	}
}
