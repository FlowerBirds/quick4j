/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Default
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SscResult {

	/**
	 * 彩票编码
	 */
	private String code;
	/**
	 * 提示信息
	 */
	private String info;
	/**
	 * 返回行数
	 */
	private int rows;
	
	/**
	 * 开奖信息 
	 */
	private List<SscEntity> data;
	
	
	private String errcode;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @return the data
	 */
	public List<SscEntity> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<SscEntity> data) {
		this.data = data;
	}
	
	public void update() {
		data.stream().forEach(entity -> entity.setCode(this.getCode()));
		data.stream().forEach(entity -> entity.setDate(entity.getExpect().substring(0, 8)));
	}

	/**
	 * @return the errcode
	 */
	public String getErrcode() {
		return errcode;
	}

	/**
	 * @param errcode the errcode to set
	 */
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
}
