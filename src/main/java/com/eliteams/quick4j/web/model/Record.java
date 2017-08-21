/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月26日
 * 
 */
package com.eliteams.quick4j.web.model;

/**
 * @author Administrator
 *
 */
public class Record {

	/**
	 * @param rows
	 * @param config
	 */
	public Record(int rows, UrlConfig config) {
		super();
		this.rows = rows;
		this.config = config;
	}
	
	private int rows;
	private UrlConfig config;
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
	 * @return the config
	 */
	public UrlConfig getConfig() {
		return config;
	}
	/**
	 * @param config the config to set
	 */
	public void setConfig(UrlConfig config) {
		this.config = config;
	}
	
	
}
