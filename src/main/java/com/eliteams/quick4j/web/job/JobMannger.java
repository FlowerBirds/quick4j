/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月27日
 * 
 */
package com.eliteams.quick4j.web.job;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class JobMannger {

	private static Map<String, ISeekJob> tasks = new HashMap<>();
	
	/**
	 * 启动任务，并进行管理
	 * @param code
	 * @param task
	 * @return
	 */
	public static synchronized boolean startSingleJob(String code, ISeekJob task) {
		ISeekJob t = tasks.get(code);
		if(t == null || t.isFinish()) {
			tasks.put(code, task);
			Thread thread = new Thread(task);
			thread.start();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 强制启动任务
	 * @param code
	 * @param task
	 * @return
	 */
	public static synchronized boolean forceStartJob(String code, ISeekJob task) {
		ISeekJob t = tasks.get(code);
		if(t != null && !t.isFinish()) {
			t.stop();
		}
		return startSingleJob(code, task);
	}
}
