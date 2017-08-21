/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月23日
 * 
 */
package com.eliteams.quick4j.core.http;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.eliteams.quick4j.web.model.SscEntity;
import com.eliteams.quick4j.web.model.SscResult;
import com.eliteams.quick4j.web.model.UrlConfig;

/**
 * @author Default
 *
 */
public class HttpManager {

	private static Logger logger = Logger.getLogger(HttpManager.class);

	/**
	 * GET请求
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		String content = null;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(url);
			logger.debug(maskURI(httpget.getURI()));
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			logger.debug(maskURI(httpget.getURI()) + " -> status: " + response.getStatusLine());
			if (entity != null) {
				logger.debug("Response content length: " + entity.getContentLength());
				logger.debug("Response content type: " + entity.getContentType().getValue());
				content =  EntityUtils.toString(entity, "utf-8");
				if(logger.isDebugEnabled()) {
					logger.debug(content);
				}
			}
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return content;
	}
	
	/**
	 * 屏蔽访问的token信息，防止泄露
	 * @param uri
	 * @return
	 */
	private static String maskURI(URI uri) {
		String url = uri.toString();
		{
			return url.replaceAll("token=(\\w{10})(\\w+)", "token=$1***");
		}
	}
	
	/**
	 * @param config
	 * @return
	 * @throws IOException 
	 */
	public static List<SscEntity> get(UrlConfig config) throws IOException { 
		String content = HttpManager.get(config.getUrl());
		if(content != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			try {
				SscResult result = mapper.readValue(content, SscResult.class);
				logger.debug(result.getCode() + " : " + result.getRows());
				result.update();
				return result.getData();
			} catch (IOException e) {
				//Auto-generated catch block
				e.printStackTrace();
				logger.error(config.getCode() + " :> " + e.getMessage(), e);
				throw e;
			}
		}
		return null;
	}
}
