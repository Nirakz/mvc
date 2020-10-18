package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vng.jcore.profiler.ProfilerLog;

public class MyLogger {
	public static ProfilerLog proLog = new ProfilerLog();
	public static Logger log = LoggerFactory.getLogger(MyLogger.class);
}
