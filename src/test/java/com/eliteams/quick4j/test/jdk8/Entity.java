/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年8月3日
 * 
 */
package com.eliteams.quick4j.test.jdk8;

/**
 * @author Administrator
 *
 */
public class Entity {
	
	/**
	 * @param key
	 * @param value
	 */
	public Entity(String key, int value) {
		super();
		this.key = key;
		this.value = value;
	}
	private String key;
	private int value;
	
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
}
