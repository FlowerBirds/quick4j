/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月24日
 * 
 */
package com.eliteams.quick4j.web.model;

/**
 * @author Default
 *
 */
public class UrlConfig {

	/**
	 * 请求地址
	 */
	private String url;
	/**
	 * 请求频率
	 */
	private String rate;
	/**
	 * 请求url的彩票种类
	 */
	private String code;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public UrlConfig clone() {
		//Auto-generated method stub
		UrlConfig config = new UrlConfig();
		config.url = this.url;
		config.code = this.code;
		config.rate = this.rate;
		config.remark = this.remark;
		return config;
	}
	
	
}
