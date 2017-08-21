/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eliteams.quick4j.web.cache.TodaySscCache;
import com.eliteams.quick4j.web.cache.UrlsCache;
import com.eliteams.quick4j.web.dao.SscMapper;
import com.eliteams.quick4j.web.dao.UrlMapper;
import com.eliteams.quick4j.web.http.ISscRequest;
import com.eliteams.quick4j.web.model.SscEntity;
import com.eliteams.quick4j.web.model.UrlConfig;
import com.eliteams.quick4j.web.util.RSAUtil;

/**
 * @author Default
 *
 */
@Component
public class LoadConfig {

	private Logger logger = Logger.getLogger(LoadConfig.class);
	
	@Value("${url.token}")
	private String token;
	
	@Value("${ssc.types}")
	private String sscTypes;
	
	@Value("${pri.file}")
	private String priKeyFile;
	
	@Value("${pub.file}")
	private String pubKeyFile;
	
	@Resource
	private UrlMapper urlMapper;
	
	@Resource
	private SscMapper sscMapper;
	
	@Autowired
	private ISscRequest isscRequest;
	
	@PostConstruct
	public void init() {
		TodaySscCache.clear();
		String current = TodaySscCache.getCurrentDate();
		logger.info("Load current date: " + current);
		
		//加载请求url
		List<UrlConfig> urls = urlMapper.select();
		urls.stream().forEach(url -> UrlsCache.update(url, token));
		logger.info("Load urls: " + urls.size());
		
		//加载当天的数据
		String[] types = sscTypes.split(",");
		SscEntity se = new SscEntity();
		se.setDate(current);
		for(String code : types) {
			se.setCode(code);
			List<SscEntity> list = sscMapper.selectSsc(se);
			if(list.size() > 0) {
				list.stream().forEach(entity -> TodaySscCache.put(entity));
			}
			logger.info(String.format("Load [%s] data: %d", code, list.size()));
		}
		logger.info("Load Data finished for " + sscTypes);
		
		//从服务器更新当天的数据，和对比
		isscRequest.requestDaily();
		logger.info("Update current date completed : " + current);
		
		RSAUtil.load(priKeyFile, pubKeyFile);
	}
}
