/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月29日
 * 
 */
package com.eliteams.quick4j.web.dao;

import java.util.List;

import com.eliteams.quick4j.core.generic.GenericDao;
import com.eliteams.quick4j.web.model.ClientUser;

/**
 * @author Administrator
 *
 */
public interface ClientUserMapper extends GenericDao<ClientUser, String>{

	public List<ClientUser> loginUser(ClientUser user);
	
}
