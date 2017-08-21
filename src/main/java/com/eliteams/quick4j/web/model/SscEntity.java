/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Default
 *
 */
public class SscEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -989552664571315734L;

	/**
	 * 开奖期数
	 */
	private String expect;
	
	/**
	 * 开奖号码
	 */
	private String opencode;
	/**
	 * 开奖时间
	 */
	private Date opentime;
	/**
	 * 开奖时间戳
	 */
	private long opentimestamp;
	
	/**
	 * 彩票编码
	 */
	private String code;
	
	/**
	 * 所属日期字符
	 */
	private String date;
	
	/**
	 * @return the expect
	 */
	public String getExpect() {
		return expect;
	}
	/**
	 * @param expect the expect to set
	 */
	public void setExpect(String expect) {
		this.expect = expect;
	}
	/**
	 * @return the opencode
	 */
	public String getOpencode() {
		return opencode;
	}
	/**
	 * @param opencode the opencode to set
	 */
	public void setOpencode(String opencode) {
		this.opencode = opencode;
	}
	/**
	 * @return the opentime
	 */
	public Date getOpentime() {
		return opentime;
	}
	/**
	 * @param opentime the opentime to set
	 */
	public void setOpentime(Date opentime) {
		this.opentime = opentime;
	}
	/**
	 * @return the opentimestamp
	 */
	public long getOpentimestamp() {
		return opentimestamp;
	}
	/**
	 * @param opentimestamp the opentimestamp to set
	 */
	public void setOpentimestamp(long opentimestamp) {
		this.opentimestamp = opentimestamp;
	}
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
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
