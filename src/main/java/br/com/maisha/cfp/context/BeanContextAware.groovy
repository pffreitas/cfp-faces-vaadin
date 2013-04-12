package br.com.maisha.cfp.context

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

final class BeanContextAware implements ApplicationContextAware{

	private static BeanContextAware instance

	private ApplicationContext appCtx;

	def static ApplicationContext get(){
		return instance.appCtx;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
	throws BeansException {
		appCtx = applicationContext;
		instance = this;
	}
}
