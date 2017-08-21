/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.http;

import java.util.List;

import com.eliteams.quick4j.web.model.Record;
import com.eliteams.quick4j.web.model.SscEntity;
import com.eliteams.quick4j.web.model.UrlConfig;

/**
 * @author Default
 *
 */
public interface ISscRequest {

	public boolean requestLatest();
	
	public List<SscEntity> selectSsc(SscEntity param);
	
	public List<Record> requestDaily();
	
	public Record requestDate(String date, String code);
	
	public Record requestDate(String date, UrlConfig config);
	
	public List<SscEntity> requestDaily(UrlConfig config);
}
