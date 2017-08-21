/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月29日
 * 
 */
package com.eliteams.quick4j.web.model;

import java.time.LocalDateTime;

import com.eliteams.quick4j.web.util.MD5Util;
import com.eliteams.quick4j.web.util.RSAUtil;

/**
 * @author Administrator
 *
 */
public class ClientUser {

	private String username;
	private String password;
	private String os;
	private String mac;
	private String token;
	
	private String status;
	private LocalDateTime lastVisit;
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}
	/**
	 * @param os the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	public void mask() {
		try {
			password = RSAUtil.decodeFast(password);
			password = MD5Util.encode(password);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the lastVisit
	 */
	public LocalDateTime getLastVisit() {
		return lastVisit;
	}
	/**
	 * @param lastVisit the lastVisit to set
	 */
	public void setLastVisit(LocalDateTime lastVisit) {
		this.lastVisit = lastVisit;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// Auto-generated method stub
		if(obj instanceof ClientUser) {
			ClientUser other = (ClientUser) obj;
			return username.equals(other.username)
					&& token.equals(other.token);
		}
		return super.equals(obj);
	}
	
	
}
