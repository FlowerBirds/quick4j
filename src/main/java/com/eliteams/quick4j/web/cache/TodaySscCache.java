/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.cache;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.eliteams.quick4j.core.util.LimitQueue;
import com.eliteams.quick4j.web.model.SscEntity;

/**
 * @author Default
 *
 */
public class TodaySscCache {

	private static Logger logger = Logger.getLogger(TodaySscCache.class);
	
	private static final int QUEUE_LIMIT = 120;
	
	private static Map<String, LimitQueue<SscEntity>> cache = new ConcurrentHashMap<>();
	private static Set<String> keys = new HashSet<>();
	
	private static String currentDate = null;
	
	/**
	 * 清除前一天的数据
	 */
	public static void clear() {
		
		LocalDate today = LocalDate.now();
		LocalTime time = LocalTime.now(); 
		LocalTime specificTime = LocalTime.of(0,0,1,40); // 00:00:01.40
		String tdate = "";
		if(time.isAfter(specificTime)) {
			tdate = today.format(DateTimeFormatter.BASIC_ISO_DATE);
		} else {
			LocalDate yestoday = today.minusDays(1);
			tdate = yestoday.format(DateTimeFormatter.BASIC_ISO_DATE);
		}
		
		if(tdate != currentDate) {
			currentDate = tdate;
		}
	}
	
	/**
	 * 缓存中是否包含当前彩票数据
	 * @param entity
	 * @return
	 */
	public static boolean contains(SscEntity entity) {
		String key = combineKey(entity);
		return keys.contains(key);
	}
	
	/**
	 * 组合键
	 * @param entity
	 * @return
	 */
	private static String combineKey(SscEntity entity) {
		return entity.getCode() + "-" + entity.getExpect();
	}
	
	/**
	 * 添加开奖结果
	 * @param entity
	 * @return
	 */
	public static boolean put(SscEntity entity) {
		boolean ok = false;
		if(!currentDate.equals(entity.getDate())) {
			logger.warn("This entity is not belong to current: " + entity.getExpect());
			//return false;
		}
		if(!contains(entity)) {
			String key = combineKey(entity);
			LimitQueue<SscEntity> queue = null;
			if(cache.containsKey(entity.getCode())) {
				queue = cache.get(entity.getCode());
			} else {
				queue = new LimitQueue<>(QUEUE_LIMIT);
				cache.put(entity.getCode(), queue);
			}
			if(queue.isFull()) {
				SscEntity e = queue.poll();
				String k = combineKey(e);
				keys.remove(k);
			}
			queue.offer(entity);
			keys.add(key);
			ok = true;
		}
		return ok;
	}
	
	/**
	 * 获取当前所属日期的字符串
	 * @return
	 */
	public static String getCurrentDate() {
		return currentDate;
	}
	
	/**
	 * @param code
	 * @param num
	 * @return
	 */
	public static List<SscEntity> getLatest(String code, int num) {
		LimitQueue<SscEntity> datas = cache.get(code);
		List<SscEntity> rows = new LinkedList<>();
		if(datas != null && datas.size() > 0) {
			rows = datas.latest(num);
		}
		return rows;
	}
	
	public static List<SscEntity> getCurrentDay(String code) {
		LimitQueue<SscEntity> datas = cache.get(code);
		List<SscEntity> rows = new LinkedList<>();
		if(datas != null && datas.size() > 0) {
			rows = datas.stream().filter(e -> e.getDate().equals(currentDate)).collect(Collectors.toList());
		}
		return rows;
	}
	
}
