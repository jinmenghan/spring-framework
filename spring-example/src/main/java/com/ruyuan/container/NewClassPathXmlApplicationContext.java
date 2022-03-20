package com.ruyuan.container;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NewClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

	public NewClassPathXmlApplicationContext(ApplicationContext parent) {
		super(parent);
	}

	/*@Override
	protected void initPropertySources() {
		System.out.println("重写initPropertySource方法...");

		getEnvironment().getSystemProperties().put("username","zhangsan");

//	我们可以在系统中添加属性username，值为张三，这样的话，如果xml文件中存在占位符 "${username}",
//	spring就可以拿着这个属性的值取替换占位符了，简单磊说方法initPropertySource存在的意义也就这样

	}*/


	@Override
	protected void initPropertySources() {
		System.out.println("重写initPropertySource方法...");

		getEnvironment().setRequiredProperties("JAVA_HOME");
		/*
			我们看到还是刚才的方法，我们在initPropertySource中，通过方法setRequiredProperties置顶必须设置变量名称为“JAVA_HOME”
			也就是说环境变量中的值，一点要存在，否侧 getEnvironment().
			validateRequiredProperties就会取判断名称为"JAVA_HOME"的这个环境比那里是否存在对应的值

			如果存在新颖的环境变量，就会抛出异常，很显然我们通过这个功能特性，
			就可以在spring容器初始化的时候，及时检测出那些必要的环境变量没有配置好，及时发现并排除隐患
		 */
	}
}
