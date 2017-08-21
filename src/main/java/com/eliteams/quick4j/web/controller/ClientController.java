/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月28日
 * 
 */
package com.eliteams.quick4j.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliteams.quick4j.core.util.ApplicationUtils;
import com.eliteams.quick4j.core.util.CookieUtils;
import com.eliteams.quick4j.web.anotation.ClientLogger;
import com.eliteams.quick4j.web.anotation.ClientPermission;
import com.eliteams.quick4j.web.cache.TodaySscCache;
import com.eliteams.quick4j.web.enums.ErrorCode;
import com.eliteams.quick4j.web.model.ClientUser;
import com.eliteams.quick4j.web.model.SscEntity;
import com.eliteams.quick4j.web.model.SscResult;
import com.eliteams.quick4j.web.service.IClientUserService;
import com.eliteams.quick4j.web.service.ISscService;
import com.eliteams.quick4j.web.util.MD5Util;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api")
public class ClientController {

	@Autowired
	private IClientUserService clientUserService;
	
	@Autowired
	private ISscService sscService;
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@ClientLogger
	public Map<String, Object> login(@RequestBody ClientUser client, 
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = new HashMap<>();
		ClientUser user = clientUserService.loginUser(client);
		if(user == null) {
			param.put("result", "faild");
			param.put("errcode", ErrorCode.SSC_001);
		} else {
			param.put("token", "token-" + MD5Util.encode(user.getToken()));
			param.put("result", "success");
			param.put("lastVisit", user.getLastVisit().toString());
			CookieUtils.addCookie(response, "token", user.getToken(), "/", 60 * 60);
			CookieUtils.addCookie(response, "username", user.getUsername(), "/", 60);
			CookieUtils.addCookie(response, "jsessionid", ApplicationUtils.randomUUID(), "/", 60 * 60);
		}
		
		return param;
	}
	
	/**
	 * 获取最新的数据
	 * @param param
	 * @return
	 */
	@RequestMapping(value="latestData", method=RequestMethod.POST)
	@ResponseBody
	@ClientPermission
	public Object getLatestData(@RequestParam Map<String, Object> param) {
		Object ocode = param.get("code");
		SscResult page = new SscResult();
		if(ocode != null) {
			String code = (String) ocode;
			int num = (int) param.getOrDefault("rows", 5);
			List<SscEntity> rows = TodaySscCache.getLatest(code, num);
			page.setCode(code);
			page.setRows(rows.size());
			page.setData(rows);
		}
		return page;
	}
	
	/**
	 * 获取当天的数据
	 * @param param
	 * @return
	 */
	@RequestMapping(value="currentDay", method=RequestMethod.POST)
	@ResponseBody
	@ClientPermission
	public Object getCurrentDay(@RequestParam Map<String, Object> param) {
		String code = (String) param.get("code");
		List<SscEntity> rows = TodaySscCache.getCurrentDay(code);
		SscResult page = new SscResult();
		page.setCode(code);
		page.setRows(rows.size());
		page.setData(rows);
		return page;
	}
	
	/**
	 * @param param
	 * @return
	 */
	@RequestMapping(value="dailyDate", method=RequestMethod.POST)
	@ResponseBody
	@ClientPermission
	public Object getDailyDate(@RequestParam Map<String, Object> param) {
		String code = (String) param.getOrDefault("code", "undifined");
		String date = (String) param.getOrDefault("date", "");
		date = date.replaceAll("-", "");
		List<SscEntity> rows = sscService.selectCurrentDay(code, date);
		SscResult page = new SscResult();
		page.setCode(code);
		page.setRows(rows.size());
		page.setData(rows);
		return page;
	}
}
