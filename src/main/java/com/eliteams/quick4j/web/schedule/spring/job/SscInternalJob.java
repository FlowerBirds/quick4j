/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.schedule.spring.job;

import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eliteams.quick4j.web.cache.TodaySscCache;
import com.eliteams.quick4j.web.http.ISscRequest;

/**
 * @author Default
 *
 */
@Component("sscInternalJob")
public class SscInternalJob {

	private static Logger logger = Logger.getLogger(SscInternalJob.class);
	
	@Autowired
	private ISscRequest ssc;
	/**
	 * 每天10点到21:50点，每10分钟调用一次
	 * 每天22点到22:55，每5分钟调用一次
	 * 每天0点到1:55，每5分钟调用一次
	 */
	@Scheduled(cron = " 50 0/10 10-21 * * ?")
	@Scheduled(cron = " 50 0/5 22-23 * * ?")
	@Scheduled(cron = " 50 0/5 0-2 * * ?")
	public void internalUpdate() {
		LocalDate today = LocalDate.now();
		LocalTime time = LocalTime.now();
		logger.info("现在是10分钟执行一次更新操作:" + today + " " + time);
		ssc.requestLatest();
		
	}
	
	/**
	 * 每天凌晨执行一次，定点清除当天缓存数据
	 */
	@Scheduled(cron = " 50 0 0 * * ?")
	public void clearTodayAward() {
		TodaySscCache.clear();
	}
}
