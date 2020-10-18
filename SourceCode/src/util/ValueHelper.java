package util;

import com.vng.jcore.profiler.ProfilerLog;

public class ValueHelper {
	// action name
	
	// polling time out
	final public static long GET_USER_ONLINE_TIMEOUT = 30000;	
	final public static long GET_LIST_MESSAGE_TIMEOUT = 30000;
	// executor delay
	final public static long ALIVE_CHECKER_DELAY = 60000;
	final public static long ALIVE_CHECKER_INIT_DELAY = 20000;
	final public static long HISTORY_STORE_DELAY = 60000;
	final public static long HISTORY_STORE_INIT_DELAY = 10000;
	// limit row when load more from history
	final public static int LIMIT_ROW_HISTORY = 10;
	//interval delete file after downloading the file; millisecond
	final public static int DELETE_FILE_INTERVAL = 5 * 60 * 1000; // 5 minutes
	
}
