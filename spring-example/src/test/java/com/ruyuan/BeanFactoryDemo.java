package com.ruyuan;

import com.ruyuan.container.BeanNameAwareImpl;
import com.ruyuan.container.Student2;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * xml加载demo
 * @author jinpeng
 */
@SuppressWarnings("all")
public class BeanFactoryDemo {

	/*
	可以看到，首先我们通过ClassPathResource封装了applicationContext配置文件，
	然后讲ClassPathResource作为XmlBeanFactory的构造方法的参数创建XmlBeanFactory，
	XmlBeanFactory可以理解为就是Spring的容器，用来存放Bean的地方。

	接着我们从XmlBeanFactory容器中，获取名字为student的bean，运行一下就可以在控制台打印字符串“ruyuan”，
	Spring容器XmlBeanFactory最基础的使用就是这样子的
	 */
	public static void main(String[] args) {
		XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(
				new ClassPathResource("applicationContext.xml"));
//		Student student = (Student) xmlBeanFactory.getBean("student");
//		Student2 student = (Student2) xmlBeanFactory.getBean("student");

//		System.out.println(student.getName());

		BeanNameAwareImpl beanNameAwareImpl = (BeanNameAwareImpl) xmlBeanFactory.getBean("beanNameAwareImpl");

		System.out.println(beanNameAwareImpl.getBeanName());

	}
	/*
	Application在XmlBeanFactory的基础上，添加了非常多的扩展功能和特性，所以，为了方便我们分析spring的核心圆满，
	我们当然要从更基础、更简单的容器XmlBeanFactory开始研究，当我们了解了XmlBeanFactory的一些核心机制之后，后面过度到
	ApplicationContext就比较简单了。
	 */

	/*
	总结：
	第一，实现个简单的demo，体验一下spring基础容器XmlBeanFactory的使用
	第二，对比XmlBeanFactory和ApplicationContext，初步了解XmlBeanFactory是Spring中比较基础的容器，而且
		  ApplicationContext在XmlBeanFactory的基础上又扩展了非常多的功能，总算是一个高级容器了
    第三，最后基于demo中的代码分析了一下XmlBeanFactory的工作原理，包括加载xml文件、解析bean标签生成相对应的bean，
         并将bean注入到spring容器中，最后通过getBean方法从容器中获取bean来使用。
	 */
}
