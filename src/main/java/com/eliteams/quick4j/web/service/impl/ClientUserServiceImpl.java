/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月29日
 * 
 */
package com.eliteams.quick4j.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eliteams.quick4j.web.client.ClientSeesion;
import com.eliteams.quick4j.web.dao.ClientUserMapper;
import com.eliteams.quick4j.web.model.ClientUser;
import com.eliteams.quick4j.web.service.IClientUserService;

/**
 * @author Administrator
 *
 */
@Service
public class ClientUserServiceImpl implements IClientUserService {

	@Resource
	private ClientUserMapper clientMapper;
	
	/* (non-Javadoc)
	 * @see com.eliteams.quick4j.web.service.IClientUserService#loginUser(com.eliteams.quick4j.web.model.ClientUser)
	 */
	@Override
	public ClientUser loginUser(ClientUser user) {
		// Auto-generated method stub
		user.mask();
		List<ClientUser> clients = clientMapper.loginUser(user);
		if(clients.size() > 0) {
			return ClientSeesion.login(user);
		}
		return null;
	}

}
