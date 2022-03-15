package com.ruyuan.container;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class DomTest {

	public static void main(String[] args) throws Exception{


		String xmlPath = "E:\\work\\source\\spring-framework\\spring-example\\src\\main\\resources\\student.xml";
		List<Student> students = getStudents(xmlPath);
		System.out.println(students);

	}


	/**
	 * 解析dom
	 * @param xmlPath 文件
	 * @return
	 * @throws Exception
	 */
	public static Document getDocument(String xmlPath) throws Exception{

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.parse(xmlPath);
	}

	public static List<Student> getStudents(String xmlPath) throws Exception{
		// 1.获取student的Dom对象
		Document document = getDocument(xmlPath);

		// 2.获取xml中所有的student节点
		List<Student> students = new ArrayList<>();
		NodeList studentNodes = document.getElementsByTagName("student");
		for(int i =0; i < studentNodes.getLength(); i++){
			// 遍历每个student节点
			Element studentEle = (Element)studentNodes.item(i);

			Student student = new Student();
			student.setName(studentEle.getElementsByTagName("name").item(0).getTextContent());
			student.setAge(Integer.parseInt(studentEle.getElementsByTagName("age").item(0).getTextContent()));
			students.add(student);
		}
		return students;
	}
}
