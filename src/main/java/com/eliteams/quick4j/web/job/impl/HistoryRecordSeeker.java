/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月27日
 * 
 */
package com.eliteams.quick4j.web.job.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.eliteams.quick4j.web.cache.UrlsCache;
import com.eliteams.quick4j.web.http.ISscRequest;
import com.eliteams.quick4j.web.job.ISeekJob;
import com.eliteams.quick4j.web.model.Record;
import com.eliteams.quick4j.web.model.UrlConfig;

/**
 * @author Administrator
 *
 */
public class HistoryRecordSeeker extends ISeekJob {

	private String code;
	private String startDate;
	private String endDate;
	
	private static Logger logger = Logger.getLogger(HistoryRecordSeeker.class);
	
	private ISscRequest sscRequest;
	
	/**
	 * @return the sscRequest
	 */
	public ISscRequest getSscRequest() {
		return sscRequest;
	}

	/**
	 * @param sscRequest the sscRequest to set
	 */
	public void setSscRequest(ISscRequest sscRequest) {
		this.sscRequest = sscRequest;
	}

	/**
	 * @param code
	 * @param startDate
	 * @param endDate
	 */
	public HistoryRecordSeeker(String code, String startDate, String endDate) {
		super();
		this.code = code;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate start = LocalDate.parse(startDate, df);
		LocalDate end = LocalDate.parse(endDate, df);
		while((end.isAfter(start) || end.isEqual(start)) && !isStop) {
			logger.info("Begin history seek: " + end);
			List<String> codes = new ArrayList<>();
			if(StringUtils.isBlank(code)) {
				codes = UrlsCache.getDateUrls().keySet().stream().collect(Collectors.toList());
				logger.info("Seek code is null, will seek all of UrlsCache: " + StringUtils.join(codes));
			} else {
				logger.info("Seek code : " + code);
				codes.add(code);
			}
			final LocalDate currentDate = end;
			try {
				codes.stream().forEach(c -> seek(currentDate, UrlsCache.getDateUrls().getOrDefault(c, null)));
			} catch (Exception e) {
				logger.error("update failed at the date : " + currentDate, e);
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			end = currentDate.minusDays(1);
		}
		logger.info("End history seek: " + start);
		if(isStop) {
			logger.info("History seek has been stopped, task will exit");
		}
		isFinished = true;
	}

	/**
	 * 对特定日期的某种数据进行更新操作
	 * @param date
	 * @param config
	 */
	public void seek(LocalDate date, UrlConfig config) {
		if (config != null && !isStop) {
			String current = date.format(DateTimeFormatter.BASIC_ISO_DATE);
			Record record = sscRequest.requestDate(current, config);
			logger.info(String.format("Seek Data [%10s %s] : %-3d rows", config.getCode(), current, record.getRows()));
		}
		if(isStop) {
			logger.info("History seek has been stopped");
		}
	}
}
