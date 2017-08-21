/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月30日
 * 
 */
package com.eliteams.quick4j.web.aspect;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eliteams.quick4j.web.client.ClientSeesion;
import com.eliteams.quick4j.web.enums.ErrorCode;
import com.eliteams.quick4j.web.model.ClientUser;

/**
 * @author Administrator
 *
 */
@Aspect
@Component
public class ClientPermitAspect {

	private static Logger logger = Logger.getLogger(ClientPermitAspect.class);
	
	public ClientPermitAspect() {
		System.out.println("===========ClientPermitAspect================");
	}
	
	@Before("execution( * com.eliteams.quick4j.web.controller.ClientController.* (. .))")
	public void before(JoinPoint jp) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ClientUser user = getUser(request.getCookies());
        String method = jp.getSignature().getName();
        if("login".equals(method)) {
        	Object[] args = jp.getArgs();
        	user = (ClientUser) args[0];
        	logger.info("【" + user.getUsername() + "】try to login");
        } else {
        	logger.info("【" + user.getUsername() + "】visit method : " + method);
        }
		
	}
	
	@Around("@annotation(com.eliteams.quick4j.web.anotation.ClientPermission)")
	public Object check(ProceedingJoinPoint jp) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
		ClientUser user = getUser(request.getCookies());
		Map<String, Object> reterr = new HashMap<>();
		Object ret = null;
		if(user != null) {
			boolean logined = ClientSeesion.isOnline(user);
			if(logined) {
				try {
					ret = jp.proceed();
				} catch (Throwable e1) {
					e1.printStackTrace();
					reterr.put("errcode", ErrorCode.SSC_500);
					ret = reterr;
				}
			} else {
				reterr.put("errcode", ErrorCode.SSC_002);
				ret = reterr;
			}
		} else {
			reterr.put("errcode", ErrorCode.SSC_002);
			ret = reterr;
		}
		return ret;
	}
	
	/**
	 * 从cookie中提取用户信息
	 * @param cookies
	 * @return
	 */
	public ClientUser getUser(Cookie[] cookies) {
		String token = null;
        String username = null;
        if(cookies != null) {
        	for(Cookie cookie : cookies) {
        		String name = cookie.getName();
        		if("token".equals(name)) {
        			token = cookie.getValue();
        		} else if("username".equals(name)) {
        			username = cookie.getValue();
        		}
        	}
        }
        ClientUser user = null;
        if(username != null && token != null) {
        	user = new ClientUser();
        	user.setUsername(username);
        	user.setToken(token);
        }
		return user;
	}
}
