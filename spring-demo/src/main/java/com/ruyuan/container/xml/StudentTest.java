package com.ruyuan.container.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class StudentTest {

	public static void main(String[] args) throws Exception{

		String xmlPath = "D:\\own\\study\\spring-framework\\spring-demo\\src\\main\\resources\\student.xml";
		List<Student> students = getStudents(xmlPath);
		System.out.println(students);

	}


	public static Document getDocument(String xmlPath) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		return builder.parse(xmlPath);
	}


	public static List<Student> getStudents(String xmlPath) throws Exception{


		// 1、获取student.xml对应的Document
		Document document = getDocument(xmlPath);

		// 2、获取xml中所有的节点
		List<Student> students = new ArrayList<>();
		NodeList studentNodes = document.getElementsByTagName("student");
		for(int i = 0 ; i < studentNodes.getLength(); i++){
			// 3、遍历每个student 几点
			Element studentEle = (Element) studentNodes.item(i);

			Student student = new Student();
			student.setName(studentEle.getElementsByTagName("name").item(0).getTextContent());
			student.setAge(Integer.parseInt(studentEle.getElementsByTagName("age").item(0).getTextContent()));

			students.add(student);
		}

		// 4、返回students
		return students;

	}
}
