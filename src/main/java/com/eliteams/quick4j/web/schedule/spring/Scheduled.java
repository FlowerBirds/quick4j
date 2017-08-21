package com.eliteams.quick4j.web.schedule.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD,java.lang.annotation.ElementType.ANNOTATION_TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
public @interface Scheduled {
	
	/**
	 * 指定cron表达式 
	 * @return
	 */
	public abstract String cron();  

	/**表示从上一个任务完成开始到下一个任务开始的间隔，单位是毫秒
	 * @return
	 */
	public abstract long fixedDelay();  

	/**
	 * 即从上一个任务开始到下一个任务开始的间隔，单位是毫秒
	 * @return
	 */
	public abstract long fixedRate();
}
