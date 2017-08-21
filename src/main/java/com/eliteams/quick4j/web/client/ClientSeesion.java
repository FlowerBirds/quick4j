/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月29日
 * 
 */
package com.eliteams.quick4j.web.client;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.eliteams.quick4j.web.model.ClientUser;
import com.eliteams.quick4j.web.util.MD5Util;

/**
 * @author Administrator
 *
 */
public class ClientSeesion {

	private static Map<String, ClientUser> session = new ConcurrentHashMap<>();
	
	/**
	 * @param user
	 */
	public static ClientUser login(ClientUser user) {
		user.setLastVisit(LocalDateTime.now());
		String text = user.getUsername() + user.getLastVisit().toString() + UUID.randomUUID().toString();
		String token = MD5Util.encode(text);
		user.setToken(token);
		user.setPassword(null);
		session.put(user.getUsername(), user);
		return user;
	}

	/**
	 * 检测是否在线
	 * @param user
	 * @return
	 */
	public static boolean isOnline(ClientUser user) {
		// Auto-generated method stub
		ClientUser client  = session.get(user.getUsername());
		if(client != null && client.equals(user)) {
			//更新最后方法时间
			client.setLastVisit(LocalDateTime.now());
			return true;
		} else {
			return false;
		}
	}
}
