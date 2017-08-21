/**
 * @author 关小羽的刀
 * @github https://github.com/FlowerBirds/
 * @date 2017年7月27日
 * 
 */
package com.eliteams.quick4j.web.job;

/**
 * @author Administrator
 *
 */
public abstract class ISeekJob implements Runnable {

	protected boolean isStop = false;
	protected boolean isFinished = false;
	
	public void stop() {
		isStop = true;
	}
	
	public boolean isFinish() {
		return isFinished;
	}
}
