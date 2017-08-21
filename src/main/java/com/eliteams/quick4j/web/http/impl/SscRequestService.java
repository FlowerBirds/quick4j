/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.http.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.eliteams.quick4j.core.http.HttpManager;
import com.eliteams.quick4j.web.cache.TodaySscCache;
import com.eliteams.quick4j.web.cache.UrlsCache;
import com.eliteams.quick4j.web.dao.SscMapper;
import com.eliteams.quick4j.web.http.ISscRequest;
import com.eliteams.quick4j.web.model.Record;
import com.eliteams.quick4j.web.model.SscEntity;
import com.eliteams.quick4j.web.model.UrlConfig;

/**
 * @author Default
 *
 */
@Service
public class SscRequestService implements ISscRequest{

	private static Logger logger = Logger.getLogger(SscRequestService.class);
	
	@Resource
    private SscMapper sscMapper;
	
	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.http.ISscRequest#requestLatest()
	 */
	@Override
	public boolean requestLatest() {
		// Auto-generated method stub
		Map<String, UrlConfig> urls = UrlsCache.getRealtimeUrls();
		List<Record> records = urls.entrySet()
				.stream()
				.map(m ->  new Record(requestUpdate(m.getKey(), m.getValue()), m.getValue()))
				.filter(r -> r.getRows() == 0)
				.collect(Collectors.toList());
		int times = 0;
		while(records.size() > 0 && times < 10) {
			//沉睡10秒之后再次请求
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LocalDateTime today = LocalDateTime.now();
			logger.info("Try to update data after 20 seconds: " + times + " times at " + today);
			records = records.stream()
							.map(r -> new Record(requestUpdate(r.getConfig().getCode(), r.getConfig()), r.getConfig()))
							.filter(r -> r.getRows() == 0)
							.collect(Collectors.toList());
			times++;
		}
		return true;
	}

	/**
	 * @param code
	 * @param config
	 * @return
	 */
	private int requestUpdate(String code, UrlConfig config) {
		List<SscEntity> result;
		int num = 0;
		try {
			result = HttpManager.get(config);
			List<SscEntity> es = result
					.stream()
					.filter(e -> updateCache(e))
					.collect(Collectors.toList());
			if(es.size() > 0) {
				num = sscMapper.insertList(es);
				logger.info("update record: " + num + " of " + code);
			} else {
				logger.info("Empty data to update "+ " of " + code);
			}
		} catch (IOException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}
		return num;
	} 

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.http.ISscRequest#selectSsc(com.eliteams.quick4j.web.model.SscResult)
	 */
	@Override
	public List<SscEntity> selectSsc(SscEntity param) {
		// Auto-generated method stub
		return sscMapper.selectSsc(param);
	}

	/**
	 * 更新到缓存中
	 * @param entity
	 * @return
	 */
	public boolean updateCache(SscEntity entity) {
		if(TodaySscCache.contains(entity)) {
			return false;
		} else {
			return TodaySscCache.put(entity);
		}
	}

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.http.ISscRequest#requestDate(java.lang.String)
	 */
	@Override
	public List<Record> requestDaily() {
		Map<String, UrlConfig> urls = UrlsCache.getDateUrls();
		List<Record> records = urls.entrySet()
				.stream()
				.map(e -> new Record(requestUpdate(e.getKey(), e.getValue()), e.getValue()))
				.collect(Collectors.toList());
		return records;
	}

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.http.ISscRequest#requestDaily(com.eliteams.quick4j.web.model.UrlConfig)
	 */
	@Override
	public List<SscEntity> requestDaily(UrlConfig config) {
		// Auto-generated method stub
		List<SscEntity> result = new ArrayList<>();
		try {
			result = HttpManager.get(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.http.ISscRequest#requestDate(java.lang.String, java.lang.String)
	 */
	@Override
	public Record requestDate(String date, String code) {
		//Auto-generated method stub
		UrlConfig config = UrlsCache.getDateUrls().get(code);
		return requestDate(date, config);
	}

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.http.ISscRequest#requestDate(java.lang.String, com.eliteams.quick4j.web.model.UrlConfig)
	 */
	@Override
	public Record requestDate(String date, UrlConfig config) {
		// Auto-generated method stub
		if(config == null) {
			return new Record(0, null);
		} else {
			UrlConfig copy = config.clone();
			copy.setUrl(copy.getUrl() + "&date=" + date);
			return new Record(requestDateUpdate(date, copy), copy);
		}
	}
	
	/**
	 * 根据日期来更新数据
	 * @param date
	 * @param config
	 * @return
	 */
	private int requestDateUpdate(String date, UrlConfig config) {
		int num = 0;
		try {
			List<SscEntity> result = HttpManager.get(config);
			SscEntity param = new SscEntity();
			param.setCode(config.getCode());
			param.setDate(date);
			List<SscEntity> ssclist = sscMapper.selectSsc(param);
			Map<String, String> map = ssclist.stream()
					.collect(Collectors.toMap(SscEntity::getExpect, SscEntity::getOpencode, (s, a) -> s));
			List<SscEntity> es = result.stream().filter(e -> !identify(e, map)).collect(Collectors.toList());
			if(es.size() > 0) {
				num = sscMapper.insertList(es);
				logger.info("update record: " + num + ", for date: " + date);
			} else {
				logger.info("Empty data to update for date: " + date);
			}
		} catch (IOException e1) {
			//Auto-generated catch block
			e1.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 判断当前数据和数据库已存在的是否一致
	 * @param entity
	 * @param map
	 * @return
	 */
	private boolean identify(SscEntity entity, Map<String, String> map) {
		String opencode = map.getOrDefault(entity.getExpect(), "");
		return opencode.equals(entity.getOpencode());
	}
}
