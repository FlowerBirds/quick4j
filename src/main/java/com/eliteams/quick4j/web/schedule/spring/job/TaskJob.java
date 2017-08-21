package com.eliteams.quick4j.web.schedule.spring.job;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("taskJob")
public class TaskJob {

	private static Logger logger = Logger.getLogger(SscInternalJob.class);
	
	
    @Scheduled(cron = " 0 0/1 * * * ?")
    public void myMethod() {
    	//LocalDate today = LocalDate.now();
		//LocalTime time = LocalTime.now();
		logger.info("现在是1分钟执行一次更新操作");
		
    }
}
