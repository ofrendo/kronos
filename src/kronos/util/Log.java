package kronos.util;

import org.apache.log4j.Logger;

public class Log {
	
	/* Get actual class name to be printed on */
	private static Logger log = Logger.getLogger(Log.class.getName());
   
	public static void info(String text) {
		log.info(text);
	}
    public static void warn(String text) {
	    log.warn(text);
    }
    public static void error(String text) {
 	    log.error(text);
    }
    public static void fatal(String text) {
        log.fatal(text);
    }
   
}
