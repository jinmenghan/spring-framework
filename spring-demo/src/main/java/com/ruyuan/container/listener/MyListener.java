package com.ruyuan.container.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class MyListener implements ApplicationListener {


	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof MyEvent) {
			((MyEvent)event).event();
		}
	}
}
