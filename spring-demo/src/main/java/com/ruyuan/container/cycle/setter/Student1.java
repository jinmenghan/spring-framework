package com.ruyuan.container.cycle.setter;

public class Student1 {

	public Student1(Student2 student2) {
		this.student2 = student2;
	}

	private Student2 student2;

	public Student2 getStudent2() {
		return student2;
	}

	public void setStudent2(Student2 student2) {
		this.student2 = student2;
	}

	@Override
	public String toString() {
		return "Student1{" +
				"student2=" + student2 +
				'}';
	}
}
