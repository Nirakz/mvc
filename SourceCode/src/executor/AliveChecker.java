package executor;

import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jetty.continuation.Continuation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Message;
import util.MyLogger;
import util.ValueHelper;

public class AliveChecker implements Runnable {

	private Map<String, Long> userOnlineMap;
	private Map<String, Continuation> continuationMap;
	private String idChangedUser;
	private Logger log;
	
	public Map<String, Long> getUserOnlineMap() {
		return userOnlineMap;
	}

	public void setUserOnlineMap(Map<String, Long> userOnlineMap) {
		this.userOnlineMap = userOnlineMap;
	}

	public Map<String, Continuation> getContinuationMap() {
		return continuationMap;
	}

	public void setContinuationMap(Map<String, Continuation> continuationMap) {
		this.continuationMap = continuationMap;
	}



	public AliveChecker(Map<String, Long> userOnlineMap,
			Map<String, Continuation> continuations,
			String idChangedUser) {

		this.userOnlineMap = userOnlineMap;
		this.continuationMap = continuations;
		this.idChangedUser = idChangedUser;
		this.log = MyLogger.log;
	}

	@Override
	public void run() {

		log.info("Checking.....");
		synchronized (userOnlineMap) {
			Iterator<Map.Entry<String, Long>> iter = userOnlineMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Long> entry = iter.next();
				String name = entry.getKey();
				Long nowTime = new Date().getTime();
				Long lastTime = entry.getValue();
				log.info(name + " time  " + (nowTime - lastTime));
				if (nowTime - lastTime > ValueHelper.GET_USER_ONLINE_TIMEOUT) { // 30000 ms
					log.info("Kick " + name);
					iter.remove();
		
					synchronized (continuationMap) {
						continuationMap.remove(name);	
					}
					synchronized (idChangedUser) {
						idChangedUser = name;
					}
					for (Continuation con : continuationMap.values()) {
						if (con != null && con.isSuspended()) {
							con.resume();
						}
					}

				}
			}
		}

	}

}
