/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月27日
 * 
 */
package com.eliteams.quick4j.web.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.eliteams.quick4j.web.dao.SscMapper;
import com.eliteams.quick4j.web.http.ISscRequest;
import com.eliteams.quick4j.web.model.SscEntity;
import com.eliteams.quick4j.web.service.ISscService;

/**
 * @author Administrator
 *
 */
@Service
public class SscServiceImpl implements ISscService {

	@Autowired
	private ISscRequest sscRequest;
	
	@Resource
	private SscMapper sscMapper;
	
	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.service.ISscService#updateDaily(java.lang.String)
	 */
	@Override
	public int updateDaily() {
		// Auto-generated method stub
		sscRequest.requestDaily();
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.service.ISscService#selectCurrentDay(java.lang.String)
	 */
	@Override
	@Cacheable(value = { "sscSelect" }, key = "#code + #date")
	public List<SscEntity> selectCurrentDay(String code, String date) {
		//Auto-generated method stub
		SscEntity param = new SscEntity();
		param.setDate(date);
		param.setCode(code);
		List<SscEntity> list = sscMapper.selectSsc(param);
		list = list.parallelStream()
					.collect(Collectors.toMap(SscEntity::getExpect, x -> x, (x, y) -> x))
					.values()
					.stream()
					.collect(Collectors.toList());
		return list;
	}

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.service.ISscService#selectManyDays(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<SscEntity> selectManyDays(String code, String startDate, String endDate) {
		//Auto-generated method stub
		List<String> dates = new ArrayList<>();
		LocalDate sdate = LocalDate.parse(startDate);
		LocalDate eData = LocalDate.parse(endDate);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		while(sdate.isBefore(eData)) {
			dates.add(sdate.format(df));
			sdate = sdate.plusDays(1);
		}
		dates.add(eData.format(df));
		List<SscEntity> list = sscMapper.selectDays(code, dates);
		//去除重复的号
		list = list.parallelStream()
				.collect(Collectors.toMap(SscEntity::getExpect, x -> x, (x, y) -> x))
				.values()
				.stream()
				.collect(Collectors.toList());
		return list;
	}

	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.service.ISscService#selectManyDays(java.lang.String, java.lang.String)
	 */
	@Override
	public List<SscEntity> selectManyDays(String code, String days) {
		// Auto-generated method stub
		String[] dates = days.split(",");
		List<SscEntity> list = sscMapper.selectDays(code, Arrays.asList(dates));
		//去除重复的号
		list = list.parallelStream()
				.collect(Collectors.toMap(SscEntity::getExpect, x -> x, (x, y) -> x))
				.values()
				.stream()
				.collect(Collectors.toList());
		return list;
	}

}
