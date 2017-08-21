/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月24日
 * 
 */
package com.eliteams.quick4j.web.cache;

import java.util.HashMap;
import java.util.Map;

import com.eliteams.quick4j.web.model.UrlConfig;

/**
 * @author Default
 *
 */
public class UrlsCache {

	private static Map<String, UrlConfig> dateUrls = new HashMap<>();
	private static Map<String, UrlConfig> timeUrls = new HashMap<>();
	
	public static void update(UrlConfig url, String token) {
		url.setUrl(url.getUrl() + "&token=" + token);
		if("date".equals(url.getRate())) {
			dateUrls.put(url.getCode(), url);
		} else if("time".equals(url.getRate())) {
			timeUrls.put(url.getCode(), url);
		} else {
			throw new RuntimeException("Unknow rate request url: " 
						+ url.getCode() + " -> " + url.getRate());
		}
	}
	
	public static Map<String, UrlConfig> getRealtimeUrls() {
		return timeUrls;
	}
	
	public static Map<String, UrlConfig> getDateUrls() {
		return dateUrls;
	}
}
