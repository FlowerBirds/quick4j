/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月27日
 * 
 */
package com.eliteams.quick4j.web.service;

import java.util.List;

import com.eliteams.quick4j.web.model.SscEntity;

/**
 * @author Administrator
 *
 */
public interface ISscService {

	public int updateDaily();
	
	public List<SscEntity> selectCurrentDay(String code, String date);

	/**
	 * @param code
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<SscEntity> selectManyDays(String code, String startDate, String endDate);

	/**
	 * @param code
	 * @param days
	 * @return
	 */
	public List<SscEntity> selectManyDays(String code, String days);
}
