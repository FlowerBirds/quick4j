/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.dao;

import java.util.List;

import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.web.model.SscEntity;
import com.eliteams.quick4j.web.model.SscResult;

/**
 * @author Default
 *
 */
public interface SscMapper extends GenericDao<SscResult, Long>{

	int insert(SscResult result);
	
	int insertList(List<SscEntity> results);
	
	List<SscEntity> selectSsc(SscEntity param);
}
