/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月27日
 * 
 */
package com.eliteams.quick4j.web.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliteams.quick4j.web.cache.TodaySscCache;
import com.eliteams.quick4j.web.http.ISscRequest;
import com.eliteams.quick4j.web.job.JobMannger;
import com.eliteams.quick4j.web.job.impl.HistoryRecordSeeker;
import com.eliteams.quick4j.web.security.RoleSign;
import com.eliteams.quick4j.web.service.ISscService;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("ssc")
public class SscController {

	private static Logger logger = Logger.getLogger(SscController.class);
	@Autowired
	private ISscService sscService;
	
	@Autowired
	private ISscRequest sscRequest;
	
	public void page() {
		
	}
	
	@RequestMapping("/updateDaily")
	@ResponseBody
	public int updateDaily() {
		return sscService.updateDaily();
	}
	
	@RequestMapping("/updateHistory")
	@ResponseBody
	@RequiresRoles(value = RoleSign.ADMIN)
	public boolean updateHistory(@RequestParam Map<String, Object> param) {
		String current = TodaySscCache.getCurrentDate();
		String startDate  = (String) param.getOrDefault("startDate", current);
		String endDate = (String) param.getOrDefault("endDate", current);
		String code = (String) param.getOrDefault("code", "");
		String force = (String) param.getOrDefault("force", "");
		
		HistoryRecordSeeker seeker = new HistoryRecordSeeker(code, startDate, endDate);
		seeker.setSscRequest(sscRequest);
		//seeker.stop();
		boolean result = JobMannger.startSingleJob("history", seeker);
		if(!result && "true".equals(force)) result = JobMannger.forceStartJob("history", seeker);
		logger.info(String.format("Start history seeker [%s %s %s] : %s", startDate, endDate, code, result));
		return result;
	}
}
